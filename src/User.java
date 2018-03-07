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
		ip = InetAddress.getByName(hostname);
	}

	public String getIP()
	{
		return ip.getHostAddress();
	}

	public String getHostName()
	{
		return hostname;
	}
	public getUsername()
	{
		return username;
	}
}