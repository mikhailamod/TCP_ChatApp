import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//AMDMIK002, ABRRIY002, SNGPAV002
//This class represents our implementation of our protocol.
public class ChatAppProtocol
{
	ChatAppClient client;
	ServerThread serverThread;
	boolean forClient;//true if initialized by client, false if initialized by server
	Message message;//the message that the client/server wants to parse
	
	//Constructor for client initiated protocol
	public ChatAppProtocol(ChatAppClient client, Message message)
	{
		this.client = client;
		this.message = message;
		forClient = true;
	}
	//Constructor for server initiated protocol
	public ChatAppProtocol(ServerThread thread, Message message)
	{
		this.serverThread = thread;
		this.message = message;
		forClient = false;
	}
	
	//public method that both classes can call
	public synchronized void parseMessage()
	{
		if(forClient)
		{
			parseClient(message);
		}
		else
		{
			parseServer(message);
		}
	}
	
	//if the server started the protocol, do these things
	private synchronized void parseServer(Message m)
	{
		ChatAppServer server = serverThread.getServer();//get the ChatAppServer that the ServerThread belongs to
		int id = serverThread.getID();//get the id of the ServerThread (represents an individual client)
		User activeUser = serverThread.getUser();
		if(m.getTag().equals("end"))
		{
			//inform all clients that this client is leaving
			String bye_text = m.getUser().getUsername() + " has left.\n";
			System.out.println(bye_text);
			m.setData(bye_text);
			System.out.println("Debug protocol user exit: " + m.getData());
			server.broadcast(id, m);
			//close IO
			serverThread.exit();
		}

		//this message will contain no data, only a User object which we will assign to activeUser
		else if(m.getTag().equals("userTransfer"))
		{
			System.out.println("DEBUG USER - " + m.getUser().getUsername());
			activeUser = new User(m.getUser().getUsername(), m.getUser().getHostName());
			serverThread.setUser(activeUser);
			server.updateUserList();
		}

		//contains details of private message
		else if(m.getTag().equals("private"))
		{
			System.out.print(id + " recieved private message:");//debug
			System.out.println(m.toString());//print message to server console
			server.privateMessage(id, m);
		}

		//broadcast messages and files are sent to all clients.
		else if(m.getTag().equals("broadcast") || m.getTag().equals("image") || m.getTag().equals("video"))
		{
			System.out.println(id + " recieved broadcast message:");//debug
			System.out.println(m.toString());//print message to server console
			server.broadcast(id, m);//send to all clients
		}

		//else this message is somehow corrupt, send error
		else
		{
			System.out.println(id + " SERIOUS ERROR. Message tag corrupted");//debug
			server.handleError(id);
		}//end else
	}
	
	//if the client started the protocol, do these things
	private synchronized void parseClient(Message m)
	{
		GUI_Main gui = client.getGUI();//get the GUI associated with the client

		if (m.getTag().equals("broadcast"))//message received was a broadcast, print appropiate text
		{
            gui.receive("[Public Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
        } 
        else if (m.getTag().equals("private"))//message received was private, print appropiate text
		{
            gui.receive("[Private Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
        } 
        else if (m.getTag().equals("image"))//message received was an image. Prompt user to accept it, and then open it
		{
			String dialogMessage = m.getUser().getUsername() + " wants to send you an image. Do you accept?";
            int decision = JOptionPane.showConfirmDialog(null, dialogMessage, "File transfer request", JOptionPane.YES_NO_OPTION);
            //checks if user wants to accept the file
            if (decision == JOptionPane.YES_OPTION) {
                try {
                	Compression comp = new Compression(m.getFile());
                    //turns byte array from received file into a stream
                    ByteArrayInputStream bis = null;
                    try {
                    	bis = new ByteArrayInputStream(comp.decompress());
                    }
                    catch(Exception e) {
                    	System.out.println("Error Decompressing!");
                    }
                    
                    //must convert to bufferedimage in order to display
                    BufferedImage img = ImageIO.read(bis);
                    //jframe created with icon to display image
                    ImageIcon icon = new ImageIcon(img);
					int height = icon.getIconHeight();
					int width = icon.getIconWidth();
					
                    JFrame frame = new JFrame();
                    frame.setLayout(new FlowLayout());
                    frame.setSize(height, width);
					
                    JLabel lbl = new JLabel();
                    lbl.setIcon(icon);
					
					frame.setTitle("Image from " + m.getUser().getUsername());
                    frame.add(lbl);
                    frame.setVisible(true);
                } catch (IOException e)
				{
					System.out.println("Error in displaying image");
					gui.displayError(e, "Error in displaying image");
                }
            }
        } else if (m.getTag().equals("video"))//message received contains a video. Prompt user to accept, and then save to a chosen location.
		{
            String dialogMessage = m.getUser().getUsername() + " wants to send you an video. Do you accept?";
            int decision = JOptionPane.showConfirmDialog(null, dialogMessage, "File transfer request", JOptionPane.YES_NO_OPTION);
            //checks if user wants to accept the file
            if (decision == JOptionPane.YES_OPTION) {
                //get file with file path and start downloading it
				//get save destination
				String saveDest = "";
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					saveDest = file.getAbsolutePath();
				}
				
				//get file from message
                String fname = m.getFilepath();
                String ext = fname.substring(m.getFilepath().lastIndexOf("."));
                
				System.out.println("Receiving Video File...");
				gui.receive("Receiving Video File...");
				try {
					m.outputFile(saveDest, fname, ext);
				}
                catch(Exception e) {
                	System.out.println(e);
                }
                System.out.println("File Successfully Downloaded!");
				gui.receive("File Successfully Downloaded to " + saveDest);
            }
        }//end else if
        else if (m.getTag().equals("userList"))//message contains a list of active users. 
		{
            System.out.println("Recieving user list - " + m.getUserList().get(0));
            gui.receive(m);
        }

		else if (m.getTag().equals("end"))//message contains details of a client who left
		{
			System.out.println("Debug protocol client user exit: " + m.getData());
            System.out.println(m.toString());
			gui.receive(m.getData());
        }
	}
}
