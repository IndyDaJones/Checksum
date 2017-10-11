import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @author j.nyffeler
 *
 */
public class Generate {
	private static final Logger log = Logger.getLogger( ChecksumGenerator.class.getName() );
	DBHandler db;
	String checksum;
	FileHandler file;
	public Generate(String Mchecksum){
		file = new FileHandler();
		checksum = Mchecksum;
	}
	/**
	 * The method starts the calcualtion
	 */
	public void start() {
		log.log(Level.INFO,"create DBHandler");
		db = new DBHandler();
		log.log(Level.INFO,"DBHandler created");
		log.log(Level.INFO,"Startup Checksum calcualtion");
		calculateChecksum();
	}
	
	/**
	 * In this method the actual checksum for the source file is calculated
	 */
	private void calculateChecksum() {
		log.log(Level.INFO,"Get all FilePaths");
		ResultSet  paths = db.GetPathsOfFiles();
		log.log(Level.INFO,"FilePaths gathered!");
		try {
			while((paths != null) && (paths.next())) {
				if(paths.getString(1) != null && paths.getString(4) != null) {
					String path = preparePath(paths.getString(2), paths.getString(3), paths.getString(4));
					log.log(Level.INFO,"FilePath is " + path);
					calculateChecksum(path, paths.getString(1));
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE,e.getLocalizedMessage());
			paths = null;
		}
	}
	
	/**
	 * This sequence completes the sequence number with leading 0s.
	 * @param SequenceNumber
	 * @return Sequence number with corresponding leading 0s.
	 */
	private String completeSequenceNumber(String SequenceNumber) {
		String sequence = "";
		switch (SequenceNumber.length()) {
        case 1:  sequence = "000"+ SequenceNumber;
                 break;
        case 2:  sequence = "00"+ SequenceNumber;
                 break;
        case 3:  sequence = "0"+ SequenceNumber;
                 break;
        case 4:  sequence = SequenceNumber;
                 break;
        default: sequence = "Invalid sequence number";
                 break;
		}
		return sequence;
	}
	
	/**
	 * In this method the checksum is being calculated and stored in the database
	 * @param path Path to the file containing the sourcode from which a checksum is needed
	 * @param ID Identifier of the database record where the checksum is stored after calculation
	 */
	private void calculateChecksum(String path, String ID) {
		try {
			File Source = new File(path);
			log.log(Level.INFO,"Source loaded " + path);
			String file_checksum = FileHandler.getFileChecksum(checksum, Source);
			log.log(Level.INFO,"Checksum calculated " + file_checksum + "for sequence with ID "+ ID);
			db.updatePath(ID, file_checksum);
			// Let Thread sleep for a while so that the DB can do it's job properly! (Access DB)
			Thread.sleep(file.getDBTimeout());
		}catch (IOException e) {
			db.updatePath(ID, e.getLocalizedMessage());
			log.log(Level.SEVERE,e.getLocalizedMessage());
		}catch (Exception e) {
			db.updatePath(ID, e.getLocalizedMessage());
			log.log(Level.SEVERE,e.getLocalizedMessage());
		}
	}
	
	/**
	 * This method generates the file path to the source file located on the local machine. (Usually a SVN- Pepository)
	 * @param System System where the sequence is active
	 * @param SequnceNumber sequence number
	 * @param SequenceName sequence name
	 * @return Absolute path the source file of the sequence 
	 */
	private String preparePath(String System, String SequnceNumber, String SequenceName) {
		String path = "";
		//FileHandler file = new FileHandler();
		if(System != null && SequnceNumber != null) {
			//It is a sequence
			path = file.getSourceFolderLocation() +"/"+ System+"/F10_Sources/"+completeSequenceNumber(SequnceNumber)+"_"+SequenceName+".JSEQ";
		}else if(System != null && SequnceNumber == null) {
			//It is a global sequence
			path = file.getGlobalSourceFolderLocation()+"/"+SequenceName+".JSEQ";
		}
		else {
			//It is a include
			path = file.getIncludesFolderLocation() +"/"+ SequenceName+".Jinc";
		}
		path = path.replace("\\","/");
		while (path.indexOf("/") != -1) {
    		String folder = path.substring(0, path.indexOf("/"));
    		
    		if (folder.equals(file.getSourceFolderLocation())||folder.equals(file.getIncludesFolderLocation())||folder.equals(file.getGlobalSourceFolderLocation())) {
    			log.log(Level.INFO,"FolderPath is " + folder);
    			break;
    		}
    		path = path.substring(path.indexOf("/")+1, path.length());
    		log.log(Level.INFO,"FilePath is " + path);
		} 
		path = "/"+path;
		path = file.getFileLocation()+path;
		log.log(Level.INFO,"FilePath is " + path);
		path = path.replace("/", "\\");
		return path;
	}
}
