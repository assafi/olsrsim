/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: CsvWriterTestCase.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Assaf
 * 
 */
public class CsvWriterTestCase {

	private static File playFile = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		playFile = new File("logs/playFile.csv");
		
		if (playFile.exists()){
			playFile.delete();
		}
		
		playFile.createNewFile();
		playFile.setWritable(true);
		playFile.setReadable(true);
		playFile.setExecutable(true);
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
	 * Test method for
	 * {@link data.CsvWriter#CsvWriter(java.io.File, java.lang.String)}.
	 */
	@Test
	public void testCsvWriter() {

		CsvWriter writer = null;

		try {
			writer = new CsvWriter(playFile, null);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception thrown");
		}

		if (null == writer) {
			fail("Writer is not set");
		}
	}

	/**
	 * Test method for {@link data.CsvWriter#writeLabels(java.lang.String[])}.
	 */
	@Test
	public void testWriteLabels() {
		CsvWriter writer = null;

		try {
			writer = new CsvWriter(playFile, null);
			writer.writeLabels(SimLabels.stringify());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception thrown");
		}
	}

	/**
	 * Test method for {@link data.CsvWriter#writeData(java.util.Map)}.
	 */
	@Test
	public void testWriteData() {
		CsvWriter writer = null;

		try {
			writer = new CsvWriter(playFile, null);
			writer.writeLabels(SimLabels.stringify());
			
			HashMap<String, String> data = new HashMap<String, String>();
			data.put(SimLabels.VIRTUAL_TIME.name(), "1");
			data.put(SimLabels.NODE_ID.name(), "node-1");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.NODE_CREATE.name());
			data.put(SimLabels.X_COOR.name(), "1");
			data.put(SimLabels.Y_COOR.name(), "2");
			writer.writeData(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "2");
			data.put(SimLabels.NODE_ID.name(), "node-2");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.HELLO_SENT.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-2");
			data.put(SimLabels.GLOBAL_TARGET.name(), "All neighbors");
			writer.writeData(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "3");
			data.put(SimLabels.NODE_ID.name(), "node-1");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.HELLO_REACH.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-2");
			data.put(SimLabels.GLOBAL_TARGET.name(), "All neighbors");
			data.put(SimLabels.LOST.name(), "false");
			writer.writeData(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "4");
			data.put(SimLabels.NODE_ID.name(), "node-1");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.DATA_SENT_FROM_SOURCE.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_TARGET.name(), "All neighbors");
			data.put(SimLabels.GLOBAL_TARGET.name(), "node-3");
			data.put(SimLabels.LOST.name(), "false");
			writer.writeData(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "5");
			data.put(SimLabels.NODE_ID.name(), "node-2");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.DATA_REACHED_2_TARGET.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_TARGET.name(), "All neighbors");
			data.put(SimLabels.GLOBAL_TARGET.name(), "node-3");
			data.put(SimLabels.LOST.name(), "false");
			writer.writeData(data);
			
			data.clear();
			data.put(SimLabels.VIRTUAL_TIME.name(), "6");
			data.put(SimLabels.NODE_ID.name(), "node-2");
			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.DATA_LOSS.name());
			data.put(SimLabels.GLOBAL_SOURCE.name(), "node-1");
			data.put(SimLabels.LOCAL_SOURCE.name(), "node-2");
			data.put(SimLabels.LOCAL_TARGET.name(), "All neighbors");
			data.put(SimLabels.GLOBAL_TARGET.name(), "node-3");
			data.put(SimLabels.LOST.name(), "true");
			writer.writeData(data);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception thrown");
		}
	}

}
