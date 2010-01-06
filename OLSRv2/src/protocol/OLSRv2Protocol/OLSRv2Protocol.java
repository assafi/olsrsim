/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: OLSRv2Protocol.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package protocol.OLSRv2Protocol;

import java.util.HashMap;

import dispatch.Dispatcher;
import messages.DataMessage;
import messages.HelloMessage;
import messages.TCMessage;
import protocol.InformationBases.LocalInformationBase;
import protocol.InformationBases.NeighborInformationBase;
import protocol.InformationBases.ReceivedMessageInformationBase;
import protocol.InformationBases.RoutingSetData;
import protocol.InformationBases.TopologyInformationBase;
import protocol.NHDPLayer.INHDPLayer;
import protocol.NHDPLayer.NHDPLayer;
import protocol.OLSRv2Layer.IOLSRv2Layer;
import protocol.OLSRv2Layer.OLSRv2Layer;
import protocol.OLSRv2Protocol.ProtocolDefinitions.ProtocolMprMpde;
import events.HelloIntervalEndEvent;
import events.IntervalEndEvent;
import events.MessageEvent;

/**
 * @author Eli Nazarov
 *
 */
public class OLSRv2Protocol implements IOLSRv2Protocol {

	private String stationID;
	
	/** Protocol information bases **/
	
	/* NHDP layer information bases */
	private LocalInformationBase localInfo = null;
	private NeighborInformationBase neighborInfo = null;
	
	/* OLSRv2 layer information bases */
	private TopologyInformationBase topologyInfo = null;
	private ReceivedMessageInformationBase receivedMsgInfo = null;
	
	/** Protocol layers **/
	private IOLSRv2Layer olsrLayer = null;
	private INHDPLayer nhdpLayer = null;	
	
	public OLSRv2Protocol(String stationID){
		this.stationID = stationID;
		
		// allocation of the information bases
		this.localInfo = new LocalInformationBase();
		this.neighborInfo = new NeighborInformationBase();
		this.receivedMsgInfo = new ReceivedMessageInformationBase();
		this.topologyInfo = new TopologyInformationBase();
	}
	
	/*
	 * This function removes all entries in the
	 * tables that their TTL has been expired
	 */
	private void cleanExpiredSetEntries(){
		long currTime = Dispatcher.getInstance().getCurrentVirtualTime();
		neighborInfo.clearExpiredEntries(currTime);
		receivedMsgInfo.clearExpiredEntries(currTime);
		topologyInfo.clearExpiredEntries(currTime);
	}
	
	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#start()
	 */
	@Override
	public void start(ProtocolMprMpde mprMode) {
		this.olsrLayer = new OLSRv2Layer(stationID, localInfo, neighborInfo, topologyInfo, receivedMsgInfo, mprMode);
		this.nhdpLayer = new NHDPLayer(stationID, localInfo, neighborInfo, this.olsrLayer);
	}
	
	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#helloIntervalTriger()
	 */
	@Override
	public void helloIntervalTriger(IntervalEndEvent helloTrigerMsg) {
		/* when we recive an event that the hello interval is over
		we should generate hello massage and insert GenerateHelloMsg
		event so that we will know next time that the interval is over
		Impotant: the distination should be us meaning src=dst
		TODO: See that when a dispacher procceses this event only I get it
		*/
		
		//clear the sets of invalid entries
		cleanExpiredSetEntries();
		
		//Create the messages
		HelloIntervalEndEvent nexTriger = new HelloIntervalEndEvent(stationID, helloTrigerMsg.getTime() + ProtocolDefinitions.HelloInterval);
		HelloMessage newHelloMsg = nhdpLayer.generateHelloMessage(helloTrigerMsg.getTime());
		
		// Send them to the dispatcher
		Dispatcher dispacher = Dispatcher.getInstance();
		dispacher.pushEvent(newHelloMsg);
		dispacher.pushEvent(nexTriger);
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveDataMessage(events.MessageEvent)
	 */
	@Override
	public void reciveDataMessage(MessageEvent dataMsg) {
		DataMessage msg  = (DataMessage)dataMsg;
		Dispatcher dispatcher = Dispatcher.getInstance();
		
		if (msg.getLocalDst().equals(stationID)){// check if this message is for me
			if(msg.getGlobalDst().equals(stationID)){
				//we got the message!!!
				//TODO write to log that finished
			}
			else{
				if(neighborInfo.is1HopNeighbor(msg.getGlobalDst())){
					// if the destination is my neighbor send him the message
					msg.setLocalSrc(stationID);
					msg.setLocalDst(msg.getGlobalDst());
					msg.updateTime(dispatcher.getCurrentVirtualTime() + ProtocolDefinitions.Delta);
					dispatcher.pushEvent(msg);
				}
				else{
					/*
					 *  the destination is not our neighbor 
					 *  -> we should send to the next node in the route 
					 */
					
					HashMap<String, RoutingSetData>  routingSet =  topologyInfo.getRoutingSet();
					if (!routingSet.containsKey(msg.getGlobalDst())){
						//TODO log error: we can't fined the destination station or next hop
						return;
					}
					
					RoutingSetData entryData = routingSet.get(msg.getGlobalDst());
					msg.setLocalDst(entryData.getNextHop());
					msg.updateTime(dispatcher.getCurrentVirtualTime() + ProtocolDefinitions.Delta);
					dispatcher.pushEvent(msg);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveHelloMessage(events.MessageEvent)
	 */
	@Override
	public void reciveHelloMessage(MessageEvent helloMsg) {
		/* if NHDP layer returns true after proccesing meaning that there
		 * was a new 1-jop symmetric neighbor added or 2-hop neighbor
		 * we must invoke the recalculation of MPRs */
		
		//clear the sets of invalid entries
		cleanExpiredSetEntries();
		
		try {
			if (HelloMessage.class.isAssignableFrom(helloMsg.getClass())){
				if (nhdpLayer.receiveHelloMessage((HelloMessage)helloMsg)){
					//if we need to calculate MPR sets do so 
					olsrLayer.calculateMPRs();
				}
			}
		} catch (ProtocolException e) {
			//Shouldn't fail
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveTCMessage(events.MessageEvent)
	 */
	@Override
	public void reciveTCMessage(MessageEvent tcMsg) {
		
		//clear the sets of invalid entries
		cleanExpiredSetEntries();
		
		try {
			if (TCMessage.class.isAssignableFrom(tcMsg.getClass())){
				olsrLayer.receiveTCMessage((TCMessage)tcMsg);
			}
		} catch (ProtocolException e) {
			//Shouldn't fail
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#tcIntervalTriger()
	 */
	@Override
	public void tcIntervalTriger(IntervalEndEvent tcTrigerMsg) {
		/* when we receive an event that the TC interval is over
		we should generate hello massage and insert GenerateTCMsg
		event so that we will know next time that the interval is over
		here we must send a new TC Message*/
		
		//clear the sets of invalid entries
		cleanExpiredSetEntries();
		
		//Create the messages
		HelloIntervalEndEvent nexTriger = new HelloIntervalEndEvent(stationID, tcTrigerMsg.getTime() + ProtocolDefinitions.TCInterval);
		TCMessage newTCMsg = olsrLayer.generateTCMessage(tcTrigerMsg.getTime());
		
		// Send them to the dispatcher
		Dispatcher dispacher = Dispatcher.getInstance();
		dispacher.pushEvent(newTCMsg);
		dispacher.pushEvent(nexTriger);
	}

}
