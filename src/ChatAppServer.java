//A class that handles the server side of the chat app
//Currently only acts as an echo server

import java.util.*;
import java.net.*;
import java.io.*;

public class ChatAppServer
{
	public static final int PORT_NUMBER = 5050;
	public static ArrayList<ServerThread> activeClients;

	public static void main(String[] args) throws IOException
	{
		activeClients = new ArrayList<>();
		ServerSocket server = null;
		Socket client = null;
		System.out.println("Server started");
		try
		{
			server = new ServerSocket(PORT_NUMBER);
		} catch (IOException e) {
			System.out.println("ERROR");
			System.out.println(e);
		}
		while(true)
		{
			try
			{
				client = server.accept();
			}
			catch(IOException ioe)
			{
				System.out.println("ERROR: " + ioe);
			}
			ServerThread st = new ServerThread(client);
			activeClients.add(st);
			st.start();
		}
	}
}