/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ProtocolDefinitions.java
 * Author: Eli Nazarov
 * Date: Nov 23, 2009
 *
 */
package protocol;

/**
 * @author Eli Nazarov
 *
 */
public class ProtocolDefinitions {
	/**
	 * The validity period of the entry in the table 
	 */
	static public int EntryValidPeriod = 10;
	
	/**
	 * The Interval that the Hello messages should be generated
	 * periodically. 
	 */
	static public int TCInterval = 100; //TODO see that this is a correct interval
	
	/**
	 * The Interval that the Hello messages should be generated
	 * periodically. 
	 */
	static public int HelloInterval = 100; //TODO see that this is a correct interval
}