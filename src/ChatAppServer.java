//A class that handles the server side of the chat app
//Currently only acts as an echo server

import java.util.*;
import java.net.*;
import java.io.*;

public class ChatAppServer
{
	public static final int PORT_NUMBER = 5050;

	public static void main(String[] args) throws IOException
	{
		System.out.println("Server started");
		try(ServerSocket server = new ServerSocket(PORT_NUMBER);
			Socket client = server.accept();
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		)//end try condition
		{
			String inputLine;
			while((inputLine = input.readLine()) != null)//get input from client
			{
				System.out.print("recieved message: ");
				System.out.println(inputLine);
				output.println(inputLine);
			}
		} catch (IOException e) {
			System.out.println("ERROR");
			System.out.println(e);
		}
	}
}