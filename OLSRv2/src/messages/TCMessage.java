/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TCMessage.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package messages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import data.SimEvents;

import events.MessageEvent;

import protocol.InformationBases.NeighborProperty;
import protocol.OLSRv2Protocol.IOLSRv2Protocol;
import topology.IStation;



/**
 * @author Eli Nazarov
 *
 */
public class TCMessage extends MessageEvent {

	private String globalSrc = null;
	/**
	 * This list contains all the stations that selected 
	 * the source station as MPR 
	 */
	private Map<String, NeighborProperty> mprSelectors = null;
	
	/**
	 * @param src
	 * @param time
	 */
	public TCMessage(String localSrc, String globalSrc, long time,
					 Map<String, NeighborProperty> neighborSet){
		super(localSrc, time);
		this.globalSrc = globalSrc;
		
		mprSelectors = new HashMap<String, NeighborProperty>();
		
		// go over the neighbor set and for each neighbor
		// that selected this station as a MPR ad to mprSelectors
		Set<Entry<String, NeighborProperty>> neighbors = neighborSet.entrySet();
		for (Entry<String, NeighborProperty> entry : neighbors) {
			if (entry.getValue().isMpr_selector()){
				mprSelectors.put(entry.getKey(), entry.getValue());
			}
		}
		this.messageType = SimEvents.TC_SENT.name();
	}

	/**
	 * @return the mprSelectors
	 */
	public Map<String, NeighborProperty> getMprSelectors() {
		return mprSelectors;
	}
	
	

	/**
	 * @return the globalSrc
	 */
	public String getGlobalSrc() {
		return globalSrc;
	}

	/**
	 * @param globalSrc the globalSrc to set
	 */
	public void setGlobalSrc(String globalSrc) {
		this.globalSrc = globalSrc;
	}
	
	/**
	 * @return the globalSrc
	 */
	public String getLocalSrc() {
		return this.getSource();
	}

	/**
	 * @param globalSrc the globalSrc to set
	 */
	public void setLocalSrc(String globalSrc) {
		this.setSource(globalSrc);
	}
	

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Object nodes) {
		Collection<IStation> stations = (Collection<IStation>)nodes;
		for (IStation station : stations) {
			logEvent(station.getID(), globalSrc, null);
			IOLSRv2Protocol olsrProtocol = station.getOLSRv2Protocol();
			olsrProtocol.reciveTCMessage(this);
		}
	}


}
