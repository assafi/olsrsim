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
	public void receiveHelloMessage(HelloMessage helloMsg) throws ProtocolException;
}
