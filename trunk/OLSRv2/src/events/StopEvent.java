/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: StopEvent.java
 * Author: Assaf
 * Date: Nov 15, 2009
 *
 */
package events;

import java.util.Hashtable;
import java.util.Map;

import data.SimEvents;
import data.SimLabels;
import dispatch.Dispatcher;

import log.Log;
import log.LogException;


/**
 * @author Assaf
 *
 */
public class StopEvent extends Event {

	/**
	 * Stop simulation now
	 */
	public StopEvent(){
		super(-1);
	}
	
	/**
	 * @param time Stop the simulation at custom Virtual time 
	 */
	public StopEvent(long time) {
		super(time);
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@Override
	public void execute(Object nodes) {
		logSimEnd();
	}
	
	private void logSimEnd() {
		Map<String,String> data = new Hashtable<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), 
				Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
		data.put(SimLabels.EVENT_TYPE.name(), SimEvents.SIM_END.name());
		try {
			Log.getInstance().writeDown(data);
		} catch (LogException e) {
			System.err.println(e.getMessage());
		}
	}
}
