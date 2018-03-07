//Client side of Chat App

import java.net.*;
import java.io.*;

public class ChatAppClient
{
	public static void main(String[] args) throws IOException {
		int portNumber = ChatAppServer.PORT_NUMBER;;
		String hostname = "";

		//Start up checks
		if(args.length == 0)//use hostname command to get hostname
		{
			System.out.println("Fetching hostname and using Port Number " + portNumber);
			try{

				Process p = Runtime.getRuntime().exec("hostname");//run the hostname command
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = null;

				while((line = input.readLine()) != null)
				{
					hostname = line;
					System.out.println("DEBUG: " + hostname);
				}//end while
			}//end try
			catch (IOException e)
			{
				System.out.println(e);
			}
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

		try(
			Socket client = new Socket(hostname, portNumber);
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
			System.out.println(ioe);
			System.exit(1);
		}

	}
}