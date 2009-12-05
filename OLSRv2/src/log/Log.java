/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: Log.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import data.DataWriter;
import data.SimLabels;

/**
 * @author Assaf
 *
 */
public class Log implements ILog {

	private static Log instance = null; 
	private DataWriter writer = null; 
	private boolean newLog = true;
	
	private static String currentTime(){
		
		SimpleDateFormat format = new SimpleDateFormat("_ddMMyyyy_HHmmss");
		Date now = new Date();
		return format.format(now);
	}
	
	private Log() {
	}
	
	/**
	 * @return Log object singleton
	 */
	public static synchronized Log getInstance(){
		
		if (null == instance){
			instance = new Log();
		}
		
		return instance;
	}
	
	/**
	 * @param dataWriter The data writer. 
	 * Different classes will change the way the log is implemented (e.g. XML or CSV)
	 *
	 */
	public synchronized void createLog(DataWriter dataWriter){
		//TODO Maybe add an XmlWriter as well
		//TODO Maybe add a security manager
		
		if (null != this.writer){
			
			/* 
			 * If log file already exists, then closing 
			 * the file before opening another 
			 */
			close();
		}
		
		String fileExtension = dataWriter.getExtension();
		if (null == fileExtension){
			fileExtension = defaultFileExtension;
		}
		
		File logFile = new File(ILog.basePath + ILog.fileName + currentTime() + "." + fileExtension);
		try {
			logFile.createNewFile();			
			dataWriter.close(); // Closes any files that may have been passed along
			this.writer = dataWriter;
			this.writer.openFile(logFile, null);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("\nError!! - Can't create new file. Log disabled");
			close();
			return;
		}
	}
	
	/**
	 * Closing the edges 
	 */
	public synchronized void close(){
		if (null != this.writer){
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer = null;
			newLog = true;
		}
	}

	/**
	 * @param data The data to be written, presented as a Map between labels and data
	 * @throws LogException
	 */
	public synchronized void writeDown(Map<String, String> data)
		throws LogException {
		
		if (null == writer){
			throw new LogException("Must create new Log before writing to it");
		}
		
		try {
			
			if (newLog){
				this.writer.writeLabels(SimLabels.stringify());
				this.newLog = false; // This will only happen once per log session
			}
			
			this.writer.writeData(data);
		} catch (IOException e) {
			throw new LogException("Error while trying to write data to log",e);
		}
	}
}
