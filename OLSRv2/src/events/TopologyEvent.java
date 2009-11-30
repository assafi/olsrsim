/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TopologyEvent.java
 * Author: olsr1
 * Date: Nov 16, 2009
 *
 */
package events;

import java.util.Map;

import topology.IStation;

/**
 * @author olsr1
 *
 */
public class TopologyEvent extends Event {

	public enum TopologyEventType {
		CREATE_NODE, DESTROY_NODE,MOVE_NODE
	};
	
	private TopologyEventType type = null;
	private IStation station = null;
	
	/**
	 * @param time
	 */
	public TopologyEvent(long time, TopologyEventType type, IStation station) {
		super(time);
		this.type = type;
		this.station = station;
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@Override
	public void execute(Map<String, IStation> nodes) {
		
	}
}
