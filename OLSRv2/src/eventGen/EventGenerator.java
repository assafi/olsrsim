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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import events.SendDataEvent;
import events.TopologyEvent;
import events.TopologyEvent.TopologyEventType;

/**
 * @author Assaf
 *
 */
public class EventGenerator {

	private float topologyPoissonicRate;
	private float dataChangePoissonicRate;

	private long nextEventTime;
	private Map<Long, List<IStation>> dataTime2stations = new HashMap<Long, List<IStation>>();
	private Dispatcher dispatcher = null;
	private static EventGenerator instance = null;
	private int maxStations;
	private boolean staticMode = false;
	
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
		this.topologyPoissonicRate = factor;
		this.dispatcher = Dispatcher.getInstance();
		this.nextEventTime = 0;
		this.topologyManager = new TopologyManager();
		this.layout = layout;
		this.maxStations = maxStations;
		this.log = Log.getInstance();
		this.staticMode = staticMod;
	}
	
	/**
	 * @param dataEventsPoissonicRate 
	 * @param topologyPoissonicRate An optional parameter specify a topologyPoissonicRate to take into consideration 
	 * while determine when to generate events. 
	 * @param layout The Layout object representing the layout by which station will be
	 * generated.
	 * @param maxStations The maximum number of simulated stations
	 * @param staticMode false to enable nodes mobility
	 * @return The EventGenerator singleton
	 */
	public static EventGenerator getInstance(float topologyPoissonicRate, float dataEventsPoissonicRate, Layout layout, int maxStations, 
			boolean staticMode){
		
		if (null == instance){
			instance = new EventGenerator(topologyPoissonicRate, layout,maxStations,staticMode);
		}
		EventGenerator eventGen = instance;
		eventGen.topologyPoissonicRate = topologyPoissonicRate;
		eventGen.dataChangePoissonicRate = dataEventsPoissonicRate;
		return eventGen;
	}

	/**
	 * Virtual time tick
	 */
	public void tick(){
		long currentTime = dispatcher.getCurrentVirtualTime();
		
		if (currentTime == 0) {
			phase1();
			return;
		}
		
		
		/*
		 * Handling topology events
		 */
		if (currentTime == this.nextEventTime){
			generateEvent();
			
			/*
			 * Update the time in which the next event will be created 
			 */
			this.nextEventTime += Math.max(getExpDelay(topologyPoissonicRate),1);
		}
		
		/*
		 * Handling send data events
		 */
		if (this.dataTime2stations.containsKey(currentTime)) {
			generateDataEvents(currentTime);
		}
	}

	/**
	 * At the first phase we create all the stations (until the maximum).
	 * for each station we also schedule the next time in which the data event
	 * will be created.
	 */
	private void phase1() {

		int i = this.maxStations;
		while (i-- != 0) {
			try {
				TopologyEvent event = createStation();
				dispatcher.pushEvent(event);
				IStation station = event.getStation();
				setNextDataEvent(station,0);
			} catch (Exception e) {
				logEvGenError(TopologyEvent.TopologyEventType.NODE_CREATE, e);
			}
		}
		
		this.nextEventTime = Math.max(getExpDelay(topologyPoissonicRate),1);
	}

	/**
	 * @param station The station for which we want to schedule a send data event
	 */
	private void setNextDataEvent(IStation station, long baseTime) {
		
		long eventTime;
		eventTime = Math.max(getExpDelay(dataChangePoissonicRate),1) + baseTime;
		updateTable(eventTime, station);
	}

	/**
	 * @param currentTime The Current simulation time
	 * 
	 */
	private void generateDataEvents(long currentTime) {
		
		List<IStation> stationsThatWantsToSendDataPackets = dataTime2stations.remove(currentTime);
		for (IStation station : stationsThatWantsToSendDataPackets) {
			SendDataEvent dataEvent = new SendDataEvent(currentTime);
			dataEvent.setSrcName(station.getID());
			
			String trgID;
			do {
				/* Will not send data to itself */
				trgID = topologyManager.getRandomStation();
			} while (trgID == station.getID());
			dataEvent.setDstName(trgID);
			dispatcher.pushEvent(dataEvent);
			
			/*
			 * Next event time for the station.
			 */
			long nextTime;
			nextTime = Math.max(getExpDelay(dataChangePoissonicRate),1) + currentTime;
			updateTable(nextTime, station);
		}
	}

	/**
	 * @param nextTime The next SendData event time
	 * @param station The station that will be the source of the event
	 */
	private void updateTable(long nextTime, IStation station) {
		List<IStation> stationsThatWantsToSendDataPackets;
		if (dataTime2stations.containsKey(nextTime)) {
			stationsThatWantsToSendDataPackets = dataTime2stations.remove(nextTime);
		} else {
			stationsThatWantsToSendDataPackets = new ArrayList<IStation>();
		}
		stationsThatWantsToSendDataPackets.add(station);
		dataTime2stations.put(nextTime, stationsThatWantsToSendDataPackets);
	}

	/**
	 * 
	 */
	public void generateEvent() {
		
		TopologyEventType type = randomAction();

		if (null == type) {
			return;
		}
		
		try{
			switch (type){
			case NODE_CREATE:
				TopologyEvent event = createStation();
				dispatcher.pushEvent(event);
				IStation station = event.getStation();
				setNextDataEvent(station, dispatcher.getCurrentVirtualTime());
				break;
			case NODE_MOVE:
				dispatcher.pushEvent(moveStation());
				break;
			case NODE_DESTROY:
				dispatcher.pushEvent(removeStation());
				/*
				 * SendData events for station that don't exist will be discarded at the
				 * Dispatcher.
				 */
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
			if (rand <= 0.5 || topologyManager.count() == 1){
				return TopologyEventType.NODE_MOVE;
			} 
			return TopologyEventType.NODE_DESTROY;
		}	
	}

	/**
	 * @return a new TopologyEvent which describes a new station creation
	 * @throws Exception 
	 */
	private TopologyEvent createStation() throws Exception {
		
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
	
	private Event removeStation() throws Exception {
		IStation station = this.topologyManager.removeStation(topologyManager.getRandomStation());
		return new TopologyEvent(this.nextEventTime, TopologyEventType.NODE_DESTROY, station);
	}
	
	private static long getExpDelay(double poissonicRate) {
	    double U = new Random().nextDouble();
		return (long) (((-1/poissonicRate)*Math.log(1-U))); //*10
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
