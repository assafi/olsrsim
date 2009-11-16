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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import protocol.Address;

/**
 * @author Eli Nazarov
 *
 */
public class NeighborInformationBase {
	
	/**
	 * Maps the address of the 1-hop neighbor interface to its property
	 */
	private Map<Address, NeighborProperty> neighborSet = null;
	
	/**
	 * Maps the address of the 1-hop neighbor address that was lost and time
	 * when this entry should be deleted.
	 */
	private Map<Address, Integer> lostNeighborSet = null;
	
	public NeighborInformationBase(){
		lostNeighborSet = new HashMap<Address, Integer>();
		neighborSet = new HashMap<Address, NeighborProperty>();
	}
	
	public void addNeighbor(Address neighbor, NeighborProperty property) {
		neighborSet.put(neighbor, property);
	}

	public NeighborProperty getNeighborPrperty(Address adrr) {
		return neighborSet.get(adrr);
	}

	public void addToLostNeighbors(Address neighbor, int time) {
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
		Set<Address> elements = lostNeighborSet.keySet();
		Iterator<Address> it = elements.iterator();
		Address key = it.next();
		while (it.hasNext()){
			int entryTime = lostNeighborSet.get(key);
			if (entryTime > time){ //TODO think how to compare times- made create an abstraction of time
				lostNeighborSet.remove(key);
			}
		}
	}
	/**
	 * @return the neighborSet
	 */
	public Map<Address, NeighborProperty> getAllNeighbors() {
		return neighborSet;
	}

	/**
	 * @return the lostNeighborSet
	 */
	public Map<Address, Integer> getAllLostNeighborSet() {
		return lostNeighborSet;
	}
	
	
}
