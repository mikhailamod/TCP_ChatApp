/*
Adapted from https://dzone.com/articles/how-compress-and-uncompress
*/
//SNGPAV002

import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.util.List;  
import java.util.Map;  
import java.util.zip.DataFormatException;  
import java.util.zip.Deflater;  
import java.util.zip.Inflater;  

public class Compression {

	private byte[] file;

	public Compression(byte[] file) {
		this.file = file;
	}

	//given a file represented as a byte array, compress the file
	public byte[] compress() {  

		try {

			System.out.println("Starting Compression...");

			//initializes zip compression 
			Deflater deflater = new Deflater();  
		    deflater.setInput(this.file);  
		   	ByteArrayOutputStream ostream = new ByteArrayOutputStream(this.file.length);   
		    deflater.finish();  
		    byte[] buffer = new byte[this.file.length];   

		    //reads compressed bytes to an outputstream
		   	while (!deflater.finished()) {
		   		//stores index of next byte in buffer  
		    	int count = deflater.deflate(buffer);  
		    	ostream.write(buffer, 0, count);   
		   	}  

		    ostream.close();  
		    //convert stream to array and return it
		    byte[] output = ostream.toByteArray();  
		    System.out.println("Compression Successful!"); 
		    return output;  

		}
		catch (Exception e) {
			System.out.println(e);
			return null;
		}


	}  

	//given a compressed file, decompress it
	public byte[] decompress() throws IOException, DataFormatException {  

	   System.out.println("Starting decompression...");

	   //initializes decompresser
	   Inflater inflater = new Inflater();   
	   inflater.setInput(this.file);  
	   ByteArrayOutputStream ostream = new ByteArrayOutputStream(this.file.length);  
	   byte[] buffer = new byte[this.file.length];  

	   //decompresses file and stores in a buffer
	   while (!inflater.finished()) {  
	    int count = inflater.inflate(buffer);  
	    ostream.write(buffer, 0, count);  
	   }  

	   //returns in from stram to byte[]
	   ostream.close();  
	   byte[] output = ostream.toByteArray();  
	   System.out.println("Decompression Successful!"); 
	   return output;   

	} 

}