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
//				System.out.println(currentVirtualTime);
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
			if (MessageEvent.class.isAssignableFrom(currentEvent.getClass())){

				MessageEvent me = (MessageEvent)currentEvent;
				try {
					List<IStation> relevantNodesList = 
						this.topologyManager.getStationNeighbors(me.getSource());
					me.execute(relevantNodesList);
				} catch (Exception e) {
					logDispError(currentEvent,e);
				}
			}

			if (TopologyEvent.class.isAssignableFrom(currentEvent.getClass())){

				TopologyEvent te = (TopologyEvent)currentEvent;
				try {
					te.execute(this.topologyManager);
				} catch (Exception e) {
					logDispError(currentEvent,e);
				}
			}

			if (IntervalEndEvent.class.isAssignableFrom(currentEvent.getClass())){
				IntervalEndEvent ie = (IntervalEndEvent)currentEvent;
				try {
					IStation station = this.topologyManager.getStationById(ie.getSource());
					ie.execute(station);
				} catch (Exception e) {
					logDispError(currentEvent,e);
				}
			}

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
}
