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

import topology.IStation;
import topology.TopologyManager;

/**
 * @author olsr1
 *
 */
public class TopologyEvent extends Event {

	/**
	 * @author Assaf
	 *
	 */
	public enum TopologyEventType {
		CREATE_NODE, DESTROY_NODE,MOVE_NODE
	};
	
	private TopologyEventType type = null;
	private IStation station = null;
	
	/**
	 * @param time event virtual time
	 * @param type the sub-type of the topology event
	 * @param station the station this event relates to
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
	public void execute(Object topologyManager) throws Exception {
		
		TopologyManager tm = (TopologyManager)topologyManager;
		switch (type){
		case CREATE_NODE:
			tm.createNewStation(station.getID(), station.getLocation());
			break;
		case DESTROY_NODE:
			tm.removeStation(station.getID());
			break;
		case MOVE_NODE:
			tm.changeStationPosition(station.getID(), station.getLocation());
			break;
		}
	}
	
	/**
	 * @return The topology event type
	 */
	public TopologyEventType getType(){
		return this.type;
	}
	
	/**
	 * @return the station
	 */
	public IStation getStation(){
		return this.station;
	}
}
