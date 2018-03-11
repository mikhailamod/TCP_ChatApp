//A thread to handle multiple clients
//Mikhail Amod - AMDMIK002

import java.net.*;
import java.io.*;
import java.io.File;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;

public class ServerThread extends Thread
{
	//Attributes
	ChatAppServer server;//ServerThread runs in ChatAppServer
	Socket client;
	int id;
	volatile boolean running;
	ObjectOutputStream output;
	ObjectInputStream input;
	Scanner kb = null;

	public ServerThread(ChatAppServer server, Socket client)
	{
		kb = new Scanner(System.in);
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
		System.out.println("ServerThread running " + id);
		while(running)//shouldnt be while true, should be a volatile boolean
		{
			try
			{
				Message m;
				m = (Message)input.readObject();//read incoming message and cast into Message

				//check tag of message. if it is "end" then close the socket
				if(m.getTag().equals("end"))
				{
					//inform all clients that this client is leaving
					String bye_text = m.getUser().getUsername() + " has left.";
					System.out.println(bye_text);
					m.setData(bye_text);
					server.broadcast(id, m);

					//close IO
					exit();
				}


				else
				{
					System.out.println(id + " recieved message:");//debug
					System.out.println(m.toString());//print message to server console
					server.broadcast(id, m);
				}
			} catch (IOException e) {
				System.out.println("Error in ServerThread run(), IO");
				System.out.println(e);
				exit();
			} catch (ClassNotFoundException ee)
			{
				System.out.println("Error in ServerThread run(), Class error");
				System.out.println(ee);
				exit();
			}//end catch
		}//end while
		
	}//end run

	public void exit()
	{
		try
		{
			output.close(); //close output stream
			client.close(); //close socket
			server.removeClient(this); //remove this thread from the arraylist
			running=false; //exit loop
		}
		catch(IOException ioe)
		{
			System.out.println("IO error in exit() ServerThread");
		}
	}//end exit
}