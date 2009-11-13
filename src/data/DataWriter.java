/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: DataWriter.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package data;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Assaf
 * 
 */
public interface DataWriter {
	
	/**
	 * @param file
	 * @param encoding
	 * @throws IOException
	 */
	public void openFile(File file, String encoding) throws IOException;
	/**
	 * @param labels The different data labels 
	 * @throws IOException 
	 */
	public void writeLabels(String[] labels) throws IOException;
	
	/**
	 * Writing the data to a file. the data will be written to the appropriate 
	 * label
	 * @param data Map between labels and data
	 * @throws IOException 
	 */
	public void writeData(Map<String, String> data) throws IOException;
	
	/**
	 * @return The current file extension that matches the DataWriter files format
	 */
	public String getExtension();
	
	/**
	 * Closes the file stream
	 * @throws IOException 
	 */
	public void close() throws IOException;
}
