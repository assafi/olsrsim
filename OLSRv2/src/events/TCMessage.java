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
package events;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import protocol.IOLSRv2Protocol;
import protocol.InformationBases.NeighborProperty;
import topology.IStation;



/**
 * @author Eli Nazarov
 *
 */
public class TCMessage extends MessageEvent {

	/**
	 * This list contains all the stations that selected 
	 * the source station as MPR 
	 */
	private Map<String, NeighborProperty> mprSelectors = null;
	
	/**
	 * @param src
	 * @param time
	 */
	public TCMessage(String src, long time,
					 Map<String, NeighborProperty> neighborSet){
		super(src, time);
		
		mprSelectors = new HashMap<String, NeighborProperty>();
		
		// go over the neighbor set and for each neighbor
		// that selected this station as a MPR ad to mprSelectors
		Set<Entry<String, NeighborProperty>> neighbors = neighborSet.entrySet();
		for (Entry<String, NeighborProperty> entry : neighbors) {
			if (entry.getValue().isMpr_selector()){
				mprSelectors.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * @return the mprSelectors
	 */
	public Map<String, NeighborProperty> getMprSelectors() {
		return mprSelectors;
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Object nodes) {
		
		Collection<IStation> stations = (Collection<IStation>)nodes;
		for (IStation station : stations) {
			IOLSRv2Protocol olsrProtocol = station.getOLSRv2Protocol();
			olsrProtocol.reciveTCMessage(this);
		}
	}


}
