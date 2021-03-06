//Client side of Chat App.
//AMDMIK002, ABRRIY002, SNGPAV002

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
    private Socket socket;
    private Message m = null;
    private boolean hasSentUser;//boolean indicating whether client has sent information about user to the server.
    private GUI_Main gui;

    //Global Variables
    //Scanner kb;
    public ChatAppClient(String hostname, int portNumber, String user_name, GUI_Main gui)
	{
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
	
	//getters
	public GUI_Main getGUI(){ return gui; }

    //initialize IO for user. Start threads.
    public synchronized void start() throws IOException
	{
        outputObject = new ObjectOutputStream(socket.getOutputStream());//send a Message obj to server
        consoleIn = new BufferedReader(new InputStreamReader(System.in));//to read from console
		clientThread = new ClientThread(socket, this);
		sendUserObject(activeUser);
    }
	
	//Send user information to server
	//this allows Server to keep track of active users.
    public synchronized void sendUserObject(User user)
	{
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

    }//end sendUserObject
	
    //given a message type, data and intended receipient, write to OutputStream
    public synchronized void send(String type, String message, String sendTo)
	{
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
    public synchronized void send(String incoming_file, String sendTo) throws IOException
	{

        Message m = null;

        String extension = incoming_file.substring(incoming_file.lastIndexOf("."));
        Path path = Paths.get(incoming_file);
        //retrieves which format the file extension fallls under (Image/video) 
        String mime = getMIME(extension);

        //checks file exists
        if (new File(incoming_file).exists() && !new File(incoming_file).isDirectory()) {

            //reads in file as a byte array
            byte[] fileContent = Files.readAllBytes(path);
            //Start Compressing
            Compression comp = new Compression(fileContent);
            //creates message with correct format
            if (mime.equalsIgnoreCase("image")) {
                m = new Message(comp.compress(), activeUser, "image", incoming_file);
            } else if (mime.equalsIgnoreCase("video")) {
                m = new Message(comp.compress(), activeUser, "video", incoming_file);
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
    public static String getMIME(String ext) throws IOException
	{
        if (ext.contains("png") || ext.contains("jpg") || ext.contains("jpeg")) {
            return "image";
        } else if (ext.contains("mp4") || ext.contains("mkv") || ext.contains("avi")) {
            return "video";
        } else {
            return "message";
        }

    }

    //Given a Message obj, send to GUI with relevant output
    public synchronized void receive(Message m) {
        //for broadcasts
        ChatAppProtocol protocol = new ChatAppProtocol(this, m);
		protocol.parseMessage();
    }//end recieve

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
    public synchronized void shutdown() {
        System.err.println("shutdown");
        try {
            Message m = new Message("", activeUser);
            m.setTag("end");
			System.out.println("Debug CAC user exit: " + m.getData());
            outputObject.writeObject(m);
			clientThread.exit();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}//end class
