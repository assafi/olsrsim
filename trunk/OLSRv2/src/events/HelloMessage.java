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

import protocol.InformationBases.NeighborProperty;

/**
 * @author Eli Nazarov
 *
 */
public class HelloMessage extends MessageEvent {
	
	private Map<String, NeighborProperty> neighborSet = null;
	private Map<String, Long> lostNeighborSet = null;
	
	public HelloMessage(String src, long time,  
						Map<String, NeighborProperty> neighborSet,
						Map<String, Long> lostNeighborSet) {
		super(src, time);
		this.neighborSet = neighborSet;
		this.lostNeighborSet = lostNeighborSet;
	}

	/**
	 * @return the neighborSet
	 */
	public Map<String, NeighborProperty> getNeighborSet() {
		return neighborSet;
	}

	/**
	 * @return the lostNeighborSet
	 */
	public Map<String, Long> getLostNeighborSet() {
		return lostNeighborSet;
	}
	
	

}
