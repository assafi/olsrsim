/**
 * 
 */
package log.dataserver;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Map;

import data.IDataSqlWriter;
import data.SimLabels;

import log.sqlproxy.SqlProxy;
import log.sqlproxy.SqlProxyDefinitions;
import log.sqlproxy.SqlProxyException;


/**
 * @author Assaf
 * 
 */
public class SqlWriter implements IDataSqlWriter {
	
	private Map<SimLabels,String> labelTypes = new Hashtable<SimLabels, String>();
	
	private SqlProxy proxy = null;

	/*
	 * Default definitions
	 */
	private static String host = SqlProxyDefinitions.host;
	private static int port = SqlProxyDefinitions.port;
	private static String database = SqlProxyDefinitions.database;
	private static String dbUsername = SqlProxyDefinitions.user;
	private static String dbPassword = SqlProxyDefinitions.password;
	private static String[] labels = null;
	private static int numLabels;
	
	/*
	 * The name of the table is defined by the creation date & time
	 * of the server proxy.
	 */
	private String tableName = "Default_table"; 
	private boolean tableExists = false;

	/**
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws SqlProxyException
	 */
	public SqlWriter() throws ClassNotFoundException, SQLException, SqlProxyException {
		this.proxy = new SqlProxy();
		this.proxy.connect(host, port, dbUsername, dbPassword, database);
		init();
		/*
		 * Added tasks for later iterations. when code reaches production level
		 */
		open(tableName);
	}

	private void init() {	
		labelTypes.put(SimLabels.INDX, "INTEGER PRIMARY KEY"); //AUTO_INCREMENT
		labelTypes.put(SimLabels.VIRTUAL_TIME, "INTEGER NOT NULL");
		labelTypes.put(SimLabels.NODE_ID, "VARCHAR(64)");
		labelTypes.put(SimLabels.EVENT_TYPE, "VARCHAR(32) NOT NULL");
		labelTypes.put(SimLabels.GLOBAL_SOURCE, "VARCHAR(64)");
		labelTypes.put(SimLabels.LOCAL_SOURCE, "VARCHAR(64)");
		labelTypes.put(SimLabels.LOCAL_TARGET, "VARCHAR(64)");
		labelTypes.put(SimLabels.GLOBAL_TARGET, "VARCHAR(64)");
		labelTypes.put(SimLabels.X_COOR, "INTEGER");
		labelTypes.put(SimLabels.Y_COOR, "INTEGER");
		labelTypes.put(SimLabels.RADIUS, "INTEGER");
		labelTypes.put(SimLabels.LOST, "BOOLEAN DEFAULT false");
		labelTypes.put(SimLabels.ERROR, "BOOLEAN DEFAULT false");
		labelTypes.put(SimLabels.DETAILS, "VARCHAR(4096)");
		
		labels = SimLabels.stringify();
		numLabels = labels.length;
	}
	
