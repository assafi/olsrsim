package events;

/**
 * @author olsr1
 *
 */
public abstract class MessageEvent extends Event {

	private long eventSource;
	
	/**
	 * @param src The originator of the current Message event
	 * @param time The Message timestamp
	 */
	public MessageEvent(long src, long time){
		super(time);
		this.eventSource = src;
	}
	
	/**
	 * @return The originator of the current Message event
	 */
	public long getSource() {
		return eventSource;
	}
}