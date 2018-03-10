//Contains details of message
import java.io.Serializable;

public class Message implements Serializable
{
	//attributes
	private String data;//the actual message
	private User userFrom; //user that send the message

	public Message(String data, User user)
	{
		this.data = data;
		userFrom = user;
	}

	public String getData(){ return data; }
	public User getUser(){ return userFrom; }

	public String toString()
	{
		String s = userFrom.getUsername() + " says: " + data;
		return s;
	}
}