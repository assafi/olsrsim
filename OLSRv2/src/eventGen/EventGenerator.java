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
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import layout.Layout;
import log.Log;
import log.LogException;

import topology.IStation;
import topology.ITopologyManager;
import topology.TopologyManager;

import data.SimLabels;
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
	private static final float MAX_DELAY = 2; 
	private long nextEventTime;
	private Dispatcher dispatcher = null;
	private static EventGenerator instance = null;
	private int maxStations;
	private static boolean firstIteration = true;
	private static boolean staticMode = false;
	
	/*
	 * This map of nodes will be updated before the Topology
	 * Manager will be updated. so we'll avoid situations where
	 * we call for an event which relates to a state which will no 
	 * longer be relevant when the event reaches the front of the 
	 * priority queue. 
	 */
	private ITopologyManager topologyManager = null;
	private Layout layout = null;
	
	private Log log = null;
	
	/**
	 * 
	 */
	private EventGenerator(float factor, Layout layout, int maxStations, boolean staticMod) {
		this.factor = factor;
		this.dispatcher = Dispatcher.getInstance();
		this.nextEventTime = 0;
		this.topologyManager = new TopologyManager();
		this.layout = layout;
		this.maxStations = maxStations;
		this.log = Log.getInstance();
		staticMode = staticMod;
	}
	
	/**
	 * @param factor An optional parameter specify a factor to take into consideration 
	 * while determine when to generate events. 
	 * @param layout The Layout object representing the layout by which station will be
	 * generated.
	 * @param maxStations The maximum number of simulated stations
	 * @return The EventGenerator singleton
	 */
	public static EventGenerator getInstance(Float factor, Layout layout, int maxStations, 
			boolean staticMode){
		
		if (null == factor){
			factor = new Float(0.5);
		}
		
		if (null == instance){
			instance = new EventGenerator(factor, layout,maxStations,staticMode);
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
	
		TopologyEventType type = randomAction();

		if (null == type) {
			return;
		}
		
		try{
			switch (type){
			case NODE_CREATE:
				dispatcher.pushEvent(createStation());
				break;
			case NODE_MOVE:
				dispatcher.pushEvent(moveStation());
				break;
			case NODE_DESTROY:
				dispatcher.pushEvent(removeStation());
				break;
			}
		} catch (Exception e){
			logEvGenError(type, e);
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
			return TopologyEventType.NODE_CREATE;
		} 
		
		if (staticMode){
			return null;
		}
		
		if (topologyManager.count() < maxStations){
			float rand = new Random().nextFloat();
			if (rand <= 0.33){
				return TopologyEventType.NODE_CREATE;
			} else if (rand <= 0.67) {
				return TopologyEventType.NODE_MOVE;
			} else {
				return TopologyEventType.NODE_DESTROY;
			}
		} else {
			float rand = new Random().nextFloat();
			if (rand <= 0.5){
				return TopologyEventType.NODE_MOVE;
			} else if (rand <= 0.67) {
				return TopologyEventType.NODE_DESTROY;
			}
			return TopologyEventType.NODE_CREATE;
		}	
	}

	/**
	 * @return a new TopologyEvent which describes a new station creation
	 * @throws Exception 
	 */
	private Event createStation() throws Exception {
		
		String stationID = UUID.randomUUID().toString();
		
		Point stationLocation = this.layout.getRandomPoint();
		IStation station = this.topologyManager.createNewStation(stationID, stationLocation);
		
		return new TopologyEvent(this.nextEventTime, TopologyEventType.NODE_CREATE, station);
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	private Event moveStation() throws Exception {
		Point newStationLocation = this.layout.getRandomPoint();
		
		while (this.topologyManager.doesStationExist(newStationLocation)){
			
			newStationLocation = this.layout.getRandomPoint();
		}
		
		IStation station = topologyManager
			.changeStationPosition(topologyManager.getRandomStation(), newStationLocation); //will need to return the updated IStation
		return new TopologyEvent(this.nextEventTime, TopologyEventType.NODE_MOVE, station);
	}
	
	/**
	 * @return
	 * @throws Exception 
	 */
	private Event removeStation() throws Exception {
		IStation station = this.topologyManager.removeStation(topologyManager.getRandomStation());
		return new TopologyEvent(this.nextEventTime, TopologyEventType.NODE_DESTROY, station);
	}

	private float getExpDelay(float factor){
		float u = (new Random().nextFloat())/(MAX_DELAY);
		return (float) ((-1/factor)*Math.log(1-u));
	}
	
	/**
	 * @param e logs the exception in the system log
	 */
	private void logEvGenError(TopologyEventType type, Exception e) {		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(this.nextEventTime));
		data.put(SimLabels.EVENT_TYPE.name(), type.name());
		data.put(SimLabels.ERROR.name(), "true");
		data.put(SimLabels.DETAILS.name(), e.getMessage());
		try {
			log.writeDown(data);
		} catch (LogException le) {
			System.out.println(le.getMessage());
		}
	}
}
