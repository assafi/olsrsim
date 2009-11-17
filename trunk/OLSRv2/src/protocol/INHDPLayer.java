/**
 * This Interface exposes the functionality
 */
package protocol;

import events.HelloMessage;

/**
 * @author Eli
 *
 */
public interface INHDPLayer {
	/**
	 * The Interval that the Hello messages should be generated
	 * periodically. 
	 */
	static final int HelloInterval = 10; //TODO see that this is a correct interval
	
	public void receiveHelloMessage(HelloMessage helloMsg) throws ProtocolException;
	public HelloMessage generateHelloMessage();
}
