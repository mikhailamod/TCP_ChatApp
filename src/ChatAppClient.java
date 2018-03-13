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
import javax.swing.JOptionPane;

public class ChatAppClient implements Runnable
{
	private int portNumber;
	private User activeUser;

	private ObjectOutputStream outputObject;
	//private ObjectOutputStream outputUser;
	private BufferedReader consoleIn;

	private ClientThread clientThread;	
	private Thread thread = null;
	private Socket socket;
	private Message m = null;
	private boolean hasSent;

	//Global Variables
	//Scanner kb;

	public ChatAppClient(String hostname, int portNumber, String user_name)
	{
		System.out.println("Starting ChatAppClient()");

		this.portNumber = portNumber;
		activeUser = new User(user_name, hostname);
		hasSent = false;

		//kb = new Scanner(System.in);

		System.out.println("Logged in with the following details:\n" + activeUser.toString());
		try
		{
			socket = new Socket(hostname, portNumber);//connect to server
			start();//open IO
		}
		catch (UnknownHostException u)
		{
			System.out.println("Error in ChatAppClient con()");
		}
		catch (IOException ioe)
		{
			System.out.println("ERR");
		}

	}//end con

	//initialize IO for user. Start threads.
	public void start() throws IOException
	{
		outputObject = new ObjectOutputStream(socket.getOutputStream());//send a Message obj to server
		//outputUser = new ObjectOutputStream(socket.getOutputStream());
		consoleIn = new BufferedReader(new InputStreamReader(System.in));//to read from console

		if(thread == null)
		{
			clientThread = new ClientThread(socket, this);
			thread = new Thread(this);
			thread.start();
		}
	}

	public void sendUserObject(User user)
	{
		if(!hasSent)
		{
			try
			{
				Message m = new Message("", user);
				m.setTag("userTransfer");
				outputObject.writeObject(m);
				System.out.println("DEBUG: Sent unser");
				hasSent = true;
			}
			catch (IOException ioe)
			{
				System.out.println("Error in ChatAppClient sendUserObject");
			}
		}
		
		
	}

	//do the follwoing while ChatAppClient is running
	public void run()
	{
		sendUserObject(activeUser);
		//System.out.println("ChatAppClient thread running\nType a message to broadcast it\n");
		while(thread != null)
		{
			System.out.println("Commands:\n'@broadcast': send a message to everyone,\n'@file': send a file");
			try
			{
				String userInput = consoleIn.readLine();//this is the message that will eventually be sent to another user.
	            if (userInput.equalsIgnoreCase("@file"))
	            {
	                System.out.println("Please Enter the file Path: ");
	                //retrieves confirmation gets file name and directory from the input
	                String in = consoleIn.readLine();
	                String extension = in.substring(in.lastIndexOf("."));
	                Path path = Paths.get(in);
	                //retrieves which format the file extension fallls under (Image/video) 
	                String mime = getMIME(extension);

	            	//checks file exists
	                if (new File(in).exists() && !new File(in).isDirectory())
	                {
	                    
	                    //reads in file as a byte array
	                    byte[] fileContent = Files.readAllBytes(path);

	                    //creates message with correct format
	                    if (mime.equalsIgnoreCase("image")) {
	                    	m = new Message(fileContent, activeUser, "image", in);
	                    }
	                    else if (mime.equalsIgnoreCase("video")) {
	                       	m = new Message(fileContent, activeUser, "video", in);
	                    }
	                } 
	            
	                else
	                {
	                    System.out.println("File Path Does Not Exist!");
	                }

	            }
	            else if(userInput.equalsIgnoreCase("@broadcast"))
	            {
	            	System.out.println("Please Enter a message:");
	            	userInput = consoleIn.readLine();
					m = new Message(userInput, activeUser);
	            }

	            else if(userInput.substring(0,5).equals("@user"))
	            {
	            	String send_to = userInput.substring(6);
	            	System.out.println(send_to + " DEBUGF");
	            	System.out.println("Please Enter a message:");
	            	userInput = consoleIn.readLine();
					m = new Message(userInput, activeUser);
					m.setTag("private");
					m.setUserTo(send_to);//format of input is @user:name
	            }

	            if(m!=null)
	            {
					outputObject.writeObject(m);
					System.out.println("<Your message has been sent>\n");         	
	            }

			}//end try
			catch (IOException ie)
			{
				System.out.println("Error in ChatAppClient run() IO");
				System.exit(1);//replace with proper way to deal with errors
			}
			
		}
	}//end run

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

