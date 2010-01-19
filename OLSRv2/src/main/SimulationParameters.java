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
	
	static public ProtocolMprMode mode = ProtocolMprMode.NORMAL;
	
	///////////////////////////////////////////////////////////////////////////
	// Layout Definitions
	///////////////////////////////////////////////////////////////////////////
	
	public enum LayoutMode{
		UNIFORM, /* Nodes will be created in a uniform fashion in the simulation grid */
		CLUSTER  /* Nodes will be created in a cluster fashion in the simulation grid */
	}
	
	static public LayoutMode layoutMode = LayoutMode.UNIFORM;
	
	static public int clusterRadius = 100; /* This value is needed in Cluster mode */
	
	static public boolean staticMode = false; /* Should the station be static or movable */
	
	static public int xBoundry = 500;
	
	static public int yBoundry = 500;
	
	///////////////////////////////////////////////////////////////////////////
	// Simulation Definitions
	///////////////////////////////////////////////////////////////////////////
	
	static public int receptionRadius = 30;
	
	static public int simulationEndTime = 2500;
	
	static public float factor = (float) 0.011; /* This factor is taken into account when creating
												topology event. should not be larger then 0.015 */
	
	static public int maxStations = 100;
	
	static public int clusterNum = 10;
	
	
	
}
