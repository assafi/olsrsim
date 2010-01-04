/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: IDataSqlWriter.java
 * Author: Assaf
 * Date: Jan 4, 2010
 *
 */
package data;

import java.sql.SQLException;

import log.sqlproxy.SqlProxyException;


/**
 * @author Assaf
 *
 */
public interface IDataSqlWriter extends IDataWriter {

	/**
	 * @param tableName The name of the table in which data will be written.
	 * @throws SQLException 
	 * @throws SqlProxyException 
	 */
	public void open(String tableName) throws SqlProxyException, SQLException;
}
