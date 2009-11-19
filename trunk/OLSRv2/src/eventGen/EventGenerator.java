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
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import topology.IStation;
import topology.ITopologyManager;

import dispatch.Dispatcher;

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
	
	
	/**
	 * 
	 */
	private EventGenerator(float factor) {
		this.factor = factor;
		this.dispatcher = Dispatcher.getInstance();
		this.nextEventTime = 0;
		this.nodes = new TopologyManager();
		//TODO this.topologyManager = new TopologyManager();
	}
	
	/**
	 * @param factor An optional parameter specify a factor to take into consideration 
	 * while determine when to generate events. 
	 * @return The EventGenerator singleton
	 */
	public static EventGenerator getInstance(Float factor){
		
		if (null == factor){
			factor = new Float(0.5);
		}
		
		if (null == instance){
			instance = new EventGenerator(factor);
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
		
		if (this.nextEventTime == 0){
			
			String stationID = UUID.randomUUID().toString();
			//TODO Point position = new Point(//...)
			this.topologyManager.createNewStation(stationID, stationLocation);
			return;
		}
		
		// TODO ...
	}
	
	private float getExpDelay(float factor){
		float u = (new Random().nextFloat())/(MAX_DELAY);
		return (float) ((-1/factor)*Math.log(1-u));
	}
	
}
