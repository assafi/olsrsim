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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import protocol.InformationBases.LocalInformationBase;
import protocol.InformationBases.NeighborInformationBase;
import protocol.InformationBases.NeighborProperty;
import events.HelloMessage;

/**
 * @author Eli Nazarov
 *
 */
public class NHDPLayer implements INHDPLayer {

	private String stationID;
	private long symTime;
	
	/**
	 * The validity period of the entry in the table 
	 */
	static final int EntryValidPeriod = 10;
	
	/** NHDP Protocol information bases **/
	//TODO See if this base is really needed
	private LocalInformationBase localInfo = null;
	private NeighborInformationBase neighborInfo = null;
	
	public NHDPLayer(String stationID, 
					 LocalInformationBase localInfo,
					 NeighborInformationBase neighborInfo,
					 int symTime){
		this.stationID = stationID;
		this.localInfo = localInfo;
		this.neighborInfo = neighborInfo;
		this.symTime = symTime;
		
		//TODO generate first hello message
	}
	
	/* (non-Javadoc)
	 * @see protocol.INHDPLayer#receiveHelloMessage(events.HelloMessage)
	 */
	@Override
	public void receiveHelloMessage(HelloMessage helloMsg) throws ProtocolException {
		if (null == helloMsg){
			throw new ProtocolException("Wrong message!");
		}
		
		//Update the protocol time to be the current time according to the 
		// Hello message
		if (symTime < helloMsg.getTime()){
			symTime = helloMsg.getTime();
		}
	
		String msgSrc = helloMsg.getSource();
		
		/* Update the neighbor set */
		NeighborProperty property = new NeighborProperty();
		
		if (!neighborInfo.isNeighbor(msgSrc)){
			if (helloMsg.getNeighborSet().containsKey(stationID)){
				property.setQuality(helloMsg.getNeighborSet().get(stationID).getQuality());
				property.setSymetricLink(true);
				property.setValideTime(symTime + EntryValidPeriod); //TODO insert event that the validity time has passed or check in other ways
				neighborInfo.addNeighbor(msgSrc, property);
			}
			else{
				property.setQuality(1);/*TODO calculate the quality*/
				property.setSymetricLink(true);
				property.setValideTime(symTime + EntryValidPeriod);
				neighborInfo.addNeighbor(msgSrc, property);
				//TODO insert event that the validity time has passed or check in other ways
			}
			
			// if in the past this neighbor was my 2-hop neighbor
			// and now became a 1-hop we should remove it from the 2-hop set
			if (neighborInfo.is2HopNeighbor(msgSrc)){
				neighborInfo.remove2hoptNeighbor(msgSrc);
			}
		}
		
		/* Update the lost neighbor set */
		Set<String> lostHelloNeigbors = helloMsg.getLostNeighborSet().keySet();
		Iterator<String> it = lostHelloNeigbors.iterator();
		String lostNeighbor = it.next();
		while (it.hasNext()){
			if (neighborInfo.isNeighbor(lostNeighbor)){
				neighborInfo.addToLostNeighbors(lostNeighbor, symTime + EntryValidPeriod);
				neighborInfo.removeNeighbor(lostNeighbor);
				//TODO if this was a symmetric neighbor must sent HELLO message
			}
		}
		
		/* Update the 2-hop neighbor set */
		Set<String> helloNeigbors = helloMsg.getNeighborSet().keySet();
		it = lostHelloNeigbors.iterator();
		String secondHopNeighbor = it.next();
		while (it.hasNext()){
			if (!neighborInfo.isNeighbor(secondHopNeighbor)){
				// If this neighbor already is in 2-hop set the 1-hop neighbor
				// that we got the Hello message from will be added to the
				// list of 1-hop nodes that we can reach it from
				neighborInfo.add2HopNeighbor(secondHopNeighbor, msgSrc);
			}
		}
	}

	/* (non-Javadoc)
	 * @see protocol.INHDPLayer#generateHelloMessage()
	 */
	@Override
	public HelloMessage generateHelloMessage() {
		//TODO see if the time is ok
		HelloMessage msg = new HelloMessage(stationID, symTime + 100 /*TODO change*/, neighborInfo.getAllNeighbors(), neighborInfo.getAllLostNeighborSet());
		return msg;
	}

}
