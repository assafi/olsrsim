/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: GenerateHelloMsg.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package events;

import java.util.Collection;


import protocol.IOLSRv2Protocol;

import topology.IStation;

/**
 * This event simulates that the Hello Interval is passed
 * and the station should transmit another Hello message.
 * 
 * @author Eli Nazarov
 *
 */
public class HelloIntervalEndEvent extends IntervalEndEvent {
	
	public HelloIntervalEndEvent(String src, long time) {
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
		olsrProtocol.helloIntervalTriger(this);
	}

}
