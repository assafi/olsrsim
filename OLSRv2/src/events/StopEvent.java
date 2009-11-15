/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: StopEvent.java
 * Author: Assaf
 * Date: Nov 15, 2009
 *
 */
package events;

/**
 * @author Assaf
 *
 */
public class StopEvent extends Event {

	/**
	 * Stop simulation now
	 */
	public StopEvent(){
		super(-1);
	}
	
	/**
	 * @param time Stop the simulation at custom Virtual time 
	 */
	public StopEvent(long time) {
		super(time);
	}
}
