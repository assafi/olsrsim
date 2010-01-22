/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: EventsQueue.java
 * Author: Asi
 * Date: 22/12/2009
 *
 */
package gui;

import java.util.LinkedList;

import events.Event;

/**
 * @author Asi
 *
 */
public class GuiEventsQueue {
	private static GuiEventsQueue instance = null;
	private LinkedList<Event> eventsQueue;
	
	/**
	 * @return An instance of GuiEventsQueue
	 */
	public static GuiEventsQueue getInstance() {
		if(instance == null) {
			instance = new GuiEventsQueue();
		}
		return instance;
	}
	
	private GuiEventsQueue() {
		this.eventsQueue = new LinkedList<Event>();
	}
	
	/**
	 * Non blocking method that adds an event to the queue.
	 * @param event
	 */
	public synchronized void addEvent(Event event) {
		this.eventsQueue.add(event);
		this.notify();
	}
	
	/**
	 * This method is a blocking method, will be released when an event is added to the queue.
	 * @return The next event
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	public synchronized Event popEvent() throws InterruptedException {
		while(eventsQueue.isEmpty()) {
			this.wait();
		}
		return this.eventsQueue.remove();
	}
	
}
