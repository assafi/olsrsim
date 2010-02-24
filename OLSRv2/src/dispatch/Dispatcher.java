/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: Dispatcher.java
 * Author: Assaf
 * Date: Nov 15, 2009
 *
 */
package dispatch;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import layout.ClustersLayout;
import layout.Layout;
import layout.LayoutException;
import layout.UniformLayout;
import log.Log;
import log.LogException;
import main.SimulationParameters;
import main.SimulationParameters.StationsMode;
import messages.DataMessage;
import messages.HelloMessage;
import messages.TCMessage;
import topology.IStation;
import topology.ITopologyManager;
import topology.Station;
import topology.TopologyManager;
import data.SimEvents;
import data.SimLabels;
import eventGen.EventGenerator;
import events.Event;
import events.IntervalEndEvent;
import events.MessageEvent;
import events.SendDataEvent;
import events.StopEvent;
import events.TopologyEvent;
import gui.GuiEventsQueue;

/**
 * @author Assaf
 * 
 */
public class Dispatcher implements IDispatcher {

	private static Dispatcher instance = null;
	private long currentVirtualTime;
	private EventGenerator eventGen = null;
	private ITopologyManager topologyManager = null;
	private Log log = null;
	/*
	 * The events priority queue, sorted by the virtual time.
	 */
	private PriorityQueue<Event> tasksQueue = null;

	/**
	 * 
	 */
	private Dispatcher() {

		this.tasksQueue = new PriorityQueue<Event>(INITIAL_QUEUE_SIZE,
				new Comparator<Event>() {
			public int compare(Event ev1, Event ev2) {
				return (int) (ev1.getTime() - ev2.getTime());
			}
		});

		this.currentVirtualTime = 0;
		this.topologyManager = new TopologyManager(); 
		this.log = Log.getInstance();

		/*
		 * Can't set eventGen here because of deadlock.
		 */
	}

	/**
	 * @return The dispatcher singleton
	 */
	public static synchronized Dispatcher getInstance() {

		if (null == instance) {
			instance = new Dispatcher();
		}

		return instance;
	}

	/**
	 * @param event The event to be pushed to the tasks queue 
	 */
	public synchronized void pushEvent(Event event) {

		this.tasksQueue.add(event);
	}

	/**
	 * Used mainly to push several events at known intervals. </br>
	 * e.g - movement of stations. a few MOVE events, representing a node's hopes.
	 * @param events Pushing several events at once.
	 */
	public synchronized void pushEvents(Event[] events) {

		if (null == events) {
			return;
		}

		for (Event event : events) {
			this.tasksQueue.add(event);
		}
	}

	/**
	 * 	Virtual time is a logical order between events that occur in the 
	 * simulator. It represents an "Happened Before" ratio between events.
	 * The CurrentVirtualTime is determined by the last Event Virtual time
	 * pulled from the tasks queue.
	 * @return The current simulator virtual time.
	 */
	public synchronized long getCurrentVirtualTime(){

		return this.currentVirtualTime;
	}

