/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TopologyInformationBase.java
 * Author: Eli Nazarov
 * Date: Nov 18, 2009
 *
 */
package protocol.InformationBases;

import java.util.Map;
import java.util.Vector;

/**
 * @author Eli Nazarov
 *
 */
public class TopologyInformationBase {
	/**
	 * Maps the address of the station that transmits TC messages
	 * to sequence number and validity time
	 */
	private Map<String,/* TODO add seq number and vailidity time*/> advertisingRemoteRouterSet = null;
	
	/**
	 * Maps the address of the destination station 
	 * to its last hop station, sequence number and validity time
	 */
	//TODO learn more about this set
	private Map<String, /* TODO ... */> topologySet = null;
	
	/**
	 * Maps the address of the destination station 
	 * to its next hop station, sequence number and validity time
	 */
	//TODO learn more about this set
	private Map<String, /* TODO ... */> Routing Set = null;


	

}
