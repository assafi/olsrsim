/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: GenerateTCMsg.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package events;

import java.util.Map;

import topology.IStation;

/**
 * This event simulates that the TC Interval is passed
 * and the station should transmit another TC message.
 * 
 * @author Eli Nazarov
 *
 */
public class GenerateTCMsg extends Event {

	private String destination;
	
	/**
	 * @param time
	 */
	public GenerateTCMsg(long time) {
		super(time);
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@Override
	public void execute(Map<String, IStation> nodes) {
		//TODO implement
	}

}