	@Override
	public void writeLabels(String[] labels) throws IOException {
		
		try {
			open(tableName);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private boolean tableExists(String table) throws SqlProxyException, SQLException {
		
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = this.proxy.preparedStatement("SELECT table_name FROM information_schema.tables " +
					"WHERE table_schema=? AND table_name=?");
			statement.setString(1, database);
			statement.setString(2, table);
			result = statement.executeQuery();
			
			/*
			 * If table exists, results are not empty.
			 */
			return result.first();
		} finally {
			SqlProxy.closeAllSQLConnections(new Object[] {statement,result});
		}
	}
	
	private void createTable(String table) throws SqlProxyException, SQLException {
		PreparedStatement createTableStmt = null;
		try {
			String query = "create table " + table + " (";
		
			for (SimLabels label : SimLabels.values()) {
				query += label.name() + " " + labelTypes.get(label);	
				if (label.ordinal() != SimLabels.values().length - 1) {
					query += ", ";
				}
			}
			query += ")";

			createTableStmt = this.proxy.preparedStatement(query);
			
			createTableStmt.executeUpdate();
		} finally {
			SqlProxy.closeAllSQLConnections(new Object[] { createTableStmt });
			this.tableExists = true;
		}
				
	}

	public void close() {
		proxy.close();
	}

	@Override
	public void open(String tableName) throws SqlProxyException, SQLException {
		/*
		 * Added tasks for later iterations. when code reaches production level
		 */
		if (!tableExists(tableName)){
			createTable(tableName);
		}
		
		this.tableName = tableName;
	}

	@Override
	public void writeData(Map<String, String> data) throws IOException {
		/*
		 * Checks that table exists
		 */
		try {
			if (!tableExists){
				createTable(tableName);
			}
			
			/*
			 * If no data - exit
			 */
			if (data.isEmpty()){
				return;
			}
			
			/*
			 * Inserts new entry in the table
			 */
			
			String query = "INSERT INTO " + tableName + " (";
			for (String label : labels) {
				if (label.equals(SimLabels.INDX.name())){
					continue;
				}
				query += label;
				if (!label.equals(labels[numLabels - 1])){
					query += ", ";
				}
			}
			query += ") VALUES(";
			query += qMarks(labelTypes.size() - 1);
			query += ")";
			
			PreparedStatement stmt = this.proxy.preparedStatement(query);
			
			for (SimLabels key : SimLabels.values()) {	
				switch (key) {
				case VIRTUAL_TIME: setInt(key, data, stmt, 1);
					break;
				case X_COOR: setInt(key, data, stmt, 1);
					break;
				case Y_COOR: setInt(key, data, stmt, 1);
					break;
				case RADIUS: setInt(key, data, stmt, 1);
					break;
				case LOST: setBoolean(key, data, stmt, 1);
					break;
				case ERROR: setBoolean(key, data, stmt, 1);
					break;
				case INDX: //Do nothing 
					break;
				default: setString(key, data, stmt, 1);
				}
			}
			
			stmt.execute();
		} catch (Exception e) {
			throw new IOException(e);
		} 
	}
	
	/**
	 * @param key
	 * @param data
	 * @param stmt
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private static void setInt(SimLabels key, Map<String, String> data,
			PreparedStatement stmt, int offset) throws NumberFormatException, SQLException {
	
		if (data.containsKey(key.name())) {
			stmt.setInt(key.ordinal() + offset, Integer.parseInt(data.get(key.name())));
		} else {
			stmt.setNull(key.ordinal() + offset, Types.INTEGER);
		}
	}
	
	/**
	 * @param key
	 * @param data
	 * @param stmt
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private static void setString(SimLabels key, Map<String, String> data,
			PreparedStatement stmt, int offset) throws NumberFormatException, SQLException {
	
		if (data.containsKey(key.name())) {
			stmt.setString(key.ordinal() + offset, data.get(key.name()));
		} else {
			stmt.setNull(key.ordinal() + offset, Types.VARCHAR);
		}
	}

	/**
	 * @param key
	 * @param data
	 * @param stmt
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private static void setBoolean(SimLabels key, Map<String, String> data,
			PreparedStatement stmt, int offset) throws NumberFormatException, SQLException {
	
		if (data.containsKey(key.name())) {
			stmt.setBoolean(key.ordinal() + offset, Boolean.parseBoolean(data.get(key.name())));
		} else {
			stmt.setNull(key.ordinal() + offset, Types.BOOLEAN);
		}
	}
	/**
	 * @param size
	 * @param j 
	 * @return
	 */
	private String qMarks(int size) {
		
		String str = "";
		for (int i = size; i > 1; i--) {
			str += "?, ";
		}
		if (size > 0) {
			str += "?";
		}
		
		return str;
	}
	
	/**
	 * Drops a given table from the SQL data base
	 * @param tableName The table to be dropped
	 * @throws SqlProxyException
	 * @throws SQLException
	 */
	public void dropTable(String tableName) throws SqlProxyException, SQLException 
	{
		if (!tableExists(tableName)){
			return;
		}
		
		String query = "DROP TABLE " + tableName;
		PreparedStatement statement = null;
		try {
			statement = this.proxy.preparedStatement(query);
			statement.executeUpdate();
		} finally {
			SqlProxy.closeAllSQLConnections(new Object[] {statement});
		}
	}

}
