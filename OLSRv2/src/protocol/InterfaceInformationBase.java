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
package protocol;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author Eli Nazarov
 *
 */
public class InterfaceInformationBase {
	
	private static InterfaceInformationBase instance = null;
	
	/**
	 * Maps the address of the 1-hop neighbor interface to its link property
	 */
	private HashMap<Address, Link> linkSet = null;
	
	/**
	 * Maps the address of the 2-hop neighbor interface to the set of 1-hop neighbors 
	 * that it is reachable from.
	 */
	private HashMap<Address, Vector<Address>> secondHopNeighbors = null;
	
	private InterfaceInformationBase(){
	}
	
	public static synchronized InterfaceInformationBase getInstance(){
		if (null == instance){
			instance = new InterfaceInformationBase();
			instance.linkSet = new HashMap<Address, Link>();
			instance.secondHopNeighbors = new HashMap<Address, Vector<Address>>();
		}
		
		return instance;
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

	public Vector<Address> get2HopReachAddresses(Address secondHopAddr){
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
			Vector<Address> oneHops = new Vector<Address>();
			oneHops.add(firstHopReach);
			secondHopNeighbors.put(secondHopAddr, oneHops);
		}
	}
}
