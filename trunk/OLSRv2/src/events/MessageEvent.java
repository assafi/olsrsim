package events;

import protocol.Address;

/**
 * @author olsr1
 *
 */
public abstract class MessageEvent extends Event {

	private Address eventSource;
	
	/**
	 * @param src The originator of the current Message event
	 * @param time The Message timestamp
	 */
	public MessageEvent(Address src, long time){
		super(time);
		this.eventSource = src;
	}
	
	/**
	 * @return The originator of the current Message event
	 */
	public Address getSource() {
		return eventSource;
	}
}
