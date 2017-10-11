import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHandler {
	private static final Logger log = Logger.getLogger( ChecksumGenerator.class.getName() );
	DBConnection conn;
	DBHandlerProperty props;
	/**
	 * Constructor
	 */
	public DBHandler(){
		props = new DBHandlerProperty();
		log.log(Level.INFO,"call initDB");
		initDB();
		log.log(Level.INFO,"Database initiated");
	}
	/**
	 * This method creates a new database instance
	 */
	private void initDB(){
		log.log(Level.INFO,"Try to create DBConnection");
		this.conn = new DBConnection(props.getDBProperty("DatabaseLocation"));
	}
	/**
	 * Return the table name defined in the database.property files
	 * @return
	 */
	public String getSourceTableName() {
		return props.getDBProperty("SourceTable");
	}
	/**
	 * Returns the defined pathes to the source files
	 */
	public ResultSet GetPathsOfFiles() {
		//Statement s;
		int i = 0;
		ResultSet rs = null;
		try {
			Statement s = conn.createStatement();
		    log.log(Level.INFO,"Statement established");
		    // Fetch table
		    String selection = "SELECT ID, System, SequenceNumber, SequenceName FROM "+ getSourceTableName();
		    log.log(Level.INFO,"Query to execute: " + selection);
		    s.execute(selection);
		    rs = s.getResultSet();
		    s.close();
		    } catch (SQLException e) {
		    	conn.closeConnection();
		    	log.log(Level.SEVERE,"Exception catched: " +e.getLocalizedMessage());
		    }
		return rs;
	}
	/**
	 * In this method the result from the checksum calculation is stored in the database table 
	 * @param ID Idetifier from the record
	 * @param checksum calculated checksum
	 */
	public void updatePath(String ID, String checksum) {
	try {
		Statement s = conn.createStatement();
	    log.log(Level.INFO,"Statement established");
	    // Fetch table
	    String query = "UPDATE "+getSourceTableName()+" SET Checksum = '"+checksum+"' WHERE ID = "+ID;
	    log.log(Level.INFO,"Query to execute: " + query);
	    s.execute(query);
	    ResultSet rs = s.getResultSet();
	    s.close();
	    } catch (SQLException e) {
	    	conn.closeConnection();
	    	log.log(Level.SEVERE,"Exception catched: " +e.getLocalizedMessage());
	    }
	}
}
