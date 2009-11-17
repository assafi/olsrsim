package events;

/**
 * @author olsr1
 *
 */
public abstract class MessageEvent extends Event {

	private String eventSource;
	
	/**
	 * @param src The originator of the current Message event
	 * @param time The Message timestamp
	 */
	public MessageEvent(String src, long time){
		super(time);
		this.eventSource = src;
	}
	
	/**
	 * @return The originator of the current Message event
	 */
	public String getSource() {
		return eventSource;
	}
}
