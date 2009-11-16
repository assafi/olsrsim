/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: InterfaceInformationBase.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package protocol.InformationBases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import protocol.Address;

/**
 * @author Eli Nazarov
 *
 */
public class InterfaceInformationBase {
	
	/**
	 * Maps the address of the 1-hop neighbor to its link property
	 */
	private Map<Address, Link> linkSet = null;
	
	/**
	 * Maps the address of the 2-hop neighbor to the set of 1-hop neighbors 
	 * that it is reachable from.
	 */
	private Map<Address, List<Address>> secondHopNeighbors = null;
	
	public InterfaceInformationBase(){
		linkSet = new HashMap<Address, Link>();
		secondHopNeighbors = new HashMap<Address, List<Address>>();
	}
	
	
	public Link getNeighborsLink(Address addr){
		return linkSet.get(addr);
	}
	
	public void setNeighborsLink(Address oneHopAddr, Link link){
		if (linkSet.containsKey(oneHopAddr)){
			linkSet.remove(oneHopAddr);
		}
		linkSet.put(oneHopAddr, link);
	}

	public List<Address> get2HopReachAddresses(Address secondHopAddr){
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
	public void add2HopNeighbor(Address secondHopAddr, Address firstHopReach){
		if (secondHopNeighbors.containsKey(secondHopAddr)){
			secondHopNeighbors.get(secondHopAddr).add(firstHopReach);
		}
		else {
			List<Address> oneHops = new ArrayList<Address>();
			oneHops.add(firstHopReach);
			secondHopNeighbors.put(secondHopAddr, oneHops);
		}
	}
}
