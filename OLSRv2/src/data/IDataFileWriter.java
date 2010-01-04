/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: IDataFileWriter.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package data;

import java.io.File;
import java.io.IOException;

/**
 * @author Assaf
 * 
 */
public interface IDataFileWriter extends IDataWriter {
	
	/**
	 * @param file
	 * @param encoding
	 * @throws IOException
	 */
	public void openFile(File file, String encoding) throws IOException;
	
	/**
	 * @return The current file extension that matches the IDataFileWriter files format
	 */
	public String getExtension();
	
}
