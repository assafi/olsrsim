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
package protocol.OLSRv2Layer;

import messages.HelloMessage;
import messages.TCMessage;
import protocol.ProtocolException;

/**
 * @author Eli Nazarov
 *
 */
public interface IOLSRv2Layer {
	public void receiveTCMessage(TCMessage tcMsg) throws ProtocolException;
	
	/**
	 * Generates TC message and sends it, 
	 * Pushes it into the dispatcher
	 * 
	 * @param currentSimTime
	 * @return Generated TC Message
	 */
	public TCMessage generateTCMessage(long currentSimTime);
	public void processHelloMessage(HelloMessage helloMsg);
	public HelloMessage helloMessageModification(HelloMessage helloMsg);
	public void calculateMPRs();
}
