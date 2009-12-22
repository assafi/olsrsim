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
		//TODO implement
	}

}
