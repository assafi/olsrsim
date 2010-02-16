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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import layout.Layout;
import log.Log;
import log.LogException;
import main.SimulationParameters.StationsMode;

import topology.IStation;
import topology.ITopologyManager;
import topology.Station;
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
	private Map<Long, List<IStation>> stations2SendDataPerTime = new HashMap<Long, List<IStation>>();
	private Dispatcher dispatcher = null;
	private static EventGenerator instance = null;
	private int maxStations;
	private StationsMode stationBehavior = StationsMode.STATIC;
	private Map<Long, List<IStation>> stations2MovePerTime = new HashMap<Long, List<IStation>>();
	private double hopDistance;
	private static int HOP_TIME_INTERVAL = 20;
	
	/* Used only in MIXED mode to track stations who are scheduled to move */
	private Set<String> scheduled2MoveStations = new HashSet<String>(); 


	/*
	 * This stations2SendDataPerTime of nodes will be updated before the Topology
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
	private EventGenerator(float factor, Layout layout, int maxStations, StationsMode staticMod) {
		this.topologyPoissonicRate = factor;
		this.dispatcher = Dispatcher.getInstance();
		this.nextEventTime = 0;
		this.topologyManager = new TopologyManager();
		this.layout = layout;
		this.maxStations = maxStations;
		this.log = Log.getInstance();
		this.stationBehavior = staticMod;
		this.hopDistance = ( main.SimulationParameters.simulationSpeed.ordinal() + 1) * 
			main.SimulationParameters.simulationHopDistance;
	}

	/**
	 * @param dataEventsPoissonicRate 
	 * @param topologyPoissonicRate An optional parameter specify a topologyPoissonicRate to take into consideration 
	 * while determine when to generate events. 
	 * @param layout The Layout object representing the layout by which station will be
	 * generated.
	 * @param maxStations The maximum number of simulated stations
	 * @param stationBehavior choose between STATIC, DYNAMIC & MIXED
	 * @return The EventGenerator singleton
	 */
	public static EventGenerator getInstance(float topologyPoissonicRate, float dataEventsPoissonicRate, Layout layout, int maxStations, 
			StationsMode stationBehavior){

		if (null == instance){
			instance = new EventGenerator(topologyPoissonicRate, layout,maxStations,stationBehavior);
			instance.topologyPoissonicRate = topologyPoissonicRate;
			instance.dataChangePoissonicRate = dataEventsPoissonicRate;
		}
		EventGenerator eventGen = instance;
		return eventGen;
	}
	
	/**
	 * This method will only return an instance if one already exists. </br>
	 * it will not create a new one. you must use the getInstance() with the
	 * parameters in order to create a new instance.
	 * @return EventGenerator instance
	 */
	public static EventGenerator getInstance() {
		if (null != instance){
			return instance;
		}
		return null; 
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
		if (currentTime == this.nextEventTime && stationBehavior == StationsMode.MIXED){

			generateEvent();

			/*
			 * Update the time in which the next event will be created 
			 */
			this.nextEventTime += Math.max(getExpDelay(topologyPoissonicRate),1);
			System.out.println("Next event at: " + nextEventTime);
		}

		/*
		 * Handling stations movement
		 */
		if (stationBehavior == StationsMode.DYNAMIC && 
				stations2MovePerTime.containsKey(currentTime)) {
			moveStations(currentTime);
		}

		/*
		 * Handling send data events
		 */
		if (this.stations2SendDataPerTime.containsKey(currentTime)) {
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
				handleFirstMovement(station);
			} catch (Exception e) {
				logEvGenError(TopologyEvent.TopologyEventType.NODE_CREATE, e);
			}
		}
		if (stationBehavior == StationsMode.MIXED) {
			/*
			 * Next event time is generated 
			 */
			this.nextEventTime = Math.max(getExpDelay(topologyPoissonicRate),1);
			System.out.println("Next event at: " + nextEventTime);
		}
	}

	/**
	 * @param station
	 */
	private void handleFirstMovement(IStation station) {
		switch (stationBehavior) {
			case STATIC: {
				/*
				 * No movement in static mode
				 */
				return;
			}
			case DYNAMIC: {
				/*
				 * Setting the first movement no time "1"
				 */
				updateTable(1, station, stations2MovePerTime);
				return;
			}
			case MIXED: {
				/*
				 * Next event will be evaluated globally
				 */
				return;
			}
		}
	}

	/**
	 * @param station The station for which we want to schedule a send data event
	 */
	private void setNextDataEvent(IStation station, long baseTime) {

		long eventTime;
		eventTime = Math.max(getExpDelay(dataChangePoissonicRate),1) + baseTime;
		updateTable(eventTime, station, stations2SendDataPerTime);
	}

	/**
	 * @param currentTime The Current simulation time
	 * 
	 */
	private void generateDataEvents(long currentTime) {

		List<IStation> stationsThatWantsToSendDataPackets = stations2SendDataPerTime.remove(currentTime);
		for (IStation station : stationsThatWantsToSendDataPackets) {
			SendDataEvent dataEvent = new SendDataEvent(currentTime);
			dataEvent.setSrcName(station.getID());

			String trgID;
			if (topologyManager.count() > 1) {
				do {
					/* Will not send data to itself */
					trgID = topologyManager.getRandomStation();
				} while (trgID == station.getID());
				dataEvent.setDstName(trgID);
				dispatcher.pushEvent(dataEvent);
			}

			/*
			 * Next event time for the station.
			 */
			long nextTime;
			nextTime = Math.max(getExpDelay(dataChangePoissonicRate),1) + currentTime;
			updateTable(nextTime, station, stations2SendDataPerTime);
		}
	}

	/**
	 * @param nextTime The next event time for the specified station
	 * @param station The station that will be the source of the event
	 * @param stations2SendDataPerTime The stations2SendDataPerTime which identifies the action to be performed upon the event time
	 */
	static private void updateTable(long nextTime, IStation station, Map<Long, List<IStation>> map) {
		List<IStation> stationList;
		if (map.containsKey(nextTime)) {
			stationList = map.remove(nextTime);
		} else {
			stationList = new ArrayList<IStation>();
		}
		stationList.add(station);
		map.put(nextTime, stationList);
	}



	/**
	 * Generating random event when stationBehavior == MIXED
	 */
	public void generateEvent() {

		TopologyEventType type = randomAction();
		long currentTime = dispatcher.getCurrentVirtualTime();

		if (null == type) {
			return;
		}

		try{
			switch (type){
			case NODE_CREATE:
				TopologyEvent event = createStation();
				dispatcher.pushEvent(event);
				IStation station = event.getStation();
				setNextDataEvent(station, currentTime);
				break;
			case NODE_MOVE:
				dispatcher.pushEvents(moveStation(currentTime));
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

	private IStation getAvailableStation() {
		if (scheduled2MoveStations.size() == topologyManager.count()) {
			/*
			 * No available stations 
			 */
			return null;
		}
		
		/*
		 * Can't move stations that are moving (Design decision)
		 */
		String stationID = this.topologyManager.getRandomStation();
		while (scheduled2MoveStations.contains(stationID)){
			stationID = this.topologyManager.getRandomStation();
		}

		return topologyManager.getStationById(stationID);
	}
	
	/**
	 * Move a single station. only used when stationBehavior == MIXED
	 * @throws Exception 
	 */
	private Event[] moveStation(long currentTime) throws Exception {

		IStation station = getAvailableStation();
		if (null == station) {
			return null;
		}
		
		scheduled2MoveStations.add(station.getID());
		return moveStation(station, currentTime);
	}
	
	/**
	 * @param currentTime 
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 */
	private Event[] moveStation(IStation station, long currentTime) throws Exception {

		/*
		 * Setting the final destination of node's movement
		 */
		Point newStationLocation = this.layout.getRandomPoint(station.getLocation());
		while (this.topologyManager.doesStationExist(newStationLocation)){
			newStationLocation = this.layout.getRandomPoint(station.getLocation());
		}

		/*
		 * Calculating the number of hops
		 */
		double distance = Math.abs(newStationLocation.distance(station.getLocation()));
		int numHops = (int)Math.ceil(distance / hopDistance ); 

		assert(numHops > 0);
		
		/*
		 * hops Cartesian movement
		 */
		double xMovement = (newStationLocation.getX() - station.getLocation().getX()) / numHops;
		double yMovement = (newStationLocation.getY() - station.getLocation().getY()) / numHops;

		TopologyEvent[] hops = new TopologyEvent[numHops];

		/*
		 * the first hop details
		 */
		long hopTime = currentTime + 1;
		int hopXCoor = (int)(station.getLocation().getX() + xMovement);
		int hopYCoor = (int)(station.getLocation().getY() + yMovement);

		IStation midStation = null;
		for (int i = 0; i < numHops; i++) {
			if (i == numHops - 1) {
				/*
				 * last hop location is the final destination 
				 */
				midStation = new Station(station.getID(), newStationLocation); 
				hops[i] = new TopologyEvent(hopTime, TopologyEventType.NODE_LAST_MOVE, midStation);
			} else {
				/*
				 * non-final hops locations
				 */
				midStation = new Station(station.getID(), new Point(hopXCoor,hopYCoor));
				hopXCoor += xMovement;
				hopYCoor += yMovement;
				hops[i] = new TopologyEvent(hopTime, TopologyEventType.NODE_MOVE, midStation);
			}
			hopTime += HOP_TIME_INTERVAL;
		}

		/*
		 * If the mode is dynamic we should schedule the next movement,
		 * Immediately following the last hop.
		 */
		if (stationBehavior == StationsMode.DYNAMIC) {
			hopTime -= HOP_TIME_INTERVAL;
			updateTable(hopTime, station, stations2MovePerTime);
		}

		this.topologyManager.changeStationPosition(midStation.getID(), midStation.getLocation());
		return hops;
	}

	/**
	 * @param currentTime
	 */
	private void moveStations(long currentTime) {

		List<IStation> stationList = this.stations2MovePerTime.remove(currentTime);
		for (IStation station : stationList) {

			try {
				Event[] movement = moveStation(station, currentTime);
				this.dispatcher.pushEvents(movement);
			} catch (Exception e) {
				logEvGenError(TopologyEventType.NODE_MOVE, e);
			}
		}
	}

	private Event removeStation() throws Exception {
		IStation station = getAvailableStation();
		if (null == station) {
			return null;
		}
		
		this.topologyManager.removeStation(station.getID());
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

	/**
	 * @param stationID 
	 */
	public void stationReachedTargetLocation(String stationID) {
		if (null != stationID) {
			scheduled2MoveStations.remove(stationID);
		}
	}
}
