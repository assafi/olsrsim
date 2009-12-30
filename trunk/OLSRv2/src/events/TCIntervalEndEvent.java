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

import java.util.Collection;


import protocol.IOLSRv2Protocol;

import topology.IStation;

/**
 * This event simulates that the TC Interval is passed
 * and the station should transmit another TC message.
 * 
 * @author Eli Nazarov
 *
 */
public class TCIntervalEndEvent extends IntervalEndEvent {
	
	/**
	 * @param time
	 */
	public TCIntervalEndEvent(String src, long time) {
		super(src, time);
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Object nodes) {
		logEvent();
		
		//we should receive only our-selves
		IStation station = (IStation)nodes;
		
		IOLSRv2Protocol olsrProtocol = station.getOLSRv2Protocol();
		olsrProtocol.tcIntervalTriger(this);
	}

}
