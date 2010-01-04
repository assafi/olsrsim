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
 