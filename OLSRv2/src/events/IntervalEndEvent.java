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

import java.util.HashMap;

import log.Log;
import log.LogException;
import data.SimLabels;

/**
 * @author Eli Nazarov
 *
 */
public class IntervalEndEvent extends Event {

	private String eventSource;
	protected String eventType = null;
	
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

	public void logEvent(String localDst) {
		Log log = Log.getInstance();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(this.getTime()));
		data.put(SimLabels.NODE_ID.name(), localDst);
		data.put(SimLabels.EVENT_TYPE.name(), eventType );
		data.put(SimLabels.LOCAL_SOURCE.name(), eventSource);
		data.put(SimLabels.LOCAL_TARGET.name(), localDst);
		try {
			log.writeDown(data);
		} catch (LogException le) {
			System.out.println(le.getMessage());
		}
	}
}
