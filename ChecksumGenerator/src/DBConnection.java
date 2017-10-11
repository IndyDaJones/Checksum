import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;



public class DBConnection {
	
	private static final Logger log = Logger.getLogger( ChecksumGenerator.class.getName() );
	Connection conn;
	public DBConnection(String database){
		try{
			conn=createConnection(database);
	    }catch(Exception ex){
	    	log.log(Level.SEVERE,"Connection exception catched" +ex.getLocalizedMessage());
	    }
	}
	/**
	 * Creates a new connection
	 * @return Connection
	 */
	Connection createConnection(String Database){
		try {
			log.log(Level.INFO,"Try tp create connection to <"+Database+">");
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			conn=DriverManager.getConnection("jdbc:ucanaccess://"+Database);
			log.log(Level.INFO,"Connection created!");
			return conn;
	    } catch(Exception ex) {
	    	log.log(Level.SEVERE,"Connection exception catched" +ex.getLocalizedMessage());
	    	return null;
	    }
	}
	/**
	 * Closes a connection
	 * @return Connection
	 */
	void closeConnection(){
		if (conn!= null) {
			try {
				conn.close(); 
			}
			catch (SQLException e) {
				log.log(Level.SEVERE,"Exception" +e);
			}
		}
	}
	/**
	 * Creates a statement
	 * @return Statement
	 */

	Statement createStatement() throws SQLException { 
		return conn.createStatement();
	}
}