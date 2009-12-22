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
import java.util.Hashtable;
import java.util.Map;
import java.util.PriorityQueue;

import layout.Layout;

import eventGen.EventGenerator;
import events.Event;
import events.StopEvent;
import topology.IStation;

/**
 * @author Assaf
 * 
 */
public class Dispatcher implements IDispatcher {

	private static Dispatcher instance = null;
	private Map<String, IStation> nodes = null;
	private long currentVirtualTime;
	private EventGenerator eventGen = null;

	/*
	 * The events priority queue, sorted by the virtual time.
	 */
	private PriorityQueue<Event> tasksQueue = null;

	/**
	 * 
	 */
	private Dispatcher() {

		this.nodes = new Hashtable<String, IStation>();

		this.tasksQueue = new PriorityQueue<Event>(INITIAL_QUEUE_SIZE,
				new Comparator<Event>() {
					public int compare(Event ev1, Event ev2) {
						return (int) (ev1.getTime() - ev2.getTime());
					}
				});
		
		this.currentVirtualTime = 0;
		
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
	 * @param factor An optional parameter specify a factor to take into consideration 
	 * @param layout The Layout object representing the layout by which station will be
	 * generated.
	 * @param maxStations The maximum number of stations.
	 * @throws DispatcherException
	 */
	public void startSimulation(float factor, Layout layout, int maxStations) throws DispatcherException {
		
		//TODO Implement main simulation method. should invoke the event generator, and start
		// pulling tasks from the queue until the queue is empty.
		
		if (null != this.eventGen){
			throw new DispatcherException("Can only start the dispatcher once...");
		}
		
		this.eventGen = EventGenerator.getInstance(factor,layout, maxStations);
		
		/*
		 * Generating the first event
		 */
		this.eventGen.generateEvent();
		
		while (true){
			
			while (tasksQueue.isEmpty()){
				this.currentVirtualTime++;
				this.eventGen.tick();
			}
			
			Event currentEvent = tasksQueue.poll();
			if (currentEvent.getClass().equals(StopEvent.class)){
				break;
			}
			
			//TODO: switch accroding to event type - handle topology event or execute message with neighbors list
			currentEvent.execute(this.nodes);
		}
	}
}
