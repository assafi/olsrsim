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
package protocol.OLSRv2Protocol;

import protocol.OLSRv2Protocol.ProtocolDefinitions.ProtocolMprMpde;
import events.*;

/**
 * @author Asi
 *
 */
public interface IOLSRv2Protocol {
	public void start(ProtocolMprMpde mprMode);
	public void reciveHelloMessage(MessageEvent helloMsg);
	public void reciveTCMessage(MessageEvent tcMsg);
	public void reciveDataMessage(MessageEvent dataMsg);
	public void helloIntervalTriger(IntervalEndEvent helloTrigerMsg);
	public void tcIntervalTriger(IntervalEndEvent tcTrigerMsg);
}
