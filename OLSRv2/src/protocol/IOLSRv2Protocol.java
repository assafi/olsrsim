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
	public void reciveHelloMessage(MessageEvent helloMsg);
	public void reciveTCMessage(MessageEvent tcMsg);
	public void reciveDataMessage(MessageEvent dataMsg);
	public void helloIntervalTriger(MessageEvent helloTrigerMsg);
	public void tcIntervalTriger(MessageEvent tcTrigerMsg);
}
