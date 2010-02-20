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
	static public int TCInterval = 10; //TODO see that this is a correct interval
	
	/**
	 * The Interval that the Hello messages should be generated
	 * periodically. 
	 */
	static public int HelloInterval = 10; //TODO see that this is a correct interval
	
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
	
	/**
	 * This defines whether the protocol sends
	 * data messages only through MPRs
	 * or through all neighbors
	 */
	public enum ProtocolDataSendMode{
		NORMAL, /* through all neighbors */ 
		MPRS /* only through MPRs */
	}
	
	static public ProtocolDataSendMode protocolDataSendMode = ProtocolDataSendMode.NORMAL;
	
	///////////////////////////////////////////////////////////////////////////
	// Layout Definitions
	///////////////////////////////////////////////////////////////////////////
	
	public enum LayoutMode{
		UNIFORM, /* Nodes will be created in a uniform fashion in the simulation grid */
		CLUSTER  /* Nodes will be created in a cluster fashion in the simulation grid */
	}
	
	static public LayoutMode layoutMode = LayoutMode.UNIFORM;
	
	static public int clusterRadius = 100; /* This value is needed in Cluster mode */
	
	static public int clusterNum = 10;
	
	public enum StationsMode{
		/*
		 * Stations are static in space
		 */
		STATIC, 
		
		/*
		 * Stations are constantly moving in space
		 */
		DYNAMIC,
		
		/*
		 * Stations will decide to move every once in a while (in a non-deterministic fashion).
		 * stations can also be destroyed and created, beside from moving.  
		 */
		MIXED
	}
	
	static public StationsMode stationsMode = StationsMode.STATIC;
	
	static public int xBoundry = 500;
	
	static public int yBoundry = 500;
	
	///////////////////////////////////////////////////////////////////////////
	// Simulation Definitions
	///////////////////////////////////////////////////////////////////////////
	
	static public int receptionRadius = 30;
	
	static public int simulationEndTime = 2500;
	
	static public float topologyPoissonicRate = (float) 0.011; /* This topologyPoissonicRate is taken into account when creating
												topology event. */
	
	static public float dataEventsPoissonicRate = (float) 0.2; /* This dataEventsPoissonicRate is taken into account when creating
												Send data events. */
	
	static public int maxStations = 100;
		
	public enum StationsSpeed {
		LOW,
		MEDIUM,
		HIGH
	}
	
	static public StationsSpeed stationSpeed = StationsSpeed.LOW;
	
	/*
	 * the actual speed of nodes movement is determined 
	 * by calculating simulationSpeed.ordinal() * simulationHopDistance 
	 * as the distance a node will travel for each hop every 10 simulation time units.
	 */
	static public int stationHopDistance = 5;
}
