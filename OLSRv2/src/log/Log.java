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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import log.sqlproxy.SqlProxyException;

import data.IDataFileWriter;
import data.IDataSqlWriter;
import data.IDataWriter;
import data.SimLabels;

/**
 * @author Assaf
 *
 */
public class Log implements ILog {

	private static Log instance = null; 
	private IDataWriter writer = null; 
	private boolean newLog = true;
	
	private enum Mode { FILE, SQL } ;
	
	private Mode workingMode = Mode.FILE;
	
	private static String currentTime() {
		
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
	public synchronized void createLog(IDataWriter dataWriter){
		//TODO Maybe add an XmlWriter as well
		//TODO Maybe add a security manager
		
		if (null != this.writer){
			
			/* 
			 * If log file already exists, then closing 
			 * the file before opening another 
			 */
			close();
		}
		
		if (null == dataWriter) {
			System.err.println("Error!! - Can't create new file. Log disabled");
			return;
		}
		
		this.writer = dataWriter;
		if (dataWriter instanceof IDataFileWriter) {
			this.workingMode = Mode.FILE;
			createFileLog((IDataFileWriter)dataWriter);
			return;
		}
		
		if (dataWriter instanceof IDataSqlWriter) {
			this.workingMode = Mode.SQL;
			createSqlLog((IDataSqlWriter)dataWriter);
			return;
		}
		
		handleDWError(new LogException("Unknown DataWriter object"));
	}
	
	private void createFileLog(IDataFileWriter dataWriter) {
		String fileExtension = dataWriter.getExtension();
		if (null == fileExtension){
			fileExtension = defaultFileExtension;
		}
		
		File logFile = new File(ILog.basePath + ILog.fileName + currentTime() + "." + fileExtension);
		try {
			logFile.createNewFile();			
			dataWriter.openFile(logFile, null);
		} catch (Exception e) {
			handleDWError(e);
		}
	}
	
	private void createSqlLog(IDataSqlWriter dataWriter) {
		
		String tableName = ILog.fileName + currentTime();
		try {
			dataWriter.open(tableName);
		} catch (Exception e) {
			handleDWError(e);
		} 
	}
	
	private void handleDWError(Exception e) {
		e.printStackTrace();
		System.err.println("Error!! - Can't create new file. Log disabled");
		close();
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
