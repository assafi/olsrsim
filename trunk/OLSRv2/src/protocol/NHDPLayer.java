/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: NHDPLayer.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package protocol;

import protocol.InformationBases.LocalInformationBase;
import protocol.InformationBases.NeighborInformationBase;
import events.HelloMessage;

/**
 * @author Eli Nazarov
 *
 */
public class NHDPLayer implements INHDPLayer {

	/** NHDP Protocol information bases **/
	private LocalInformationBase localInfo = null;
	private NeighborInformationBase neighborInfo = null;
	
	public NHDPLayer(String stationID, 
					 LocalInformationBase localInfo,
					 NeighborInformationBase neighborInfo){
		this.localInfo = localInfo;
		this.neighborInfo = neighborInfo;
		
	}
	
	/* (non-Javadoc)
	 * @see protocol.INHDPLayer#receiveHelloMessage(events.HelloMessage)
	 */
	@Override
	public void receiveHelloMessage(HelloMessage helloMsg) throws ProtocolException {
		if (null == helloMsg){
			throw new ProtocolException("Wrong message!");
		}
		
		String msgSrc = helloMsg.getSource();
		
		if (neighborInfo)
	}

}
