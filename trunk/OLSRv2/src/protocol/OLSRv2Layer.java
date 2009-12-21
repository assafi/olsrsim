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

import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dispatch.Dispatcher;

import protocol.InformationBases.LocalInformationBase;
import protocol.InformationBases.NeighborInformationBase;
import protocol.InformationBases.NeighborProperty;
import protocol.InformationBases.ReceivedMessageInformationBase;
import protocol.InformationBases.ReceivedSetData;
import protocol.InformationBases.TopologyCommonData;
import protocol.InformationBases.TopologyInformationBase;
import protocol.InformationBases.TopologySetData;
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
	
	private TopologyInformationBase topologyInfo = null;
	private ReceivedMessageInformationBase receivedMsgInfo = null;
	
	public OLSRv2Layer(String stationID, 
					 LocalInformationBase localInfo,
					 NeighborInformationBase neighborInfo,
					 TopologyInformationBase topologyInfo,
					 ReceivedMessageInformationBase receiveMsgInfo){
		this.stationID = stationID;
		this.localInfo = localInfo;
		this.neighborInfo = neighborInfo;
		this.topologyInfo = topologyInfo;
		this.receivedMsgInfo = receiveMsgInfo;
		
		//TODO generate first TC message
	}
	
	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Layer#generateTCMessage()
	 */
	@Override
	public TCMessage generateTCMessage(long currentSimTime) {
		TCMessage tcMsg = new TCMessage(stationID, currentSimTime + ProtocolDefinitions.TCInterval, neighborInfo.getAllNeighbors());
		Dispatcher dispatcher = Dispatcher.getInstance();
		dispatcher.pushEvent(tcMsg);
		return tcMsg;
	}

	private void updateRoutingSet(){
		//TODO implement
	}

	/**
	 * This method should send the TC message it received to all it's MPRs
	 */
	private void floodTCMsg(TCMessage tcMsg){
		/* Go over all the 1-hop neighbors that are selected as 
		   MPRs and send the message to them */
		Set<String> neighbors = neighborInfo.getAllNeighbors().keySet();
		for (String neighbor : neighbors) {
			NeighborProperty neighborProp = neighborInfo.getAllNeighbors().get(neighbor);
			if (neighborProp.isMpr()){
				Dispatcher dispatcher = Dispatcher.getInstance();
				dispatcher.pushEvent(tcMsg);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Layer#receiveTCMessage(events.TCMessage)
	 */
	@Override
	public void receiveTCMessage(TCMessage tcMsg) throws ProtocolException {
		
		/* We should update the following bases
		 * 1. Update bases according to the TC message: (see OLSR rfc section 12.3)
		 *  1.1. Populating the Advertising Remote Router Set - advertisingRemoteRouterSet in Topology Base
		 * 	     add a new tuple if the originator doesn't exist 
		 *  1.2. Populating the Router Topology Set - topologySet in Topology base
		 *       add new entry or update if existing.
		 * 
		 * 2. Update the routingSet in Topology Base
		 *    (see OLSR rfc section 13.6 and 15)
		 *    
		 * 3. We should check if this message was already farwerded in the Receive Message
		 *    base and if yes skip step 5.
		 *   
		 * 4. We should check if TC Message was received from MPR selector
		 *    and if yes flood this message to all my MPRs
		 * 
		 */
		
		
		String src = tcMsg.getSource();
		
		/*
		 * First we should check if the message was already processed 
		 */
		
		if (receivedMsgInfo.getReceivedSet().containsKey(src)){
			// each address is mapped to a list of messages it sent
			ArrayList<ReceivedSetData> receviedMsgList = receivedMsgInfo.getReceivedSet().values().iterator().next();
			for (ReceivedSetData receivedMsg : receviedMsgList) {
				if (MessegeTypes.TC == receivedMsg.getType() && receivedMsg.getMsgHashCode() == tcMsg.hashCode()){
					// if we found that the massage was already received we shouldn't process it
					return;
				}
			}
		}
		else{ // Add the TC message to the Received TC messages base
			
			// add new entry for the originator the Receive set
			// because this is the first time we receive a message from
			// this originator we must allocate new receive message list.
			ArrayList<ReceivedSetData> msgList = new ArrayList<ReceivedSetData>();
			msgList.add(new ReceivedSetData(MessegeTypes.TC, tcMsg.hashCode(), tcMsg.getTime() + ProtocolDefinitions.EntryValidPeriod));
			
			receivedMsgInfo.getReceivedSet().put(src, msgList);
		}
		
		// 1.1 Populating the Advertising Remote Router Set
		if (!topologyInfo.getAdvertisingRemoteRouterSet().containsKey(src)){
			TopologyCommonData entryData = new TopologyCommonData(tcMsg.getTime() + ProtocolDefinitions.EntryValidPeriod);
			topologyInfo.getAdvertisingRemoteRouterSet().put(src, entryData);
		}
		
		//1.2 Populating the Router Topology Set
		
		// get MPR selectors from the 
		Set<String>  mprSelectorsInTcMsg = tcMsg.getMprSelectors().keySet();
		
		//if this station is not in the Topology Set
		if (!topologyInfo.getTopologySet().containsKey(src)){
			TopologySetData entryData = new TopologySetData(tcMsg.getTime() + ProtocolDefinitions.EntryValidPeriod);
			
			for (String mprSelector : mprSelectorsInTcMsg) {
				entryData.addToAddress(mprSelector);
			}
			
			topologyInfo.addTopologyEntry(src, entryData);
		}
		else{ // if this station already exists in the entry.
			TopologySetData entryData = topologyInfo.getTopologySet().get(src);
			
			for (String mprSelector : mprSelectorsInTcMsg) {
				entryData.addToAddress(mprSelector);
			}
			
			// update validity time of the entry
			entryData.setTTL(tcMsg.getTime() + ProtocolDefinitions.EntryValidPeriod);
		}
		
		// 2. Update the routingSet in Topology Base
		updateRoutingSet();
		
		// 3. Forward the tcMsg to all my MPRs unless it was already forwarded
		if (!receivedMsgInfo.getForwardSet().containsKey(src)){
			floodTCMsg(tcMsg);// flood message
			
			// add new entry for the originator the Forward set
			// because this is the first time we forward a message from
			// this originator we must allocate new message list.
			ArrayList<ReceivedSetData> msgList = new ArrayList<ReceivedSetData>();
			msgList.add(new ReceivedSetData(MessegeTypes.TC, tcMsg.hashCode(), tcMsg.getTime() + ProtocolDefinitions.EntryValidPeriod));
			
			receivedMsgInfo.getForwardSet().put(src, msgList);
		}
		else{
			ArrayList<ReceivedSetData> forwardMsgList = receivedMsgInfo.getForwardSet().values().iterator().next();
			boolean floodMsg = true;
			for (ReceivedSetData receivedMsg : forwardMsgList) {
				if (MessegeTypes.TC == receivedMsg.getType() && receivedMsg.getMsgHashCode() == tcMsg.hashCode()){
					// if we found that the massage was already forwarded we shouldn't flood it
					floodMsg = false;
					break;
				}
			}
			// if the hash-code of the massage is not in the forward 
			// list of the originator we must flood the TC message,
			// and add it to the list.
			if (floodMsg){
				floodTCMsg(tcMsg);// flood message
				
				// add to the list
				forwardMsgList.add(new ReceivedSetData(MessegeTypes.TC, tcMsg.hashCode(), tcMsg.getTime() + ProtocolDefinitions.EntryValidPeriod));
			}
			
		}	
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
			
			//because the MPR selector set is changed we must send a TC message
			//TODO see how this settels with minimum time before trunsmition
			//     and if the interval of TC is passed or near to finish
			generateTCMessage(helloMsg.getTime());
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

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Layer#calculateMPRs()
	 */
	@Override
	public void calculateMPRs() {
		/*    This function should be called when:
		 *    If 1-hop symmetric neighbor is added we must recalculate the MPRs
		 *    If 2-hop neighbor is added/removed we must recalculate the MPRs
		 *    (see OLSR rfc section 13.5 and 14)
		 */    
		//TODO Use the willingness of a station to be a MPR when calculating the MPR set
		
		
	}

}
