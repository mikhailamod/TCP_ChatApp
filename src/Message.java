//Contains details of message
import java.io.Serializable;
import java.util.Arrays;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Message implements Serializable
{
	//attributes
	private String tag; // tag of message
	private String data;//the actual message
	private byte[] file;// or file
	private String filepath; //File's path
	private User userFrom; //user that send the message
	private String userTo;//for private message, user that will recieve this message. Set to all for broadcast
	private ArrayList<User> userList;
	
	public Message(String data, User user)
	{
		this.data = data;
		userFrom = user;
		tag="message";
		userTo = "all";
	}

	public Message(byte[] file, User user, String tag, String filepath)
	{
		setFile(file);
		userFrom = user;
		this.tag=tag;
		this.filepath = filepath;
	}

	public Message(String data)
	{
		this.data = data;
	}
	
	public Message(ArrayList<User> userList)
	{
		data = "";
		tag = "userList";
		this.userList = new ArrayList<>(userList);
	}

	//getters
	public byte[] getFile() { return file; }
	public String getFilepath() { return filepath; }
	public String getData(){ return data; }
	public User getUser(){ return userFrom; }
	public String getTag(){ return tag; }
	public String getUserTo(){ return userTo; }
	public ArrayList<User> getUserList(){ return userList; }

	//setters
	public void setTag(String tag){ this.tag = tag; }
	public void setData(String data){this.data = data; }
	public void setUserTo(String user){this.userTo = user; }
	public void setFile(byte[] file)
	{
		this.file = Arrays.copyOf(file, file.length);
	}
	
	//Outputs file to the received directory
	public void outputFile(String saveDest, String name, String ext) {
        try {
        	//get directory of output
            String dir = name.substring(0, name.lastIndexOf("/")) + "/received/";
            String fname = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));

            //output file to directory
            FileOutputStream fileoutputstream = new FileOutputStream(saveDest + fname + "" + ext);
            fileoutputstream.write(this.file);
            fileoutputstream.close();
            //optional command to open up video
            //desktop.open(new File(dir + "" + fname + "" + ext));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	public String toString()
	{
		return data;
	}
}