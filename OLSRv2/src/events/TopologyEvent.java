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

import java.util.Hashtable;
import java.util.Map;

import data.SimEvents;
import data.SimLabels;
import dispatch.Dispatcher;

import log.Log;
import log.LogException;
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
		NODE_CREATE, NODE_DESTROY, NODE_MOVE
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
		case NODE_CREATE:
			tm.createNewStation(station.getID(), station.getLocation());
			break;
		case NODE_DESTROY:
			tm.removeStation(station.getID());
			break;
		case NODE_MOVE:
			tm.changeStationPosition(station.getID(), station.getLocation());
			break;
		}
		
		logTopologyEvent();
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
	
	private void logTopologyEvent() {
		Map<String, String> data = new Hashtable<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), 
				Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
		data.put(SimLabels.X_COOR.name(), 
				Integer.toString(station.getLocation().x));
		data.put(SimLabels.Y_COOR.name(), 
				Integer.toString(station.getLocation().y));
		data.put(SimLabels.NODE_ID.name(),station.getID());
		data.put(SimLabels.EVENT_TYPE.name(), type.name());
		try {
			Log.getInstance().writeDown(data);
		} catch (LogException e) {
			System.err.println(e.getMessage());
		}
	}
}
