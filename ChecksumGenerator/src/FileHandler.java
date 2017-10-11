import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHandler {
	private static final Logger log = Logger.getLogger( ChecksumGenerator.class.getName() );
	FileHandlerProperty props;
	/**
	 * Constructor
	 */
	public FileHandler(){
		props = new FileHandlerProperty();
	}
	/**
	 * Returns the selection of Selection parameters
	 */
	public String getFileLocation() {
		props = new FileHandlerProperty();
		return props.getFileProperty("FileLocation");
	}
	/**
	 * Return the timeout of the running thread
	 * @return
	 */
	public long getDBTimeout() {
		props = new FileHandlerProperty();
		return Integer.parseInt(props.getFileProperty("DBTimeout"));
	}
	/**
	 * Returns the the location of the sources folder
	 */
	public String getSourceFolderLocation() {
		props = new FileHandlerProperty();
		return props.getFileProperty("SystemSrc");
	}
	/**
	 * Returns the the location of the sources folder
	 */
	public String getGlobalSourceFolderLocation() {
		props = new FileHandlerProperty();
		return props.getFileProperty("GlobalSrc");
	}
	/**
	 * Returns the the location of the includes folder
	 */
	public String getIncludesFolderLocation() {
		props = new FileHandlerProperty();
		return props.getFileProperty("SystemIncl");
	}
	/**
	 * This method calculates the checksum of a given file according to a given algorithm.
	 * @param digest Algorithm specified in the property files
	 * @param file source code file
	 * @return Checksum of the source code file using algorithm
	 * @throws IOException If source code file cannot be read
	 */
	public static String getFileChecksum(String digest, File file) throws IOException
	{
		MessageDigest SHADigest = null;;
		try {
			SHADigest = MessageDigest.getInstance(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.getLocalizedMessage());
		}
		
		//Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);
		//Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0; 
		
		//Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			SHADigest.update(byteArray, 0, bytesCount);
		};
		
		//close the stream; We don't need it now.
		fis.close();
		
		//Get the hash's bytes
		byte[] bytes = SHADigest.digest();

		//This bytes[] has bytes in decimal format;
		//Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< bytes.length ;i++)
		{
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		//return complete hash
		return sb.toString();
	}
}
