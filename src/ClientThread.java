//thread for client
//AMDMIK002, ABRRIY002, SNGPAV002

import java.net.*;
import java.io.*;

public class ClientThread extends Thread
{
	private Socket clientSocket = null;//each client has a socket, this is their associated socket
	private ChatAppClient client = null;//each ClientThread runs in ChatAppClient
	private ObjectInputStream input;//stream to accept Message objects
	private volatile boolean running;//is the clientThread currently running


	public ClientThread(Socket socket, ChatAppClient client)
	{
		this.running = true;
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
		while(running)
		{
			try
			{
				Message m = (Message)input.readObject();//get message from input stream (this would be the output stream of ServerThread)
				client.receive(m);//send message object to ChatAppClient(which will then parse it to the protocol).
			}
			catch(ClassNotFoundException ie)
			{
				System.out.println("Error in ClientThread run(). Class");
				exit();
			}
			catch (IOException e)
			{
				System.out.println("---");
				exit();
			}//edn catch
		}//end while
	}//end run

	public void exit()
	{
		try
		{
			input.close(); //close output stream
			clientSocket.close(); //close socket
			running=false; //exit loop
		}
		catch(IOException ioe)
		{
			System.out.println("IO error in exit() ClientThread");
		}
	}//end exit
}