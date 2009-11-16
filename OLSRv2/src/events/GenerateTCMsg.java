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

}
