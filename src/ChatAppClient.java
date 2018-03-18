//Client side of Chat App
//Properly deal with errors and stop threads

import java.net.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ChatAppClient {

    private int portNumber;//the port number this client will connect to
    private User activeUser;//the User object associated with this class

    private ObjectOutputStream outputObject;
    private ObjectInputStream GUI_Input;
    private BufferedReader consoleIn;//console input for when no gui is used

    private ClientThread clientThread;//ThreadClass that this client uses to accept messages
    private Thread thread = null;//Runnable thread that this class uses to send messages
    private Socket socket;
    private Message m = null;
    private boolean hasSentUser;//boolean indicating whether client has sent information about user to the server.
    private GUI_Main gui;

    //Global Variables
    //Scanner kb;
    public ChatAppClient(String hostname, int portNumber, String user_name, GUI_Main gui) {
        System.out.println("Starting ChatAppClient()");

        this.portNumber = portNumber;
        activeUser = new User(user_name, hostname);
        hasSentUser = false;

        this.gui = gui;

        System.out.println("Logged in with the following details:\n" + activeUser.toString());
        try {
            socket = new Socket(hostname, portNumber);//connect to server
            start();//open IO
        } catch (UnknownHostException u) {
            System.out.println("Error in ChatAppClient con()");
        } catch (IOException ioe) {
            System.out.println("ERR");
        }

    }//end con

    //initialize IO for user. Start threads.
    public synchronized void start() throws IOException {
        outputObject = new ObjectOutputStream(socket.getOutputStream());//send a Message obj to server
        consoleIn = new BufferedReader(new InputStreamReader(System.in));//to read from console
		clientThread = new ClientThread(socket, this);
		sendUserObject(activeUser);
    }
	
	//Send user information to server
	//this allows Server to keep track of active users.
    public synchronized void sendUserObject(User user) {
        if (!hasSentUser) {
            try {
                Message m = new Message("", user);
                m.setTag("userTransfer");
                outputObject.writeObject(m);
                hasSentUser = true;
            } catch (IOException ioe) {
                System.out.println("Error in ChatAppClient sendUserObject");
				gui.displayError(ioe, "Error sending User object");
            }
        }

    }
	
    //given a message type, data and intended receipient, write to OutputStream
    public void send(String type, String message, String sendTo) {
        Message m = new Message(message, activeUser);
        m.setTag(type);
        m.setUserTo(sendTo);
        try {
            outputObject.writeObject(m);
        } catch (IOException ioe) {
            System.out.println("Error in send() ChatAppClient");
        }
    }

    //Overloaded send function for files
    public void send(String in, String sendTo) throws IOException {

        Message m = null;

        String extension = in.substring(in.lastIndexOf("."));
        Path path = Paths.get(in);
        //retrieves which format the file extension fallls under (Image/video) 
        String mime = getMIME(extension);

        //checks file exists
        if (new File(in).exists() && !new File(in).isDirectory()) {

            //reads in file as a byte array
            byte[] fileContent = Files.readAllBytes(path);

            //creates message with correct format
            if (mime.equalsIgnoreCase("image")) {
                m = new Message(fileContent, activeUser, "image", in);
            } else if (mime.equalsIgnoreCase("video")) {
                m = new Message(fileContent, activeUser, "video", in);
            }
        }

        m.setUserTo(sendTo);
        try {
            outputObject.writeObject(m);
        } catch (IOException ioe) {
            System.out.println("Error in send() ChatAppClient");
        }
    }

    //get file category based on extension
    public static String getMIME(String ext) throws IOException {
        if (ext.contains("png") || ext.contains("jpg") || ext.contains("jpeg")) {
            return "image";
        } else if (ext.contains("mp4") || ext.contains("mkv") || ext.contains("avi")) {
            return "video";
        } else {
            return "message";
        }

    }

    //Given a Message obj, send to GUI with relevant output
    public void recieve(Message m) {
        //for broadcasts
        if (m.getTag().equals("broadcast"))
		{
            gui.recieve("[Public Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
            //System.out.println("[Public Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
        } //for user to user message
        else if (m.getTag().equals("private"))
		{
            gui.recieve("[Private Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
            //System.out.println("[Private Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
        } //for image broadcast
        else if (m.getTag().equals("image"))
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
				gui.recieve("Receiving Video File...");
                m.outputFile(saveDest, fname, ext);
                System.out.println("File Successfully Downloaded!");
				gui.recieve("File Successfully Downloaded to " + saveDest);
            }
        }//end else if
        else if (m.getTag().equals("userList"))
		{
            System.out.println("Recieving user list - " + m.getUserList().get(0));
            gui.recieve(m);
        }

		else if (m.getTag().equals("end"))
		{
            System.out.println(m.toString());
			gui.recieve(m.getData());
        }

    }

    //get a password input from user - uses Unix console password format (i.e doesnt show)
    //if check, then change text
    public static String getPassword(boolean check) {
        String text = "Enter a password: ";
        if (check) {
            text = "Re-enter your password: ";
        }
        Console console = System.console();
        char[] input = console.readPassword(text);
        String password = new String(input);
        return password;
    }

    //given a username, get a password and check if it is a correct combo in database.
    public static void login(String username) {
        String password = getPassword(false);
        String hashed = AuthManager.hashPassword(password);
        while (!AuthManager.checkPassword(username, hashed)) {
            System.out.println("Username/password incorrect");
            password = getPassword(false);
            hashed = AuthManager.hashPassword(password);
        }//end while

    }

    //given a username, get a password and append details to database.
    public static void register(String username) {
        String password = getPassword(false);
        boolean matches = false;
        while (!matches) {
            String confirm_password = getPassword(true);
            if (password.equals(confirm_password)) {
                matches = true;
            } else {
                System.out.println("Passwords do not match");
            }
        }//end while

        //hash password and write to file
        //TO DO: hash password with SHA-256, parse into byte to hex, write hex to file
        String hashed = AuthManager.hashPassword(password);
        AuthManager.writeToFile(username, hashed);

    }//end register

    //Returns hostname
    public static String getHostName(int portNumber) {
        String hostname = null;
        System.out.println("Fetching hostname and using Port Number " + portNumber);
        try {

            Process p = Runtime.getRuntime().exec("hostname");//run the hostname command
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;

            while ((line = input.readLine()) != null) {
                hostname = line;
                //System.out.println("DEBUG: " + hostname);

            }//end while
            //System.out.println("DEBUG2: " + hostname);
            return hostname;
        }//end try
        catch (IOException e) {
            System.out.println(e);
            return e.toString();
        }
    }//end getHostName

    //runs when program shutdowns. send a message to the server with the tag "end"
    public void shutdown() {
        System.err.println("shutdown");
        try {
            Message m = new Message("", activeUser);
            m.setTag("end");
            outputObject.writeObject(m);
			clientThread.exit();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}//end class
