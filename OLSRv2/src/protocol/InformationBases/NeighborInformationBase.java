/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: NeighborInformationBase.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package protocol.InformationBases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * This Class contains the Neighbor Information Base and Interface Information Base
 * as described in the RFC due to the fact that we simulate that each
 * station have only single interface and single data. 
 * 
 * @author Eli Nazarov
 *
 */
public class NeighborInformationBase {
	
	/**
	 * Maps the address of the 1-hop neighbor interface to its property
	 */
	private Map<String, NeighborProperty> neighborSet = null;
	
	/**
	 * Maps the address of the 1-hop neighbor address that was lost and time
	 * when this entry should be deleted.
	 */
	private Map<String, Long> lostNeighborSet = null;
	
	/**
	 * Maps the address of the 2-hop neighbor to the set of 1-hop neighbors 
	 * that it is reachable from.
	 */
	private Map<String, List<String>> secondHopNeighbors = null;
	
	public NeighborInformationBase(){
		lostNeighborSet = new HashMap<String, Long>();
		neighborSet = new HashMap<String, NeighborProperty>();
	}
	
	public boolean isNeighbor(String neighbor) {
		return neighborSet.containsKey(neighbor);
	}

	public boolean isLostNeighbor(String neighbor) {
		return lostNeighborSet.containsKey(neighbor);
	}
	
	public boolean is2HopNeighbor(String neighbor) {
		return secondHopNeighbors.containsKey(neighbor);
	}
	
	public void addNeighbor(String neighbor, NeighborProperty property) {
		neighborSet.put(neighbor, property);
	}

	public NeighborProperty getNeighborPrperty(String adrr) {
		return neighborSet.get(adrr);
	}

	public void addToLostNeighbors(String neighbor, long time) {
		lostNeighborSet.put(neighbor, time);
	}
	
	/**
	 * Removes all lost neighbors that their valid time is after 
	 * the time received.
	 * 
	 * @param time
	 * @return the lostNeighborSet
	 */
	public void removeInvalidLostNeighbors(int time) {
		Set<String> elements = lostNeighborSet.keySet();
		Iterator<String> it = elements.iterator();
		String key = it.next();
		while (it.hasNext()){
			long entryTime = lostNeighborSet.get(key);
			if (entryTime > time){ //TODO think how to compare times- made create an abstraction of time
				lostNeighborSet.remove(key);
			}
		}
	}
	/**
	 * @return the neighborSet
	 */
	public Map<String, NeighborProperty> getAllNeighbors() {
		return neighborSet;
	}

	/**
	 * @return the lostNeighborSet
	 */
	public Map<String, Long> getAllLostNeighborSet() {
		return lostNeighborSet;
	}
	
	public List<String> get2HopReachAddresses(String secondHopAddr){
		return secondHopNeighbors.get(secondHopAddr);
	}
	
	/**
	 * Adds 2-hop neighbor to the set.
	 * 
	 * This function can be used to add a 1-hop neighbor from 
	 * which the 2-hop neighbor can be reached
	 * 
	 * @param secondHopAddr
	 * @param firstHopReach
	 */
	public void add2HopNeighbor(String secondHopAddr, String firstHopReach){
		if (secondHopNeighbors.containsKey(secondHopAddr)){
			secondHopNeighbors.get(secondHopAddr).add(firstHopReach);
		}
		else {
			List<String> oneHops = new ArrayList<String>();
			oneHops.add(firstHopReach);
			secondHopNeighbors.put(secondHopAddr, oneHops);
		}
	}
	
	public void removeNeighbor(String neighbor){
		neighborSet.remove(neighbor);
	}
	
	public void removeLostNeighbor(String neighbor){
		lostNeighborSet.remove(neighbor);
	}
	public void remove2hoptNeighbor(String neighbor){
		secondHopNeighbors.remove(neighbor);
	}
}
