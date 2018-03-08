//A thread to handle multiple clients
//Mikhail Amod - AMDMIK002

import java.net.*;
import java.io.*;

public class ServerThread extends Thread
{
	//Attributes
	Socket client;
	String id;

	public ServerThread(Socket client, String id)
	{
		this.client = client;
		this.id = id;
	}

	public void run()
	{
		try(
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);//send to clients output
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));//input from client
		)//end try condition
		{
			String inputLine;
			while((inputLine = input.readLine()) != null)//get input from client
			{
				System.out.print(id + " recieved message: ");
				System.out.println(inputLine);
				output.println(inputLine);//send to clients
			}
			client.close();
		} catch (IOException e) {
			System.out.println("ERROR");
			System.out.println(e);
		}
	}
}