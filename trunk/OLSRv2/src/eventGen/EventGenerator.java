/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: EventGenerator.java
 * Author: Assaf
 * Date: Nov 15, 2009
 *
 */
package eventGen;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import layout.Layout;

import topology.IStation;
import topology.ITopologyManager;
import topology.TopologyManager;

import dispatch.Dispatcher;
import events.Event;
import events.TopologyEvent;
import events.TopologyEvent.TopologyEventType;

/**
 * @author Assaf
 *
 */
public class EventGenerator {

	private float factor;
	private static final float MAX_DELAY = 10; 
	private long nextEventTime;
	private Dispatcher dispatcher = null;
	private static EventGenerator instance = null;
	
	/*
	 * This map of nodes will be updated before the Topology
	 * Manager will be updated. so we'll avoid situations where
	 * we call for an event which relates to a state which will no 
	 * longer be relevant when the event reaches the front of the 
	 * priority queue. 
	 */
	private ITopologyManager topologyManager = null;
	private Layout layout = null;
	
	/**
	 * 
	 */
	private EventGenerator(float factor, Layout layout) {
		this.factor = factor;
		this.dispatcher = Dispatcher.getInstance();
		this.nextEventTime = 0;
		this.topologyManager = new TopologyManager();
		this.layout = layout;
	}
	
	/**
	 * @param factor An optional parameter specify a factor to take into consideration 
	 * while determine when to generate events. 
	 * @return The EventGenerator singleton
	 */
	public static EventGenerator getInstance(Float factor, Layout layout){
		
		if (null == factor){
			factor = new Float(0.5);
		}
		
		if (null == instance){
			instance = new EventGenerator(factor, layout);
		}
		return instance;
	}

	/**
	 * Virtual time tick
	 */
	public void tick(){
		if (dispatcher.getCurrentVirtualTime() == this.nextEventTime){
			generateEvent();
			
			/*
			 * Update the time in which the next event will be created 
			 */
			this.nextEventTime += getExpDelay(factor);
		}
	}

	/**
	 * 
	 */
	public void generateEvent() {
		
		Dispatcher dispatcher = Dispatcher.getInstance();
		
		if (this.nextEventTime == 0){
			
			
		} else {
			
			switch (randomAction()){
			case TopologyEventType.CREATE_NODE:
				dispatcher.pushEvent(createStation());
				break;
			case TopologyEventType.MOVE_NODE:
				dispatcher.pushEvent(moveStation());
				break;
			case TopologyEventType.DESTROY_NODE:
				dispatcher.pushEvent(removeStation());
				break;
			}
		}
		
	}


	/**
	 * @return a new TopologyEvent which describes a new station creation
	 */
	private Event createStation() {
		
		String stationID = UUID.randomUUID().toString();
		Point2D.Double stationLocation = this.layout.getRandomPoint();
		IStation station = this.topologyManager.createNewStation(stationID, stationLocation);
		return new TopologyEvent(0, TopologyEventType.CREATE_NODE, station);;
	}
	
	/**
	 * @return
	 */
	private Event moveStation() {
		Point2D.Double newStationLocation = this.layout.getRandomPoint();
		while (this.topologyManager.doesStationExist(newStationLocation)){
			
			newStationLocation = this.layout.getRandomPoint();
		}
		IStation station = topologyManager
			.changeStationPosition(topologyManager.getRandomStation(), newPosition); //will need to return the updated IStation
		return new TopologyEvent(this.nextEventTime, TopologyEventType.MOVE_NODE, station);
	}
	
	/**
	 * @return
	 */
	private Event removeStation() {
		IStation station = this.topologyManager.removeStation(topologyManager.getRandomStation());
		return new TopologyEvent(this.nextEventTime, TopologyEventType.DESTROY_NODE, station);
	}

	private float getExpDelay(float factor){
		float u = (new Random().nextFloat())/(MAX_DELAY);
		return (float) ((-1/factor)*Math.log(1-u));
	}
}
