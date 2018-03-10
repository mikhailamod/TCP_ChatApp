//thread for client

import java.net.*;
import java.io.*;

public class ClientThread extends Thread
{
	private Socket clientSocket = null;//each client has a socket, this is their associated socket
	private ChatAppClient client = null;//each ClientThread runs in ChatAppClient
	private ObjectInputStream input;

	public ClientThread(Socket socket, ChatAppClient client)
	{
		this.clientSocket = socket;
		this.client = client;
		try
		{
			input = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e)
		{
			System.out.println("Error in ClientThread con()\n" + e.getMessage());
		}//end catch
      start();//start this thread
	}//end con

	public void run()
	{
      System.out.println("Client Thread running");
		while(true)//TO DO: should not be while true, there should be some volatile boolean that changes when error is thrown or when user disconnects
		{
			try
			{
				Message m = (Message)input.readObject();//get message from input stream (this would be the output stream of ServerThread)
				client.recieve(m);
			}
			catch(ClassNotFoundException ie)
			{
				System.out.println("Error in ClientThread run(). Class");
            System.exit(1);//change sometime soon pls
			}
			catch (IOException e)
			{
				System.out.println("Error in ClientThread run(). IO");
            System.exit(1);
			}
		}
	}
}