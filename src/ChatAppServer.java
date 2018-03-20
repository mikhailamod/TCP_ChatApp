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
	public synchronized void addClient(Socket client)
	{
	    System.out.println("User connected");
	    activeClients.add(new ServerThread(this, client));
	    activeClients.get(activeClients.size()-1).start();//start thread
	}//end addClient
	
	public synchronized void updateUserList()
	{
	    ArrayList<String> userList = new ArrayList<>();
	    for(int i=0; i<activeClients.size();i++)
	    {
			//System.out.println("User is: " + activeClients.get(i).getUser());
			userList.add(activeClients.get(i).getUser().getUsername());
	    }
	    //System.out.println("DEBUG!!: " + userList.size());
	    sendUserList(userList);
	}

	public synchronized void removeClient(ServerThread client)
	{	
	    activeClients.remove(client);
	}
	
	public synchronized void sendUserList(ArrayList<String> userList)
	{
		Message m = new Message(userList);
		//System.out.println(m.getUserList().size()+ "DEBUG---------");
		for (int i = 0; i < activeClients.size(); i++)
		{
			activeClients.get(i).recieveMessage(m);
		}
	}

	//if server receives a message tagged as 'private', send to relevant user.
	public synchronized void privateMessage(int sentFromID, Message m)
	{
	    System.out.println("Private Message accepted");
	    int size = activeClients.size();
	    for (int i=0; i<size;i++ )
	    {
			if(activeClients.get(i).getID() != sentFromID)
			{
				String clientName = activeClients.get(i).getUser().getUsername();
				System.out.println("Sending to user: " + clientName);
				//System.out.println("DEBUIG CHECK NAME22: " + m.getUserTo());
				if(clientName.equals(m.getUserTo()))
				{
					activeClients.get(i).recieveMessage(m);
					break;//message only meant for one user, so stop
				}//end if
			}//end if		
	    }//end for
	}

	//send given message to all active clients
	public synchronized void broadcast(int sentFromID, Message m)
	{
		System.out.println("Debug CAS user exit: " + m.getData());
		int size = activeClients.size();
		for (int i=0; i<size;i++ )
		{
			if(activeClients.get(i).getID() != sentFromID)
			{
				activeClients.get(i).recieveMessage(m);
			}
		}
	}//end broadcast
	
	public synchronized void handleError(int sentFromID)
	{
		String data = "There was an error sending your message";
		Message m = new Message(data, activeClients.get(sentFromID).getUser());
		activeClients.get(sentFromID).recieveMessage(m);
	}

	public static void main(String[] args) throws IOException
	{
		ChatAppServer main_server = null;
		System.out.println("Starting Server on " + ":"+args[0]  );
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