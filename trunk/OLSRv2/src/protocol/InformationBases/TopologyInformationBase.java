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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eli Nazarov
 *
 */
public class TopologyInformationBase {
	/**
	 * Maps the address of the station that transmits TC messages
	 * to sequence number and validity time
	 */
	private Map<String, TopologyCommonData> advertisingRemoteRouterSet = null;
	
	/**
	 * Maps the address of the station in the MANET
	 * to its list of all stations that could be reached from it 
	 * in 1-hop
	 */
	private Map<String, TopologySetData> topologySet = null;
	
	/**
	 * Maps the address of the destination station 
	 * to its next hop station and number of hops.
	 */
	private Map<String, RoutingSetData> routingSet = null;
	
	public TopologyInformationBase(){
		advertisingRemoteRouterSet = new HashMap<String, TopologyCommonData>();
		topologySet = new HashMap<String, TopologySetData>();
		routingSet = new HashMap<String, RoutingSetData>();
	}
	
	/**
	 * @return the advertisingRemoteRouterSet
	 */
	public Map<String, TopologyCommonData> getAdvertisingRemoteRouterSet() {
		return advertisingRemoteRouterSet;
	}

	/**
	 * @return the topologySet
	 */
	public Map<String, TopologySetData> getTopologySet() {
		return topologySet;
	}

	/**
	 * @return the routingSet
	 */
	public Map<String, RoutingSetData> getRoutingSet() {
		return routingSet;
	}
	
	
	
}
