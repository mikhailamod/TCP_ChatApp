//Contains details of message
import java.io.Serializable;

public class Message implements Serializable
{
	//attributes
	private String tag; // tag of message
	private String data;//the actual message
	private User userFrom; //user that send the message

	public Message(String data, User user)
	{
		this.data = data;
		userFrom = user;
		tag="message";
	}

	public Message(String data)
	{
		this.data = data;
	}

	public String getData(){ return data; }
	public User getUser(){ return userFrom; }
	public String getTag(){ return tag; }
	public void setTag(String tag){ this.tag = tag; }
	public void setData(String data){this.data = data; }

	public String toString()
	{
		return data;
	}
}