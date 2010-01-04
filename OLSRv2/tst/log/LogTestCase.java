/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: LogTestCase.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package log;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;

import log.dataserver.SqlWriter;
import log.sqlproxy.SqlProxyException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.CsvWriter;
import data.IDataWriter;
import data.SimEvents;
import data.SimLabels;

/**
 * @author Assaf
 *
 */
public class LogTestCase {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link log.Log#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		Log log1 = Log.getInstance();
		Log log2 = Log.getInstance();
		
		if (log1 != log2 || !log1.equals(log2)){
			fail("Log is not a singleton");
		}
	}

	/**
	 * Test method for {@link log.Log#close()}.
	 */
	@Test
	public void testCsvLog() {
		testLog(new CsvWriter());
	}

	/**
	 * Test method for {@link log.Log#close()}.
	 * @throws SqlProxyException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testSqlLog() throws ClassNotFoundException, SQLException, SqlProxyException {
		testLog(new SqlWriter());
	}
	
	
	private void testLog(IDataWriter writer) {
		Log log = null;

		try {
			log = Log.getInstance();
			log.createLog(writer);
			
			HashMap<String, String> data = new HashMap<String, String>();
			data.put(SimLabels.VIRTUAL_TIME.name(), "1");
			data.put(SimLabels.NODE_ID.name(), "node-1");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.NODE_CREATE.name());
			data.put(SimLabels.X_COOR.name(), "1");
			data.put(SimLabels.Y_COOR.name(), "2");
			log.writeDown(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "2");
			data.put(SimLabels.NODE_ID.name(), "node-2");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.HELLO_SENT.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-2");
			data.put(SimLabels.GLOBAL_TARGET.name(), "All neighbors");
			log.writeDown(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "3");
			data.put(SimLabels.NODE_ID.name(), "node-1");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.HELLO_REACH.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-2");
			data.put(SimLabels.GLOBAL_TARGET.name(), "All neighbors");
			data.put(SimLabels.LOST.name(), "0");
			log.writeDown(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "4");
			data.put(SimLabels.NODE_ID.name(), "node-1");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.DATA_SENT.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_TARGET.name(), "All neighbors");
			data.put(SimLabels.GLOBAL_TARGET.name(), "node-3");
			data.put(SimLabels.LOST.name(), "0");
			log.writeDown(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "5");
			data.put(SimLabels.NODE_ID.name(), "node-2");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.DATA_REACH.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_TARGET.name(), "All neighbors");
			data.put(SimLabels.GLOBAL_TARGET.name(), "node-3");
			data.put(SimLabels.LOST.name(), "0");
			log.writeDown(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "6");
			data.put(SimLabels.NODE_ID.name(), "node-2");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.DATA_LOSS.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_SOURCE.name(), "node-2");
			data.put(SimLabels.LOCAL_TARGET.name(), "All neighbors");
			data.put(SimLabels.GLOBAL_TARGET.name(), "node-3");
			data.put(SimLabels.LOST.name(), "True");
			log.writeDown(data);
			log.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception thrown");
		}
	}

}
