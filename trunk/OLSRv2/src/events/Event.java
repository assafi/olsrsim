package events;

/**
 * @author olsr1
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
}
 