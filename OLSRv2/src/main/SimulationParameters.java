/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: SimulationParameters.java
 * Author: Asi
 * Date: 10/01/2010
 *
 */
package main;


/**
 * @author Asi
 *
 */
public class SimulationParameters {

	///////////////////////////////////////////////////////////////////////////
	// Protocol Definitions
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * The validity period of the entry in the table 
	 */
	static public int entryValidPeriod = 1000;
	
	/**
	 * The Interval that the Hello messages should be generated
	 * periodically. 
	 */
	static public int TCInterval = 200; //TODO see that this is a correct interval
	
	/**
	 * The Interval that the Hello messages should be generated
	 * periodically. 
	 */
	static public int HelloInterval = 200; //TODO see that this is a correct interval
	
	/**
	 * The Delta of time after which the messages are being sent. 
	 */
	static public int transmitionTime = 5; //TODO see that this is a correct interval
	
	/**
	 * This defines whether the protocol chooses its
	 * MPRs regularly or all of the nodes are selected 
	 * as MPRs 
	 */
	public enum ProtocolMprMode{
		NORMAL, /* Regular MPR selection */ 
		ALL_MPRS /* All nodes are being chosen as MPRs*/
	}
	
	static public ProtocolMprMode protocolMode = ProtocolMprMode.NORMAL;
	
	
}
