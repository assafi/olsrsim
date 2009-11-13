/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ILog.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package log;

/**
 * @author Assaf
 * This is a default interface, used only by the Log class.
 */
interface ILog {
	
	/*
	 * The log file naming convention is <fileName>_<date>.<fileExtension>
	 */
	public static final String fileName = "sim";
	public static final String defaultFileExtension = "log";
	
	public static final String basePath = "logs/";
}
