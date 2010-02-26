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
package events;

import java.util.UUID;

/**
 * @author Assaf
 *
 */
public abstract class Event {
	private long time;
	private UUID id;
	
	/**
	 * @param time
	 */
	public Event(long time){
		this.time = time;
		this.id = UUID.randomUUID();
	}

	/**
	 * @return the time of an event
	 */
	public long getTime() {
		return time;
	}
	
	public UUID getID() {
		return id;
	}
	/**
	 * @param time set the event time
	 */
	public void setTime(long time) {
		this.time = time;
	}
	/**
	 *
	 */
	public void updateTime(long time) {
		this.time = time;
	}

	/**
	 * @param nodes The nodes map
	 * @throws Exception 
	 */
	public abstract void execute(Object nodes) throws Exception;
}
 