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
	//Scanner kb = null;
	User activeUser;//the user associated with this Socket

	public ServerThread(ChatAppServer server, Socket client)
	{
		//kb = new Scanner(System.in);
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
	
	//getters
	public User getUser()
	{
		return activeUser;
	}

	//server thread recieves a message and sends it to an output stream
	public synchronized void recieveMessage(Message m)
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
				
				//Below represents our implementation of our protocol on the server side.
				//check tag of message. if it is "end" then close the socket
				if(m.getTag().equals("end"))
				{
					//inform all clients that this client is leaving
					String bye_text = m.getUser().getUsername() + " has left.\n";
					System.out.println(bye_text);
					m.setData(bye_text);
					server.broadcast(id, m);
					//close IO
					exit();
				}

				//this message will contain no data, only a User object which we will assign to activeUser
				else if(m.getTag().equals("userTransfer"))
				{
					activeUser = new User(m.getUser().getUsername(), m.getUser().getHostName());
					server.updateUserList();
				}

				//contains details of private message
				else if(m.getTag().equals("private"))
				{
					System.out.print(id + " recieved private message:");//debug
					System.out.println(m.toString());//print message to server console
					server.privateMessage(id, m);
				}
				
				//TODO - change to else if for broadcast and file. last else must catch corrupted message.
				else if(m.getTag().equals("broadcast") || m.getTag().equals("image") || m.getTag().equals("video"))
				{
					System.out.println(id + " recieved broadcast message:");//debug
					System.out.println(m.toString());//print message to server console
					server.broadcast(id, m);
				}
				//else this message is somehow corrupt, send error
				else
				{
					System.out.println(id + " SERIOUS ERROR. Message tag corrupted");//debug
					server.handleError(id);
				}//end else
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

	public synchronized void exit()
	{
		try
		{
			
			output.close(); //close output stream
			client.close(); //close socket
			server.removeClient(this); //remove this thread from the arraylist
			server.updateUserList();
			running=false; //exit loop
		}
		catch(IOException ioe)
		{
			System.out.println("IO error in exit() ServerThread");
		}
	}//end exit
}