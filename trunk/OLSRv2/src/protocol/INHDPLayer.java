/**
 * This Interface exposes the functionality
 */
package protocol;

import events.HelloMessage;

/**
 * @author Eli Nazarov
 *
 */
public interface INHDPLayer {
	/**
	 * The Interval that the Hello messages should be generated
	 * periodically. 
	 */
	static final int HelloInterval = 100; //TODO see that this is a correct interval
	
	public void receiveHelloMessage(HelloMessage helloMsg) throws ProtocolException;
	public HelloMessage generateHelloMessage(long currentSimTime);
}
