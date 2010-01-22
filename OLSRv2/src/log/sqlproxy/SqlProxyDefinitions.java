package log.sqlproxy;

public interface SqlProxyDefinitions {
	
	/*
	 * Special heading for MySQL drivers
	 */
	public final static String heading = "jdbc:mysql://";
	public final static String mysqlDriver = "com.mysql.jdbc.Driver";
	public final static String host = "localhost";
	public final static int port = 3306;
	public final static String failoverhost = "localhost"; //"assaf.homelinux.com";
	public final static int failoverport = 3306;
	public final static String database = "olsr";
	public final static String user = "root";
	
	//TODO Make sure the SQL server works with SSL !!!
	public final static String password = "root4sql";
	
}
