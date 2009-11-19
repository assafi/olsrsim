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

import java.util.Map;

import topology.IStation;

/**
 * @author Assaf
 *
 */
public abstract class Event {
	private long time;
	
	/**
	 * @param time
	 */
	public Event(long time){
		this.time = time;
	}

	/**
	 * @return the time of an event
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param nodes The nodes map
	 */
	public abstract void execute(Map<String, IStation> nodes);
}
 