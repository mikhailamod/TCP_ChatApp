//Client side of Chat App
//Properly deal with errors and stop threads

import java.net.*;
import java.io.*;

public class ChatAppClient implements Runnable
{
	private int portNumber;
	private User activeUser;

	private ObjectOutputStream outputObject;
	private BufferedReader consoleIn;

	private ClientThread clientThread;	
	private Thread thread = null;
	private Socket socket;

	public ChatAppClient(String hostname, int portNumber, String user_name)
	{
		System.out.println("Starting ChatAppClient()");

		this.portNumber = portNumber;
		activeUser = new User(user_name, hostname);

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
		System.out.println("ChatAppClient thread running\nType a message to broadcast it\n");
		while(thread != null)
		{
			try
			{
				String userInput = consoleIn.readLine();//this is the message that will eventually be sent to another user.
				Message m = new Message(userInput, activeUser);

				outputObject.writeObject(m);
				System.out.println("<Your message has been sent>\n");
			}//end try
			catch (IOException ie)
			{
				System.out.println("Error in ChatAppClient run() IO");
				System.exit(1);//replace with proper way to deal with errors
			}
			
		}
	}//end run

	public void recieve(Message m)
	{
		if(m.getTag().equals("message"))
		{
			System.out.println(m.getUser().getUsername() + " says:" + m.toString() + "\n");
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