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
package messages;

import java.util.Collection;

import events.Event;
import events.MessageEvent;

import protocol.IOLSRv2Protocol;

import topology.IStation;

/**
 * This event simulates that the Hello Interval is passed
 * and the station should transmit another Hello message.
 * 
 * @author Eli Nazarov
 *
 */
public class GenerateHelloMsg extends MessageEvent {

	private String destination;
	
	public GenerateHelloMsg(String src, long time) {
		super(src, time);
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Object nodes) {
		//TODO see if this is correct. maybe we should pass to Ctor the station
		//	   and run helloIntervalTriger only on her
		
		Collection<IStation> stations = (Collection<IStation>)nodes;
		for (IStation station : stations) {
			IOLSRv2Protocol olsrProtocol = station.getOLSRv2Protocol();
			olsrProtocol.helloIntervalTriger(this);
		}
	}

}