	/**
	 * @throws DispatcherException
	 */
	public void startSimulation() throws DispatcherException {

		if (null != this.eventGen){
			throw new DispatcherException("Can only start the dispatcher once...");
		}

		Station.defaultReceptionRadius = SimulationParameters.receptionRadius;

		Layout layout;

		try {
			layout = createLayout();
		} catch (LayoutException e) {
			throw new DispatcherException(e);
		}

		float topologyPoissonicRate = SimulationParameters.topologyPoissonicRate;

		float dataEventsPoissonicRate = SimulationParameters.dataEventsPoissonicRate;

		int maxStations = SimulationParameters.maxStations;

		int timeout = SimulationParameters.simulationEndTime;

		StationsMode stationsBehavior = SimulationParameters.stationsMode;

		this.eventGen = EventGenerator.getInstance(topologyPoissonicRate,dataEventsPoissonicRate, layout, maxStations,stationsBehavior);

		/*
		 * Generating the first event
		 */
		this.eventGen.tick();

		while (timeout > currentVirtualTime){

			Event currentEvent = null;

			long nextEventTime;

			if (tasksQueue.isEmpty() || 
					(nextEventTime = peek().getTime()) > currentVirtualTime){
				this.currentVirtualTime++;
				this.eventGen.tick();
				continue;
			}

			/*
			 * Error handling
			 */
			if (nextEventTime < currentVirtualTime){
				logDispError(dequeue(),new DispatcherException("Simulation internal error - event time: " + 
						nextEventTime + ", sim time: " + currentVirtualTime + "."));
				continue;
			}

			currentEvent = dequeue();
			
			// This will provide the gui the info it needs to display a data message
			if(currentEvent.getClass().equals(DataMessage.class)) {
				DataMessage dataMessageEvent = (DataMessage)currentEvent;
				if(topologyManager.doesStationExist(dataMessageEvent.getLocalSrc()) && 
						topologyManager.doesStationExist(dataMessageEvent.getLocalDst())) {
					IStation srcStation = topologyManager.getStationById(dataMessageEvent.getLocalSrc());
					IStation dstStation = topologyManager.getStationById(dataMessageEvent.getLocalDst());
					if(srcStation.isInRange(dstStation)) {
						dataMessageEvent.setEventSourceLocation(srcStation.getLocation());
						dataMessageEvent.setLocalDestinationLocation(dstStation.getLocation());
						GuiEventsQueue.getInstance().addEvent(currentEvent);
					}
				}
			} 
			else {
				GuiEventsQueue.getInstance().addEvent(currentEvent);
			}

			if (currentEvent.getClass().equals(StopEvent.class)){
				break;
			}

			/*
			 * Handles the event according to it's type
			 */
			
			/*
			 * MessageEvent is used for Hello & TC messages as well as Data messages
			 */
			if (MessageEvent.class.isAssignableFrom(currentEvent.getClass())){

				MessageEvent me = (MessageEvent)currentEvent;
				try {
					List<IStation> relevantNodesList = 
						this.topologyManager.getStationNeighbors(me.getSource());
					me.execute(relevantNodesList);
					
					/*
					 * If a station thinks that the next hop for a data message is
					 * in it's radius it sets the local target as this node. however
					 * if that target is not physible (because of the the local source
					 * databases has not converged with the physical world) we would 
					 * like to register that in the log. (the message will get lost)
					 */
					checkDataPhysibility(me, relevantNodesList);
				} catch (Exception e) {
					logDispError(currentEvent,e);
				}
			}

			/*
			 * Topology changes - i.e. create/move/remove stations
			 */
			if (TopologyEvent.class.isAssignableFrom(currentEvent.getClass())){

				TopologyEvent te = (TopologyEvent)currentEvent;
				try {
					te.execute(this.topologyManager);
				} catch (Exception e) {
					logDispError(currentEvent,e);
				}
			}

			/*
			 * End of Hello/TC intervals
			 */
			if (IntervalEndEvent.class.isAssignableFrom(currentEvent.getClass())){
				IntervalEndEvent ie = (IntervalEndEvent)currentEvent;
				try {
					IStation station = this.topologyManager.getStationById(ie.getSource());
					ie.execute(station);
				} catch (Exception e) {
					logDispError(currentEvent,e);
				}
			}

			/*
			 * Alerts the station to send a data packet
			 */
			if (SendDataEvent.class.isAssignableFrom(currentEvent.getClass())) {
				SendDataEvent sde = (SendDataEvent)currentEvent;
				try {
					IStation station = this.topologyManager.getStationById(sde.getSrcName());
					if (null != station) {
						sde.execute(station);
					}
				} catch (Exception e) {
					logDispError(currentEvent,e);
				}
			}
		}

		// Alerts the GUI that the simulation is over
		GuiEventsQueue.getInstance().addEvent(new StopEvent(timeout));
		try {
			new StopEvent().execute(null);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * @param me
	 * @param relevantNodesList 
	 */
	private void checkDataPhysibility(MessageEvent me, List<IStation> relevantNodesList) {
		if (!DataMessage.class.isAssignableFrom(me.getClass())) {
			return;
		}
		
		DataMessage dm = (DataMessage)me;
		if (null != relevantNodesList &&
				!relevantNodesList.contains(dm.getLocalDst())) {
			logTargetNotPhysible(dm);
		}
	}

	private synchronized Event dequeue() {
		return this.tasksQueue.poll();
	}

	private synchronized Event peek() {
		return this.tasksQueue.peek();
	}

	/**
	 * @return
	 * @throws LayoutException 
	 */
	private Layout createLayout() throws LayoutException {

		int xBoundry = SimulationParameters.xBoundry;
		int yBoundry = SimulationParameters.yBoundry;

		switch (SimulationParameters.layoutMode) {
		case UNIFORM: 
			return new UniformLayout(xBoundry,yBoundry);
		case CLUSTER:

			int clusterNum = SimulationParameters.clusterNum;
			int clusterRadius = SimulationParameters.clusterRadius;
			return new ClustersLayout(clusterNum, xBoundry, yBoundry, clusterRadius);
		}

		throw new LayoutException("Undefined layout");
	}

	/**
	 * @param currentEvent
	 * @param e 
	 */
	public void logDispError(Event currentEvent, Exception e) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(currentEvent.getTime()));
		data.put(SimLabels.EVENT_TYPE.name(), currentEvent.getClass().getName());
		if (TCMessage.class.isAssignableFrom(currentEvent.getClass())){
			data.put(SimLabels.GLOBAL_SOURCE.name(),((TCMessage)currentEvent).getSource());
		}

		if (HelloMessage.class.isAssignableFrom(currentEvent.getClass())){
			data.put(SimLabels.GLOBAL_SOURCE.name(),((HelloMessage)currentEvent).getSource());
		}

		data.put(SimLabels.ERROR.name(), "true");
		data.put(SimLabels.DETAILS.name(), e.getMessage());
		try {
			log.writeDown(data);
		} catch (LogException le) {
			System.out.println(le.getMessage());
		}
	}
	
	public void logTargetNotPhysible(DataMessage dm) {
		Log log = Log.getInstance();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
		data.put(SimLabels.EVENT_TYPE.name(), SimEvents.LOCAL_TARGET_NOT_PHYSIBLE.name());
		data.put(SimLabels.NODE_ID.name(),dm.getLocalSrc());
		data.put(SimLabels.LOCAL_SOURCE.name(), dm.getLocalSrc());
		data.put(SimLabels.LOCAL_TARGET.name(),dm.getLocalDst());
		data.put(SimLabels.GLOBAL_SOURCE.name(), dm.getGlobalSrc());
		data.put(SimLabels.GLOBAL_TARGET.name(),dm.getGlobalDst());
		data.put(SimLabels.LOST.name(), "1");
		data.put(SimLabels.DETAILS.name(), "Local target is not physible. probably because local source" +
				" databases do not match actual topology.");
		try {
			log.writeDown(data);
		} catch (LogException le) {
			System.out.println(le.getMessage());
		}
	}
}
