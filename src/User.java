//Class for user

import java.net.*;

public class User
{
	//Attributes
	private String username;
	private InetAddress ip;
	private String hostname;

	public User(String username, String hostname)
	{
		this.username = username;
		this.hostname = hostname;
		try{
			ip = InetAddress.getByName(hostname);
		}
		catch (UnknownHostException e)
		{
			System.out.println("Error");
		}
		
	}

	public String getIP()
	{
		return ip.getHostAddress();
	}

	public String getHostName()
	{
		return hostname;
	}
	public String getUsername()
	{
		return username;
	}

	public String toString()
	{
		String out = "Username: " + username + "\nHostname: " + hostname + "\nIP: " + getIP();
		return out;
	}
}