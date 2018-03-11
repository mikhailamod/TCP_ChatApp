//Client side of Chat App
//Properly deal with errors and stop threads

import java.net.*;
import java.io.*;
import java.io.File;
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
import java.io.BufferedReader;

public class ChatAppClient implements Runnable
{
	private int portNumber;
	private User activeUser;

	private ObjectOutputStream outputObject;
	private BufferedReader consoleIn;

	private ClientThread clientThread;	
	private Thread thread = null;
	private Socket socket;
	private Message m = null;

	//Global Variables
	Scanner kb;

	public ChatAppClient(String hostname, int portNumber, String user_name)
	{
		System.out.println("Starting ChatAppClient()");

		this.portNumber = portNumber;
		activeUser = new User(user_name, hostname);

		kb = new Scanner(System.in);

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
		consoleIn = new BufferedReader(new InputStreamReader(System.in));//to read from console

		if(thread == null)
		{
			clientThread = new ClientThread(socket, this);
			thread = new Thread(this);
			thread.start();
		}
	}

	//do the follwoing while ChatAppClient is running
	public void run()
	{
		//System.out.println("ChatAppClient thread running\nType a message to broadcast it\n");
		while(thread != null)
		{
			try
			{

			
            System.out.println("Would you like to send a media file? (y/n): ");
            boolean decision = (kb.nextLine().substring(0, 1).equalsIgnoreCase("y"));

            if (decision) {

                System.out.println("Please Enter the file Path: ");

                String in = kb.nextLine();

                String extension = in.substring(in.lastIndexOf("."));

                System.out.println(extension);

                Path path = Paths.get(in);

                String mime = getMIME(extension);


	                if (new File(in).exists() && !new File(in).isDirectory()) {
	                    
	                    byte[] fileContent = Files.readAllBytes(path);

	                    if (mime.equalsIgnoreCase("image")) {
	                    	m = new Message(fileContent, activeUser, "image", in);
	                    }
	                    else if (mime.equalsIgnoreCase("video")) {
	                       	m = new Message(fileContent, activeUser, "video", in);
	                    }
	                } 
                
	                else {
	                    System.out.println("File Path Does Not Exist!");
	                }

                }
                else {
                	System.out.println("Please Enter a message:");
					String userInput = consoleIn.readLine();//this is the message that will eventually be sent to another user.
					m = new Message(userInput, activeUser);
                }


                if(m!=null){
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
		if(m.getTag().equals("message")) {
			System.out.println(m.getUser().getUsername() + " says:" + m.toString() + "\n");
		}

		else if(m.getTag().equals("image")) {
			System.out.println(m.getUser().getUsername() + " wants to send you an image. Do you accept(y/n):" + "\n");
			boolean decision = (kb.nextLine().substring(0, 1).equalsIgnoreCase("y"));
			if(decision) {
				try {
	                ByteArrayInputStream bis = new ByteArrayInputStream(m.getFile());
	                BufferedImage img = ImageIO.read(bis);
	                System.out.println("IMG");
	                System.out.println("CALLLED!");

	                ImageIcon icon = new ImageIcon(img);
	                JFrame frame = new JFrame();
	                frame.setLayout(new FlowLayout());
	                frame.setSize(500, 500);
	                JLabel lbl = new JLabel();
	                lbl.setIcon(icon);
	                frame.add(lbl);
	                frame.setVisible(true);
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
				catch(Exception e) {

				}
			}
		}
		else if(m.getTag().equals("video")) {

			String fname = m.getFilepath();
			String ext = fname.substring(m.getFilepath().lastIndexOf("."));
            System.out.println("Receiving Video File!");
            m.outputFile(fname, ext);
            System.out.println("File Successfully Downloaded!");
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