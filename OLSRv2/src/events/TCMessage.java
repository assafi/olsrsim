/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TCMessage.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package events;



/**
 * @author Eli Nazarov
 *
 */
public class TCMessage extends MessageEvent {

	/**
	 * @param src
	 * @param time
	 */
	public TCMessage(String src, long time) {
		super(src, time);
	}


}
