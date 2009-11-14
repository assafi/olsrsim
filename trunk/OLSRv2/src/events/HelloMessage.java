/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: HelloMessage.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package events;

import protocol.Address;

/**
 * @author Eli Nazarov
 *
 */
public class HelloMessage extends MessageEvent {

	/**
	 * @param src
	 * @param time
	 */
	public HelloMessage(Address src, long time) {
		super(src, time);
	}

}
