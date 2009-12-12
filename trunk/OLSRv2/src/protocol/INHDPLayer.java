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
	public void receiveHelloMessage(HelloMessage helloMsg) throws ProtocolException;
	public HelloMessage generateHelloMessage(long currentSimTime);
}
