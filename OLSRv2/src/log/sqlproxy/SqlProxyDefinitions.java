package log.sqlproxy;

/**
 * MySQL driver definitions
 * @author Assaf
 *
 */
public interface SqlProxyDefinitions {
	
	/*
	 * Special heading for MySQL drivers
	 */
	public final static String heading = "jdbc:mysql://";
	public final static String mysqlDriver = "com.mysql.jdbc.Driver";
	public static String host = "localhost";
	public static int port = 3306;
	public final static String failoverhost = "localhost"; 
	public final static int failoverport = 3306;
	public static String database = "olsr";
	public static String user = "olsr";
	public static String password = "olsr";
	
}