	public void recieve(Message m)
	{
		//for broadcasts
		//TO DO: change message to broadcast
		if(m.getTag().equals("message")) {
			System.out.println("[Public Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
		}

		//for user to user message
		else if(m.getTag().equals("private"))
		{
			System.out.println("[Private Message]" + m.getUser().getUsername() + " says:" + m.toString() + "\n");
		}
		//for image broadcast
		else if(m.getTag().equals("image")) {
			//System.out.println(" + "\n");
			
			int decision = JOptionPane.showConfirmDialog(null, m.getUser().getUsername() + " wants to send you an image. Do you accept?");
			//checks if user wants to accept the file
			//boolean decision = (kb.nextLine().substring(0, 1).equalsIgnoreCase("y"));
			if(decision==JOptionPane.YES_OPTION) {
				try {
					//turns byte array from received file into a stream
	                ByteArrayInputStream bis = new ByteArrayInputStream(m.getFile());
	                //must convert to bufferedimage in order to display
	                BufferedImage img = ImageIO.read(bis);
	                //jframe created with icon to display image .. will work on dimensions etc int the GUI stage
	                ImageIcon icon = new ImageIcon(img);
	                JFrame frame = new JFrame();
	                frame.setLayout(new FlowLayout());
	                frame.setSize(500, 500);
	                JLabel lbl = new JLabel();
	                lbl.setIcon(icon);
	                frame.add(lbl);
	                frame.setVisible(true);
				}
				catch(Exception e) {

				}
			}
		}
		else if(m.getTag().equals("video")) {
			int decision = JOptionPane.showConfirmDialog(null, m.getUser().getUsername() + " wants to send you a video. Do you accept?");
			//checks if user wants to accept the file
			//boolean decision = (kb.nextLine().substring(0, 1).equalsIgnoreCase("y"));
			if(decision==JOptionPane.YES_OPTION) {
					//get file with file path and start downloading it
					String fname = m.getFilepath();
					String ext = fname.substring(m.getFilepath().lastIndexOf("."));
		            System.out.println("Receiving Video File!");
		            m.outputFile(fname, ext);
		            System.out.println("File Successfully Downloaded!");
        		}
		}

		if(m.getTag().equals("end"))
		{
			System.out.println(m.toString());
		}
		
	}

	public static void main(String[] args) throws IOException 
	{


		
		String hostname = "";
		int port = 6000;

		//Start up checks
		if(args.length == 0)//use hostname command to get hostname
		{
			hostname = getHostName(port);
		}
		else if(args.length == 1)
		{
			hostname = args[0];
		}
		else
		{
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		}
		//get user info
		System.out.println("Enter a username:");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String user_in = br.readLine();

		if(AuthManager.exists(user_in))//if username exists, make them login
		{
			login(user_in);
		}
		else
		{
			register(user_in);
		}
		
		ChatAppClient app = new ChatAppClient(hostname, port, user_in);

		// adds a hook that runs the shutdown method when the program ends
		Runtime.getRuntime().addShutdownHook
		(
			new Thread()
			{
				public void run()
				{
					app.shutdown();
				}
			}
		);


	}//end main

	//get a password input from user - uses Unix console password format (i.e doesnt show)
	//if check, then change text
	public static String getPassword(boolean check)
	{
		String text = "Enter a password: ";
		if(check){ text = "Re-enter your password: "; }
		Console console = System.console();
		char[] input = console.readPassword(text);
		String password = new String(input);
		return password;
	}

	//given a username, get a password and check if it is a correct combo in database.
	public static void login(String username)
	{
		String password = getPassword(false);
		String hashed = AuthManager.hashPassword(password);
		while (!AuthManager.checkPassword(username, hashed)) {
			System.out.println("Username/password incorrect");
			password = getPassword(false);
			hashed = AuthManager.hashPassword(password);
		}//end while
		
	}

	//given a username, get a password and append details to database.
	public static void register(String username)
	{
		String password = getPassword(false);
		boolean matches = false;
		while(!matches)
		{
			String confirm_password = getPassword(true);
			if(password.equals(confirm_password))
			{
				matches = true;
			}
			else
			{
				System.out.println("Passwords do not match");
			}
		}//end while

		//hash password and write to file
		//TO DO: hash password with SHA-256, parse into byte to hex, write hex to file
		String hashed = AuthManager.hashPassword(password);
		AuthManager.writeToFile(username, hashed);
		
	}//end register

	//Returns hostname
	public static String getHostName(int portNumber)
	{
		String hostname = null;
		System.out.println("Fetching hostname and using Port Number " + portNumber);
		try{

			Process p = Runtime.getRuntime().exec("hostname");//run the hostname command
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;

			while((line = input.readLine()) != null)
			{
				hostname = line;
				//System.out.println("DEBUG: " + hostname);
				
			}//end while
			//System.out.println("DEBUG2: " + hostname);
			return hostname;
		}//end try
		catch (IOException e)
		{
			System.out.println(e);
			return e.toString();
		}
	}//end getHostName

	//runs when program shutdowns. send a message to the server with the tag "end"
	public void shutdown() 
	{
		System.err.println("shutdown");
		try
		{
			Message m = new Message("", activeUser);
			m.setTag("end");
			outputObject.writeObject(m);
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
    	
  	}

}//end class