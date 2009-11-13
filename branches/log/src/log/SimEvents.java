/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: SimEvents.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package log;

/**
 * @author Assaf
 * Simulator definitions that can be wrote down into the log.
 * This is basically a list of Events categories.
 */
public enum SimEvents {
	NODE_CREATE, NODE_DESTROY, NODE_MOVE, 
	HELLO_SENT, HELLO_REACH,
	TC_SENT, TC_REACH,
	DATA_SENT, DATA_REACH, DATA_LOSS //Can add more as needed
}
