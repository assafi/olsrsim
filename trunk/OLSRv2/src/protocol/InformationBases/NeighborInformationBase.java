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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import log.Log;
import log.LogException;
import data.SimEvents;
import data.SimLabels;
import dispatch.Dispatcher;

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
		secondHopNeighbors = new HashMap<String, List<String>>();
	}
	
	public boolean is1HopNeighbor(String neighbor) {
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

	public NeighborProperty getNeighborProperty(String adrr) {
		return neighborSet.get(adrr);
	}

	public void addToLostNeighbors(String neighbor, long time) {
		lostNeighborSet.put(neighbor, time);
	}
	
//	/**
//	 * Removes all lost neighbors that their valid time is after 
//	 * the time received.
//	 * 
//	 * @param time
//	 * @return the lostNeighborSet
//	 */
//	public void removeInvalidLostNeighbors(int time) {
//		Set<String> elements = lostNeighborSet.keySet();
//		Iterator<String> it = elements.iterator();
//		String key = it.next();
//		while (it.hasNext()){
//			long entryTime = lostNeighborSet.get(key);
//			if (entryTime > time){ //TODO think how to compare times- made create an abstraction of time
//				lostNeighborSet.remove(key);
//			}
//		}
//	}
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
	
	public List<String> get2hopNeighbors(){
		Set<String> secondHopNeighborsNameSet = secondHopNeighbors.keySet();
		List<String> secondHopNeighborsNameList = new ArrayList<String>();
		
		for (String name : secondHopNeighborsNameSet) {
			secondHopNeighborsNameList.add(name);
		}
		
		return secondHopNeighborsNameList;
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
	
	public void clearExpiredEntries(long currTime){
		ArrayList<String> keysToRemove = new ArrayList<String>();
		
		//clear Neighbor Set
		for (Entry<String, NeighborProperty> entry : neighborSet.entrySet()) {
			if(entry.getValue().getValideTime() < currTime){
				keysToRemove.add(entry.getKey());
			}
		}
		for (String key : keysToRemove) {
			neighborSet.remove(key);
		}
		keysToRemove.clear();
		
		//clear Lost Neighbor Set
		for (Entry<String, Long> entry : lostNeighborSet.entrySet()) {
			if(entry.getValue() < currTime){
				keysToRemove.add(entry.getKey());
			}
		}
		for (String key : keysToRemove) {
			neighborSet.remove(key);
		}
		keysToRemove.clear();

		
		/*
		 * clear 2 Hop Neighbors Set
		 * if all 1-hop neighbors that some 2-hop neighbor
		 * is reachable via them dosn't exist in the neighborSet or are 
		 * reported as lost, we'll remove the 2-hop neighbor from our set.
		 */
		
		for (Entry<String,List<String>> entry : secondHopNeighbors.entrySet()) {
			boolean valid = false;
			for (String viaNode : entry.getValue()) {
				if(neighborSet.containsKey(viaNode)){
					valid = true;
				}
			}
			if(!valid){
				keysToRemove.add(entry.getKey());
			}
		}
		for (String key : keysToRemove) {
			neighborSet.remove(key);
		}
		keysToRemove.clear();
		
	}
	
	private void log(String stationID, String dataToLog) {
		Log log = Log.getInstance();
//		String collNames = new String(tableName);
//		
//		// put all names in one string
//		for (String name : logData) {
//			collNames += " " + name + ",";
//		}
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(SimLabels.NODE_ID.name(), stationID);
		data.put(SimLabels.EVENT_TYPE.name(), SimEvents.LOG.name());
		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
		data.put(SimLabels.DETAILS.name(), dataToLog);
		try {
			log.writeDown(data);
		} catch (LogException le) {
			System.out.println(le.getMessage());
		}
	}
	
	private String logMPRs(Collection<String> logData) {
		Log log = Log.getInstance();
		/* log MPRs */
		String collNames = new String("MPRs:");
		
		// put all names in one string
		for (String name : logData) {
			if (neighborSet.get(name).isMpr()){
				collNames += " " + name + ",";
			}
		}
		
//		HashMap<String, String> data = new HashMap<String, String>();
//		data.put(SimLabels.NODE_ID.name(), stationID);
//		data.put(SimLabels.EVENT_TYPE.name(), SimEvents.LOG.name());
//		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
//		data.put(SimLabels.DETAILS.name(), collNames);
//		try {
//			log.writeDown(data);
//		} catch (LogException le) {
//			System.out.println(le.getMessage());
//		}
		
		/* log mpr_selectors */
		
		collNames += " mpr_selectors:";
		
		// put all names in one string
		for (String name : logData) {
			if (neighborSet.get(name).isMpr_selector()){
				collNames += " " + name + ",";
			}
		}
		
		return collNames;
		
//		data = new HashMap<String, String>();
//		data.put(SimLabels.NODE_ID.name(), stationID);
//		data.put(SimLabels.EVENT_TYPE.name(), SimEvents.LOG.name());
//		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
//		data.put(SimLabels.DETAILS.name(), collNames);
//		try {
//			log.writeDown(data);
//		} catch (LogException le) {
//			System.out.println(le.getMessage());
//		}
	}
	
	public void logStationTables(String StationID){
		
		String logData = new String(
				         " neighborSet:" + tableContenet(neighborSet.keySet())+
		 				 " lostNeighborSet:" + tableContenet(lostNeighborSet.keySet())+
		 				 " secondHopNeighbors:" + tableContenet(secondHopNeighbors.keySet())+
		 				 logMPRs(neighborSet.keySet()));
		log(StationID, logData); 
	}

	/**
	 * @param keySet
	 * @return
	 */
	private String tableContenet(Set<String> set) {
		String collNames = new String();
		
		// put all names in one string
		for (String name : set) {
			collNames += " " + name + ",";
		}
		
		return collNames;
	}
	
	
}
