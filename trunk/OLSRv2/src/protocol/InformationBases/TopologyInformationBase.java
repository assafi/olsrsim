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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;



/**
 * @author Eli Nazarov
 *
 */
public class TopologyInformationBase {
	/**
	 * Maps the address of the station that transmits TC messages
	 * to sequence number and validity time
	 */
	private HashMap<String, TopologyCommonData> advertisingRemoteRouterSet = null;
	
	/**
	 * Maps the address of the station in the MANET
	 * to a list of all stations that could be reached from it 
	 * in 1-hop
	 */
	private HashMap<String, TopologySetData> topologySet = null;
	
	/**
	 * Maps the address of the destination station 
	 * to its next hop station on the route and number of hops
	 * (Length) of the rout until the destination station.
	 */
	private HashMap<String, RoutingSetData> routingSet = null;
	
	public TopologyInformationBase(){
		advertisingRemoteRouterSet = new HashMap<String, TopologyCommonData>();
		topologySet = new HashMap<String, TopologySetData>();
		routingSet = new HashMap<String, RoutingSetData>();
	}
	
	
	public void addTCTransmitingStation(String station, long TTL){
		TopologyCommonData data = new TopologyCommonData(TTL);
		advertisingRemoteRouterSet.put(station, data);
	}
	
	public boolean isStationTCTransmiting(String station){
		return advertisingRemoteRouterSet.containsKey(station);
	}
	
	/**
	 * @return the advertisingRemoteRouterSet
	 */
	public HashMap<String, TopologyCommonData> getAdvertisingRemoteRouterSet() {
		return advertisingRemoteRouterSet;
	}

	/**
	 * @return the topologySet
	 */
	public HashMap<String, TopologySetData> getTopologySet() {
		return topologySet;
	}

	/**
	 * @return the routingSet
	 */
	public HashMap<String, RoutingSetData> getRoutingSet() {
		return routingSet;
	}
	
	public void addTopologyEntry(String station, TopologySetData entryData){
		topologySet.put(station, entryData);
	}
	
	public void clearExpiredEntries(long currTime){
		ArrayList<String> keysToRemove = new ArrayList<String>();
		
		//clear Advertising Remote Router Set
		for (Entry<String, TopologyCommonData> entry : advertisingRemoteRouterSet.entrySet()) {
			if(entry.getValue().getTTL() < currTime){
				keysToRemove.add(entry.getKey());
			}
		}
		for (String key : keysToRemove) {
			advertisingRemoteRouterSet.remove(key);
		}
		keysToRemove.clear();
		
		//clear Advertising Remote Router Set
		for (Entry<String, TopologySetData> entry : topologySet.entrySet()) {
			if(entry.getValue().getTTL() < currTime){
				keysToRemove.add(entry.getKey());
			}
		}
		for (String key : keysToRemove) {
			topologySet.remove(key);
		}
		keysToRemove.clear();
	}
	
}
