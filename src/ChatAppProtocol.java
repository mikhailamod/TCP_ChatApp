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

/**
 *
 * @author mikhail
 */
public class ChatAppProtocol
{
	ChatAppClient client;
	ServerThread serverThread;
	boolean forClient;
	Message message;
	
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
	
	private synchronized void parseServer(Message m)
	{
		ChatAppServer server = serverThread.getServer();
		int id = serverThread.getID();
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

		//TODO - change to else if for broadcast and file. last else must catch corrupted message.
		else if(m.getTag().equals("broadcast") || m.getTag().equals("image") || m.getTag().equals("video"))
		{
			System.out.println(id + " recieved broadcast message:");//debug
			System.out.println(m.toString());//print message to server console
			server.broadcast(id, m);
		}
		//else this message is somehow corrupt, send error
		else
		{
			System.out.println(id + " SERIOUS ERROR. Message tag corrupted");//debug
			server.handleError(id);
		}//end else
	}
	
	//given a message, look at the tag to decide what to do.
	private synchronized void parseClient(Message m)
	{
		GUI_Main gui = client.getGUI();
		if (m.getTag().equals("broadcast"))
		{
            gui.receive("[Public Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
        } 
        else if (m.getTag().equals("private"))//for user to user message
		{
            gui.receive("[Private Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
        } 
        else if (m.getTag().equals("image"))//for image broadcast
		{
			String dialogMessage = m.getUser().getUsername() + " wants to send you an image. Do you accept?";
            int decision = JOptionPane.showConfirmDialog(null, dialogMessage, "File transfer request", JOptionPane.YES_NO_OPTION);
            //checks if user wants to accept the file
            if (decision == JOptionPane.YES_OPTION) {
                try {
                    //turns byte array from received file into a stream
                    ByteArrayInputStream bis = new ByteArrayInputStream(m.getFile());
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
        } else if (m.getTag().equals("video"))
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
                m.outputFile(saveDest, fname, ext);
                System.out.println("File Successfully Downloaded!");
				gui.receive("File Successfully Downloaded to " + saveDest);
            }
        }//end else if
        else if (m.getTag().equals("userList"))
		{
            System.out.println("Recieving user list - " + m.getUserList().get(0));
            gui.receive(m);
        }

		else if (m.getTag().equals("end"))
		{
			System.out.println("Debug protocol client user exit: " + m.getData());
            System.out.println(m.toString());
			gui.receive(m.getData());
        }
	}
}
