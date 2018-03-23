//A thread to handle multiple clients
//AMDMIK002, ABRRIY002, SNGPAV002

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
	private ChatAppServer server;//ServerThread runs in ChatAppServer
	Socket client;
	private int id;
	volatile boolean running;
	ObjectOutputStream output;
	ObjectInputStream input;
	//Scanner kb = null;
	private User activeUser;//the user associated with this Socket

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
	public User getUser(){ return activeUser; }
	public ChatAppServer getServer(){ return server;}
	public int getID(){ return id; }
	
	//setters
	public void setUser(User user){ activeUser = new User(user); }

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
				System.out.println("Debug ST user exit: " + m.getData());
				ChatAppProtocol protocol = new ChatAppProtocol(this, m);
				protocol.parseMessage();
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