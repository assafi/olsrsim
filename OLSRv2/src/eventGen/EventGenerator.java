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
	private int maxStations;
	private static boolean firstIteration = true;
	
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
	private EventGenerator(float factor, Layout layout, int maxStations) {
		this.factor = factor;
		this.dispatcher = Dispatcher.getInstance();
		this.nextEventTime = 0;
		this.topologyManager = new TopologyManager();
		this.layout = layout;
		this.maxStations = maxStations;
	}
	
	/**
	 * @param factor An optional parameter specify a factor to take into consideration 
	 * while determine when to generate events. 
	 * @param layout The Layout object representing the layout by which station will be
	 * generated.
	 * @return The EventGenerator singleton
	 */
	public static EventGenerator getInstance(Float factor, Layout layout, int maxStations){
		
		if (null == factor){
			factor = new Float(0.5);
		}
		
		if (null == instance){
			instance = new EventGenerator(factor, layout,maxStations);
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
			case CREATE_NODE:
				dispatcher.pushEvent(createStation());
				break;
			case MOVE_NODE:
				dispatcher.pushEvent(moveStation());
				break;
			case DESTROY_NODE:
				dispatcher.pushEvent(removeStation());
				break;
			}
		}
		
	}


	/**
	 * @return
	 */
	private TopologyEventType randomAction() {
		if (firstIteration && topologyManager.count() == maxStations) {
			firstIteration = false;
		}
		
		if (firstIteration){
			return TopologyEventType.CREATE_NODE;
		} 
		
		if (topologyManager.count() < maxStations){
			float rand = new Random().nextFloat();
			if (rand <= 0.33){
				return TopologyEventType.CREATE_NODE;
			} else if (rand <= 0.67) {
				return TopologyEventType.MOVE_NODE;
			} else {
				return TopologyEventType.DESTROY_NODE;
			}
		} else {
			float rand = new Random().nextFloat();
			if (rand <= 0.5){
				return TopologyEventType.MOVE_NODE;
			} else if (rand <= 0.67) {
				return TopologyEventType.DESTROY_NODE;
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
		while (this.topologyManager.stationExist(newStationLocation)){
			
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
		IStation station;
		try {
			station = this.topologyManager.removeStation(topologyManager.getRandomStation());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new TopologyEvent(this.nextEventTime, TopologyEventType.DESTROY_NODE, station);
	}

	private float getExpDelay(float factor){
		float u = (new Random().nextFloat())/(MAX_DELAY);
		return (float) ((-1/factor)*Math.log(1-u));
	}
}
