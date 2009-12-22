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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import protocol.IOLSRv2Protocol;
import protocol.InformationBases.NeighborProperty;
import topology.IStation;

/**
 * @author Eli Nazarov
 *
 */
public class HelloMessage extends MessageEvent {
	
	private Map<String, NeighborProperty> neighborSet = null;
	private Map<String, Long> lostNeighborSet = null;
	
	/* Willingness parameter of the Hello Message must be set by OLSRv2 layer
	 * before message transmission*/
	private int willingnes;
	
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

	/**
	 * @return the willingnes
	 */
	public int getWillingnes() {
		return willingnes;
	}

	/**
	 * @param willingnes the willingnes to set
	 */
	public void setWillingnes(int willingnes) {
		this.willingnes = willingnes;
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@Override
	public void execute(Map<String, IStation> nodes) {
		Collection<IStation> stations = nodes.values();
		for (IStation station : stations) {
			IOLSRv2Protocol olsrProtocol = station.getOLSRv2Protocol();
			olsrProtocol.reciveHelloMessage(this);
		}
	}
	
	
	

}
