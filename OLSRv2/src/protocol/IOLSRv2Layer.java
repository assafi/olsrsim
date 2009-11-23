/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: IOLSRv2Layer.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package protocol;

import events.HelloMessage;
import events.TCMessage;;

/**
 * @author Eli Nazarov
 *
 */
public interface IOLSRv2Layer {
	/**
	 * The Interval that the Hello messages should be generated
	 * periodically. 
	 */
	static final int TCInterval = 10; //TODO see that this is a correct interval
	
	public void receiveTCMessage(TCMessage tcMsg) throws ProtocolException;
	public TCMessage generateTCMessage();
	public void processHelloMessage(HelloMessage helloMsg);
	public HelloMessage helloMessageModification(HelloMessage helloMsg);
}
