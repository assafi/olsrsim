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
	
	public static GuiEventsQueue getInstance() {
		if(instance == null) {
			instance = new GuiEventsQueue();
		}
		return instance;
	}
	
	private GuiEventsQueue() {
		this.eventsQueue = new LinkedList<Event>();
	}
	
	public void addEvent(Event event) {
		this.eventsQueue.add(event);
	}
	
	public Event popEvent() throws Exception {
		if(this.eventsQueue.isEmpty()) {
			throw new Exception("Tried to retrieve an event from an empty queue");
		}
		return this.eventsQueue.remove();
	}
}
