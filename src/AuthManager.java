//Manages authentication for ChatApp
//AMDMIK002
//bytesToString method from www.baeldung.com/sha-256-hashing-java

import java.io.*;
import java.security.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class AuthManager
{

	public static String DATABASE_PATH = "database.txt";//file that stores user data

	//given a password, hash the password and then return the hashed String
	public static String hashPassword(String input)
	{
		byte[] hashArr = hash(input);
		String out = bytesToString(hashArr);
		return out;
	}

	//given a hashedPassword and username,check if the combo exists in the database.
	public static boolean checkPassword(String username, String hashedPassword)
	{
		boolean equal = false;
		HashMap<String, String> hm = loadDatabase();
		if(exists(username))
		{
			String proper_hash = hm.get(username);
			if(proper_hash.equals(hashedPassword))
			{
				equal = true;
			}
			else
			{
				equal = false;
			}
		}
		return equal;
	}

	//given a username, check if it exists.
	public static boolean exists(String username)
	{
		HashMap<String, String> hm = loadDatabase();
		if(hm.get(username) == null){ return false; }
		else { return true; }
	}

	//load database textfile into a hashmap
	private static HashMap<String, String> loadDatabase()
	{
		HashMap<String, String> hm;
		try
		{
			hm = new HashMap<>();
			Scanner sc = new Scanner(new File(DATABASE_PATH));
			while(sc.hasNext())
			{
				String line = sc.nextLine();
				String[] lineArr = line.split("#");
				String username = lineArr[0];
				String hash = lineArr[1];
				hm.put(username, hash);//push username as key, hash as value
			}//end while
			return hm;
		}//end try
		catch (IOException e)
		{
			System.out.println("Error in AuthManager loadDatabase");
			return null;
		}
	}

	//take in a string and hash it using SHA256
	//return a byte array
	private static byte[] hash(String s)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(s.getBytes(StandardCharsets.UTF_8));
			return hash;
		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("Error in Auth");
			return null;
		}
		
	}//end hash

	//given a byte array containing a hash, convert to hex string
	//from www.baeldung.com/sha-256-hashing-java
	private static String bytesToString(byte[] hash)
	{
		StringBuffer sb = new StringBuffer();
		for (int i =0;i<hash.length;i++)
		{
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1)
			{
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}//end

	//given a username and hash, append to DATABASE file
	public static void writeToFile(String username, String hash)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_PATH, true));
			bw.write(username + "#" + hash);
			bw.newLine();
			bw.flush();
		}
		catch (IOException ioe)
		{
			System.out.println("Error in writeToFile() in AuthManager");
		}
	}//end
}