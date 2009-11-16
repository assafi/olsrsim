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
package events;

import protocol.Address;

/**
 * @author Eli Nazarov
 *
 */
public class DataMessage extends MessageEvent {

	/**
	 * @param src
	 * @param time
	 */
	public DataMessage(Address src, long time) {
		super(src, time);
	}

}
