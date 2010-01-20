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
package protocol.NHDPLayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import log.Log;
import log.LogException;
import main.SimulationParameters;
import messages.HelloMessage;

import data.SimEvents;
import data.SimLabels;
import dispatch.Dispatcher;
import events.HelloIntervalEndEvent;

import protocol.InformationBases.LocalInformationBase;
import protocol.InformationBases.NeighborInformationBase;
import protocol.InformationBases.NeighborProperty;
import protocol.OLSRv2Layer.IOLSRv2Layer;
import protocol.OLSRv2Protocol.ProtocolException;

/**
 * @author Eli Nazarov
 *
 */
public class NHDPLayer implements INHDPLayer {

	//TODO see how to remove all records with TTL expires
		
	private String stationID;
	//private long symTime;
	
	/** NHDP Protocol information bases **/
	//TODO See if this base is really needed
	private LocalInformationBase localInfo = null;
	private NeighborInformationBase neighborInfo = null;
	private IOLSRv2Layer olsrLayer = null;
	
	public NHDPLayer(String stationID, 
					 LocalInformationBase localInfo,
					 NeighborInformationBase neighborInfo,
					 IOLSRv2Layer olsrLayer){
		Dispatcher dispatcher = Dispatcher.getInstance();
		this.stationID = stationID;
		this.localInfo = localInfo;
		this.neighborInfo = neighborInfo;
		this.olsrLayer = olsrLayer;
		
		// calculate jitter for the first Hello Interval event
		int rand = new Random().nextInt(SimulationParameters.HelloInterval);
		long jitter = rand / SimulationParameters.maxStations;
		
		dispatcher.pushEvent(generateHelloMessage(dispatcher.getCurrentVirtualTime() + jitter));
		dispatcher.pushEvent(new HelloIntervalEndEvent(stationID, dispatcher.getCurrentVirtualTime() + SimulationParameters.HelloInterval + jitter));
	}
	
	/* (non-Javadoc)
	 * @see protocol.INHDPLayer#receiveHelloMessage(events.HelloMessage)
	 */
	@Override
	public boolean receiveHelloMessage(HelloMessage helloMsg) throws ProtocolException {
		if (null == helloMsg){
			throw new ProtocolException("Wrong message!");
		}
		
		
//		//TODO remove
//		if (stationID.equals("2")){
//			String logDetails = new String("Hello neighbors");
//
//			for (String name : helloMsg.getNeighborSet().keySet()) {
//				logDetails += " " + name;
//			}
//			
//			Log log = Log.getInstance();
//			HashMap<String, String> data = new HashMap<String, String>();
//			data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
//			data.put(SimLabels.NODE_ID.name(), stationID);
//			data.put(SimLabels.EVENT_TYPE.name(), SimEvents.LOG.name());
//			data.put(SimLabels.DETAILS.name(), logDetails);
//			try {
//				log.writeDown(data);
//			} catch (LogException le) {
//				System.out.println(le.getMessage());
//			}
//		}
//		////////////////////////////////////////////////////////
		
		
		// in case we have new symmetric 1-hop neighbor or new 2-hop neighbor 
		// we should notify so that OLSRv2 layer can recalculate the MPRs
		boolean newSemmerticOr2hop = false;
		
		Dispatcher dispatcher = Dispatcher.getInstance();
		
		String msgSrc = helloMsg.getSource();
		long simTime = helloMsg.getTime();
		
		/* Update the neighbor set */
		NeighborProperty property = new NeighborProperty();
		
		if (!neighborInfo.is1HopNeighbor(msgSrc)){
			if (helloMsg.getNeighborSet().containsKey(stationID)){// if the receiving station is a neighbor then this is a symmetric connection 
				property.setQuality(helloMsg.getNeighborSet().get(stationID).getQuality());
				property.setSymetricLink(true);
				newSemmerticOr2hop = true;
				property.setValideTime(simTime + SimulationParameters.entryValidPeriod); 
				//TODO insert event that the validity time has passed or check in other ways
				//dispatcher.pushEvent(new );
				neighborInfo.addNeighbor(msgSrc, property);
			}
			else{
				property.setQuality(1);/*TODO calculate the quality*/
				property.setSymetricLink(false);
				property.setValideTime(simTime + SimulationParameters.entryValidPeriod);
				neighborInfo.addNeighbor(msgSrc, property);
				//TODO insert event that the validity time has passed or check in other ways
			}
			
			// if in the past this neighbor was my 2-hop neighbor
			// and now became a 1-hop we should remove it from the 2-hop set
			if (neighborInfo.is2HopNeighbor(msgSrc)){
				neighborInfo.remove2hoptNeighbor(msgSrc);
			}
		}else{
			// update the validity time of the entry
			neighborInfo.getNeighborProperty(msgSrc).setValideTime(simTime + SimulationParameters.entryValidPeriod);
		}
		
		/* Update the lost neighbor set */
		Set<String> lostHelloNeigbors = helloMsg.getLostNeighborSet().keySet();
		Iterator<String> it = lostHelloNeigbors.iterator();
		boolean sendHelloMsg = false;
		while (it.hasNext()){
			String lostNeighbor = it.next();
			if (neighborInfo.is1HopNeighbor(lostNeighbor)){
				neighborInfo.addToLostNeighbors(lostNeighbor, simTime + SimulationParameters.entryValidPeriod);
				neighborInfo.removeNeighbor(lostNeighbor);
				//if this was a symmetric neighbor must sent HELLO message
				NeighborProperty lostNeighborProp = neighborInfo.getNeighborProperty(lostNeighbor);
				if (lostNeighborProp.isSymetricLink()){
					sendHelloMsg = true;
				}
			}
		}
		
		//if this was a symmetric neighbor must sent HELLO message
		if (sendHelloMsg){
			generateHelloMessage(simTime);
			newSemmerticOr2hop = true;
		}
		
		/* Update the 2-hop neighbor set */
		Set<String> helloNeigbors = helloMsg.getNeighborSet().keySet();
		it = helloNeigbors.iterator();
		while (it.hasNext()){
			String secondHopNeighbor = it.next();
			// if not my 1-hop neighbor and not me
			if (!neighborInfo.is1HopNeighbor(secondHopNeighbor) && !secondHopNeighbor.equals(stationID)){
				// If this neighbor already is in 2-hop set the 1-hop neighbor
				// that we got the Hello message from will be added to the
				// list of 1-hop nodes that we can reach it from
				neighborInfo.add2HopNeighbor(secondHopNeighbor, msgSrc);
				newSemmerticOr2hop = true;
			}
		}
		
		return newSemmerticOr2hop;
	}

	/* (non-Javadoc)
	 * @see protocol.INHDPLayer#generateHelloMessage()
	 */
	@Override
	public HelloMessage generateHelloMessage(long currentSimTime) {
		// Create new Hello message
		HelloMessage msg = new HelloMessage(stationID, currentSimTime + SimulationParameters.transmitionTime, neighborInfo.getAllNeighbors(), neighborInfo.getAllLostNeighborSet());
		// Pass it to OLSR layer for update
		msg = olsrLayer.helloMessageModification(msg);
		return msg;
	}

}
