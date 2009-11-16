/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: IOLSRv2Protocol.java
 * Author: Asi
 * Date: 15/11/2009
 *
 */
package protocol;

import events.*;

/**
 * @author Asi
 *
 */
public interface IOLSRv2Protocol {
	public void reciveHelloMessage(HelloMessage helloMsg);
	public void reciveTCMessage(TCMessage tcMsg);
	public void reciveDataMessage(DataMessage dataMsg);
}
