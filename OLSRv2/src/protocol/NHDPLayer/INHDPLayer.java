/**
 * This Interface exposes the functionality
 */
package protocol.NHDPLayer;

import messages.HelloMessage;
import protocol.OLSRv2Protocol.ProtocolException;

/**
 * @author Eli Nazarov
 *
 */
public interface INHDPLayer {
	public boolean receiveHelloMessage(HelloMessage helloMsg) throws ProtocolException;
	public HelloMessage generateHelloMessage(long currentSimTime);
}
