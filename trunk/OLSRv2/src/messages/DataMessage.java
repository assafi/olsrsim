/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: DataMessage.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package messages;

import java.util.Collection;

import protocol.IOLSRv2Protocol;

import topology.IStation;

import events.MessageEvent;

/**
 * @author Eli Nazarov
 *
 */
public class DataMessage extends MessageEvent {

	/**
	 * @param src
	 * @param time
	 */
	public DataMessage(String src, long time) {
		super(src, time);
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@Override
	public void execute(Object nodes) {
		Collection<IStation> stations = (Collection<IStation>)nodes;
		for (IStation station : stations) {
			IOLSRv2Protocol olsrProtocol = station.getOLSRv2Protocol();
			olsrProtocol.reciveDataMessage(this);
		}
	}

}
