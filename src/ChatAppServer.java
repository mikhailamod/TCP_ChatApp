//A class that handles the server side of the chat app
//Currently only acts as an echo server

import java.util.*;
import java.net.*;
import java.io.*;

public class ChatAppServer implements Runnable
{
	public int port_number;
	public ArrayList<ServerThread> activeClients;//list of current active clients
	ServerSocket server;
	Thread serverThread = null;

	public ChatAppServer(int port_num) throws IOException
	{
		activeClients = new ArrayList<>();
		port_number = port_num;
		server = new ServerSocket(port_num);

		start();
	}

	//start threads
	public void start()
	{
		if(serverThread == null)
		{
			serverThread = new Thread(this);
			serverThread.start();
		}
	}

	//when thread is running, do this
	public void run()
	{
		while(serverThread != null)
		{
			try
			{
				System.out.println("Connecting to user");
				Socket client = server.accept();
				addClient(client);
			} catch (IOException e)
			{
				System.out.println("Error in run() method");
				System.out.println(e.getMessage());
			}
		}
	}//end run

	//when new client accepted, add them to list and start ServerThread for that client
	public void addClient(Socket client)
	{
		System.out.println("User connected");
		activeClients.add(new ServerThread(this, client));
		activeClients.get(activeClients.size()-1).start();//start thread
	}//end addClient

	public void removeClient(ServerThread client)
	{	
		activeClients.remove(client);
	}

	//send given message to all active clients
	public synchronized void broadcast(int sentFromID, Message m)
	{
		int size = activeClients.size();
		for (int i=0; i<size;i++ )
		{
			if(activeClients.get(i).id != sentFromID)
			{
				activeClients.get(i).recieveMessage(m);
			}
		}
	}

	public static void main(String[] args) throws IOException
	{
		ChatAppServer main_server = null;
		System.out.println("Starting Server");
		try
		{
			main_server = new ChatAppServer(Integer.parseInt(args[0]));
		}
		catch (IOException ie)
		{
			System.out.println("Error in ChatAppServer main()");
		}
		
	}
}