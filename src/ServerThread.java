//A thread to handle multiple clients
//Mikhail Amod - AMDMIK002

import java.net.*;
import java.io.*;

public class ServerThread extends Thread
{
	//Attributes
	Socket client;
	int id;

	public ServerThread(Socket client, int id)
	{
		this.client = client;
		this.id = id;
	}

	public void run()
	{
		try(
			//io streams for message objects
			ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());//send to clients output
			ObjectInputStream input = new ObjectInputStream(client.getInputStream());//input from client
		)//end try condition
		{
			Message inputLine;
			while((inputLine = (Message)input.readObject()) != null)//get input from client
			{
				System.out.println(id + " recieved message:");
				System.out.println(inputLine.toString());
				output.writeObject(inputLine);//send to clients
			}
			client.close();
		} catch (IOException e) {
			System.out.println("ERROR");
			System.out.println(e);
		} catch (ClassNotFoundException ee)
		{
			System.out.println("Class error");
			System.out.println(ee);
		}
	}
}