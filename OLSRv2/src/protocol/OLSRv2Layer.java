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
import events.TCMessage;

/**
 * @author Eli Nazarov
 *
 */
public class OLSRv2Layer implements IOLSRv2Layer {

	private String stationID;
	private long symTime;
	
	/**
	 * The validity period of the entry in the table 
	 */
	static final int EntryValidPeriod = 10;
	
	/** OLSRv2 Protocol information bases **/
	//TODO See if this base is really needed
	private LocalInformationBase localInfo = null;
	private NeighborInformationBase neighborInfo = null;
	
	public OLSRv2Layer(String stationID, 
					 LocalInformationBase localInfo,
					 NeighborInformationBase neighborInfo,
					 int symTime){
		this.stationID = stationID;
		this.localInfo = localInfo;
		this.neighborInfo = neighborInfo;
		this.symTime = symTime;
		
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

}
