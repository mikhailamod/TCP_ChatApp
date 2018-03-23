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
import java.util.zip.DataFormatException;  

//AMDMIK002, ABRRIY002, SNGPAV002
//Contans details of message that clients will send
public class Message implements Serializable
{
	//attributes
	private String tag; // tag of message
	private String data;//the actual message
	private User userFrom; //user that send the message
	private String userTo;//for private message, user that will recieve this message. Set to all for broadcast
	
	private byte[] file;// or file
	private String filepath; //File's path

	private ArrayList<String> userList;//a list of active clients in ChatAppServer
	
	//constructor used for most messages
	public Message(String data, User user)
	{
		this.data = data;
		userFrom = user;
		tag="message";
		userTo = "all";
	}

	//overloaded constructor used for files
	public Message(byte[] file, User user, String tag, String filepath)
	{
		this.userFrom = user;
		this.tag=tag;
		this.data = "";
		setFile(file);
		this.filepath = filepath;
	}
	
	//overloaded constructor used for sending a list of users
	public Message(ArrayList<String> userList)
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
	public ArrayList<String> getUserList(){ return userList; }

	//setters
	public void setTag(String tag){ this.tag = tag; }
	public void setData(String data){this.data = data; }
	public void setUserTo(String user){this.userTo = user; }
	public void setFile(byte[] file)
	{
		this.file = Arrays.copyOf(file, file.length);
	}
	
	//Outputs file to the received directory
	public void outputFile(String saveDest, String name, String ext) throws IOException, DataFormatException  {
        try {

        	//Start decompression
        	Compression comp = new Compression(this.file);
        	//get directory of output
            String dir = name.substring(0, name.lastIndexOf("/")) + "/received/";
            String fname = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));

            //output file to directory
            FileOutputStream fileoutputstream = new FileOutputStream(saveDest + fname + "" + ext);
            fileoutputstream.write(comp.decompress());
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