/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: HelloMessage.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package events;

import java.util.HashMap;
import java.util.Map;

import protocol.Address;
import protocol.InformationBases.Link;
import protocol.InformationBases.NeighborProperty;

/**
 * @author Eli Nazarov
 *
 */
public class HelloMessage extends MessageEvent {

	private Map<Address, Link> linkSet = null;
	private Map<Address, NeighborProperty> neighborSet = null;
	private Map<Address, Integer> lostNeighborSet = null;
	
	public HelloMessage(Address src, long time, 
						Map<Address, Link> linkSet, 
						Map<Address, NeighborProperty> neighborSet,
						Map<Address, Integer> lostNeighborSet) {
		super(src, time);
		this.linkSet = linkSet;
		this.neighborSet = neighborSet;
		this.lostNeighborSet = lostNeighborSet;
	}

	/**
	 * @return the linkSet
	 */
	public Map<Address, Link> getLinkSet() {
		return linkSet;
	}

	/**
	 * @return the neighborSet
	 */
	public Map<Address, NeighborProperty> getNeighborSet() {
		return neighborSet;
	}

	/**
	 * @return the lostNeighborSet
	 */
	public Map<Address, Integer> getLostNeighborSet() {
		return lostNeighborSet;
	}
	
	

}
