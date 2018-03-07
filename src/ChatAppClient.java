//Client side of Chat App
//TODO: change start up check into a method
//		initiailize User object

import java.net.*;
import java.io.*;

public class ChatAppClient
{
	static int portNumber = ChatAppServer.PORT_NUMBER;
	public static void main(String[] args) throws IOException {
		
		String hostname = "";
		User activeUser;

		//Start up checks
		if(args.length == 0)//use hostname command to get hostname
		{
			hostname = getHostName();
		}
		else if(args.length == 1)
		{
			hostname = args[0];
		}
		else
		{
			hostname = args[0];
			portNumber = Integer.parseInt(args[1]);
		}
		//get user info
		System.out.println("Enter a username:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String user_in = br.readLine();
		activeUser = new User(user_in, hostname);

		System.out.println("Logged in with the following details:\n" + activeUser.toString());
		
		try(
			Socket client = new Socket("mikhail-VirtualBox", portNumber);
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
			)//end try conditions
		{
			String userInput;
			while((userInput = consoleIn.readLine()) != null)
			{
				output.println(userInput);
				System.out.println("Echo: " + input.readLine());
			}//end while
		}//end try
		catch (UnknownHostException uhe)
		{
			System.out.println("error connecting to host");
			System.out.println(uhe);
		}
		catch (IOException ioe)
		{
			System.out.println("error with IO");
			System.out.println(ioe.toString());
			System.exit(1);
		}
		

	}//end main

	//Returns hostname
	public static String getHostName()
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

}//end class