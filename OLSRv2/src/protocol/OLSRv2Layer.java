/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: OLSRv2Layer.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package protocol;

import protocol.InformationBases.LocalInformationBase;
import protocol.InformationBases.NeighborInformationBase;
import protocol.InformationBases.NeighborProperty;
import events.HelloMessage;
import events.TCMessage;

/**
 * @author Eli Nazarov
 *
 */
public class OLSRv2Layer implements IOLSRv2Layer {

	private String stationID;
	
	/** OLSRv2 Protocol information bases **/
	//TODO See if this base is really needed
	private LocalInformationBase localInfo = null;
	private NeighborInformationBase neighborInfo = null;
	
	public OLSRv2Layer(String stationID, 
					 LocalInformationBase localInfo,
					 NeighborInformationBase neighborInfo){
		this.stationID = stationID;
		this.localInfo = localInfo;
		this.neighborInfo = neighborInfo;
		
		//TODO generate first TC message
	}
	
	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Layer#generateTCMessage()
	 */
	@Override
	public TCMessage generateTCMessage() {
		return null;
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Layer#receiveTCMessage(events.TCMessage)
	 */
	@Override
	public void receiveTCMessage(TCMessage tcMsg) throws ProtocolException {
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Layer#processHelloMessage(events.HelloMessage)
	 */
	@Override
	public void processHelloMessage(HelloMessage helloMsg) {
		NeighborProperty neighborProp = neighborInfo.getNeighborProperty(helloMsg.getSource());
		neighborProp.setWillingness(helloMsg.getWillingnes());
		
		// if the receiving station is selected as an MPR in the source station
		// update the mpr_selector flag in the neighbor set
		if (helloMsg.getNeighborSet().containsKey(stationID) && 
			helloMsg.getNeighborSet().get(stationID).isMpr()){
			neighborInfo.getNeighborProperty(helloMsg.getSource()).setMpr_selector(true);
		}
		else{ // otherwise set it to be false. Needed in case it was selected and now it is not
			neighborInfo.getNeighborProperty(helloMsg.getSource()).setMpr_selector(false);
		}
		
		
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Layer#helloMessageModification(events.HelloMessage)
	 */
	@Override
	public HelloMessage helloMessageModification(HelloMessage helloMsg) {
		helloMsg.setWillingnes(3); //TODO change it to be correct willingness
		return helloMsg;
	}

}
