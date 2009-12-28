/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ItervalEndEvent.java
 * Author: Administrator
 * Date: Dec 28, 2009
 *
 */
package events;

/**
 * @author Eli Nazarov
 *
 */
public class IntervalEndEvent extends Event {

	private String eventSource;
	
	/**
	 * @param time
	 */
	public IntervalEndEvent(String src, long time) {
		super(time);
		this.eventSource = src;
	}
	
	/**
	 * @return The originator of the current Message event
	 */
	public String getSource() {
		return eventSource;
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.lang.Object)
	 */
	@Override
	public void execute(Object nodes) throws Exception {
	}

}
