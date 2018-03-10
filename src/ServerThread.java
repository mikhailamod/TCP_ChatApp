//A thread to handle multiple clients
//Mikhail Amod - AMDMIK002

import java.net.*;
import java.io.*;

public class ServerThread extends Thread
{
	//Attributes
	ChatAppServer server;//ServerThread runs in ChatAppServer
	Socket client;
	int id;
	volatile boolean running;
	ObjectOutputStream output;
	ObjectInputStream input;

	public ServerThread(ChatAppServer server, Socket client)
	{
		this.running=true;
		this.client = client;
		this.server = server;
		id = client.getPort();//the port of each socket uniquely identifies them
		try
		{
			output = new ObjectOutputStream(client.getOutputStream());//send to clients output
			input = new ObjectInputStream(client.getInputStream());//input from client
		} catch (IOException e)
		{
			System.out.println("Error in ServerThread con(), IO");
		}
		
	}

	//server thread recieves a message and sends it to an output stream
	public void recieveMessage(Message m)
	{
		try
		{
			output.writeObject(m);
		} catch(IOException c)
		{
			System.out.println("Error recieveMessage() in ServerThread, ClassNot ");
		}
	}

	public void run()
	{
		System.out.println("ServerThread runnin " + id);
		while(running)//shouldnt be while true, should be a volatile boolean
		{
			try
			{
				Message incomingMessage;
				incomingMessage = (Message)input.readObject();//read incoming message and cast into Message

				//check tag of message. if it is "end" then close the socket
				if(incomingMessage.getTag().equals("end"))
				{
					System.out.println(incomingMessage.getUser().getUsername() + " left");
					output.close();
					client.close();
					running=false;
				}
				else
				{
					System.out.println(id + " recieved message:");//debug
					System.out.println(incomingMessage.toString());//print message to server console
				}
					
				//send input to server, which will send it to all clients
				server.broadcast(id, incomingMessage);
			} catch (IOException e) {
				System.out.println("Error in ServerThread run(), IO");
				System.out.println(e);
				System.exit(1);
			} catch (ClassNotFoundException ee)
			{
				System.out.println("Error in ServerThread run(), Class error");
				System.out.println(ee);
				System.exit(1);
			}//end catch
		}
		
	}
}