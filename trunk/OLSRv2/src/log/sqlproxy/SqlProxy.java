/**
 * 
 */
package log.sqlproxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * @author Assaf
 *
 */

public class SqlProxy {

	private Connection connection = null;
	/**
	 * 
	 */
	public SqlProxy() {
		super();
	}
	
	/**
	 * @param host
	 * @param port
	 * @param user
	 * @param password
	 * @param database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void connect(
			String host, int port, String user, 
			String password, String database) 
	throws ClassNotFoundException, SQLException {
		try {
			Class.forName(SqlProxyDefinitions.mysqlDriver);
			
			connection = DriverManager.getConnection(SqlProxyDefinitions.heading + 
					host +
					":" + port + 
					"/" + database + "?" +
					"user=" + user + "&" +
					"password=" + password
					);
			
		} catch (ClassNotFoundException cnfe) {
			System.err.println("[Error while loading MySQL driver]");
			throw(cnfe);
		} catch (SQLException sqle) {
			System.err.println("[Error while connecting to MySQL database]");
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("SQLState: " + sqle.getSQLState());
			System.err.println("VendorError: " + sqle.getErrorCode());
			throw(sqle);
		}
	}
	
	//TODO Probably need to add SSH public key (SSL) - Later iteration
	/**
	 * @param host
	 * @param port
	 */
	public void secureConnect(String host, int port){
		throw new NotImplementedException();
	}
	
	/**
	 * @param query
	 * @return
	 * @throws SqlProxyException
	 * @throws SQLException
	 */
	public PreparedStatement preparedStatement(String query) 
		throws SqlProxyException, SQLException {
		
		if (null == connection){
			throw new SqlProxyException("Must connect to SQL before retrieving a statement");
		}
		
		if (null == query){
			throw new SqlProxyException("Must specify an SQL query");
		}
		
		return connection.prepareStatement(query); 
	}

	/**
	 * 
	 */
	public void close() {
		try {
			if (null != connection){
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * closes all objects that are instances of Connection, PreparedStatement
	 * and ResultSet
	 * 
	 * @param objects
	 *            potential connections to close
	 */
	public static void closeAllSQLConnections(Object[] objects) {

		if (objects == null) {
			return;
		}

		for (Object object : objects) {
			try {

				// in case object is null it's not instance of any of the
				// following and nothing will be done for it

				if (object instanceof Connection) {
					Connection con = ((Connection) object);
					if (!con.isClosed()) {
						con.close();
					}
				} else if (object instanceof PreparedStatement) {
					PreparedStatement stmt = ((PreparedStatement) object);
					if (!stmt.isClosed()) {
						stmt.close();
					}

				} else if (object instanceof ResultSet) {
					ResultSet res = ((ResultSet) object);
					if (!res.isClosed()) {
						res.close();
					}
				}

			} catch (SQLException e) {
			}

		}
	}
}
