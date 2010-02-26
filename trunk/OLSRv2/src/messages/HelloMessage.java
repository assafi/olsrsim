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
package messages;

import java.util.Collection;
import java.util.Map;

import data.SimEvents;

import events.MessageEvent;

import protocol.InformationBases.NeighborProperty;
import protocol.OLSRv2Protocol.IOLSRv2Protocol;
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
		this.messageType = SimEvents.HELLO_SENT.name();
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
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Object nodes) {
		logEvent(getSource(), null,null, null, null);
		Collection<IStation> stations = (Collection<IStation>)nodes;
		for (IStation station : stations) {
			logEvent(station.getID(), SimEvents.HELLO_REACHED.name(),station.getID(), null, null);
			IOLSRv2Protocol olsrProtocol = station.getOLSRv2Protocol();
			olsrProtocol.reciveHelloMessage(this);
		}
	}
}
