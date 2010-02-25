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
package data;

/**
 * @author Assaf
 * Simulator definitions that can be wrote down into the log.
 * This is basically a list of Events categories.
 */
public enum SimEvents {
	NODE_CREATE, NODE_DESTROY, NODE_MOVE, 
	HELLO_SENT, HELLO_REACH,
	TC_SENT, TC_REACH, DATA_SENT_EVENT, BUSSY_MSG_IGNORED,
	DATA_SENT_FROM_SOURCE, DATA_REACHED_2_TARGET, DATA_NOT_SENT_NO_ROUTE , DATA_LOSS_NO_ROUTE, DATA_IGNORED_AT_RELAY, 
	DATA_SENT_FROM_RELAY, DATA_REACHED_2_RELAY, LOCAL_TARGET_NOT_PHYSIBLE,
	TTL_LIMIT_REACHED,
	SIM_END, LOG //Can add more as needed
}
