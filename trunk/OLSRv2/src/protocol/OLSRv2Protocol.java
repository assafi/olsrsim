/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: OLSRv2Protocol.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package protocol;

import events.DataMessage;
import events.HelloMessage;
import events.TCMessage;

/**
 * @author Eli Nazarov
 *
 */
public class OLSRv2Protocol implements IOLSRv2Protocol {

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveDataMessage(events.DataMessage)
	 */
	@Override
	public void reciveDataMessage(DataMessage dataMsg) {
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveHelloMessage(events.HelloMessage)
	 */
	@Override
	public void reciveHelloMessage(HelloMessage helloMsg) {
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveTCMessage(events.TCMessage)
	 */
	@Override
	public void reciveTCMessage(TCMessage tcMsg) {
	}

}
