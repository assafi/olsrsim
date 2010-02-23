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

import data.SimEvents;
import data.SimLabels;
import dispatch.Dispatcher;
import log.Log;
import log.LogException;
import main.SimulationParameters;
import main.SimulationParameters.ProtocolMprMode;
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
import events.HelloIntervalEndEvent;
import events.IntervalEndEvent;
import events.MessageEvent;
import events.TCIntervalEndEvent;

/**
 * @author Eli Nazarov
 *
 */
public class OLSRv2Protocol implements IOLSRv2Protocol {

	private String stationID;
	private long lastReceiveTime = -1; 
	
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
	public void start(ProtocolMprMode mprMode) {
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
		HelloIntervalEndEvent nexTriger = new HelloIntervalEndEvent(stationID, helloTrigerMsg.getTime() + SimulationParameters.HelloInterval);
		HelloMessage newHelloMsg = nhdpLayer.generateHelloMessage(helloTrigerMsg.getTime());
		
		// Send them to the dispatcher
		Dispatcher dispacher = Dispatcher.getInstance();
		// if we are not transmitting or receiving we can send the data
		if (lastReceiveTime != -1 &&
			helloTrigerMsg.getTime() > lastReceiveTime + SimulationParameters.transmitionTime){
			dispacher.pushEvent(newHelloMsg);
		}
		dispacher.pushEvent(nexTriger);
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveDataMessage(events.MessageEvent)
	 */
	@Override
	public void reciveDataMessage(MessageEvent dataMsg) {
		DataMessage msg  = (DataMessage)dataMsg;
		
		// if we are transmitting or receiving then we should drop this message
		if (lastReceiveTime != -1 &&
			msg.getTime() >= lastReceiveTime && msg.getTime() <= lastReceiveTime + SimulationParameters.transmitionTime){
			logEvent(SimEvents.BUSSY_MSG_DROPPED.name(), msg.getGlobalSrc(), msg.getGlobalDst(), msg.getLocalSrc(), msg.getLocalDst() ,false, "Cannt receive Data message");
			return;
		}
		
		lastReceiveTime = msg.getTime(); // update the last receive time
		
		Dispatcher dispatcher = Dispatcher.getInstance();
		
		logEvent(SimEvents.DATA_REACHED_2_RELAY.name(), msg.getGlobalSrc(), msg.getGlobalDst(), msg.getLocalSrc(), msg.getLocalDst() ,false, "1-hop neighbour received the data message");
		
		if (msg.getLocalDst().equals(stationID)){// check if this message is for me
			if(msg.getGlobalDst().equals(stationID)){
				//we got the message!!!
				logEvent(SimEvents.DATA_REACH_2_TARGET.name(), null, msg.getLocalDst(), null, null ,false, "The message reached global destination");
			}
			else{
				if(neighborInfo.is1HopNeighbor(msg.getGlobalDst())){
					// if the destination is my neighbor send him the message
					logEvent(SimEvents.DATA_SENT_FROM_RELAY.name(), msg.getGlobalSrc(), msg.getGlobalDst(), msg.getLocalSrc(), msg.getLocalDst() ,false, "Data sent to the next station on the route");
					msg.setLocalSrc(stationID);
					msg.setLocalDst(msg.getGlobalDst());
					msg.updateTime(dispatcher.getCurrentVirtualTime() + SimulationParameters.transmitionTime);
					dispatcher.pushEvent(msg);
				}
				else{
					/*
					 *  the destination is not our neighbor 
					 *  -> we should send to the next node in the route 
					 */
					
					HashMap<String, RoutingSetData>  routingSet =  topologyInfo.getRoutingSet();
					
					if (!routingSet.containsKey(msg.getGlobalDst())){
						logEvent(SimEvents.DATA_LOSS.name(), msg.getGlobalSrc(), msg.getGlobalDst(), msg.getLocalSrc(), msg.getLocalDst() ,false, "Cann't find rout");
						return;
					}
					
					RoutingSetData entryData = routingSet.get(msg.getGlobalDst());
					msg.setSource(stationID);
					msg.setLocalDst(entryData.getNextHop());
					msg.updateTime(dispatcher.getCurrentVirtualTime() + SimulationParameters.transmitionTime);
					logEvent(SimEvents.DATA_SENT_FROM_RELAY.name(), msg.getGlobalSrc(), msg.getGlobalDst(), msg.getLocalSrc(), msg.getLocalDst() ,false, "Data sent to the next station on the route");
					dispatcher.pushEvent(msg);
				}
			}
		}
		else{
			logEvent(SimEvents.DATA_DROPPED_AT_RELAY.name(), msg.getGlobalSrc(), msg.getGlobalDst(), msg.getLocalSrc(), msg.getLocalDst() ,false, "The station not the local target of the message - The message is ignored");
		}
	}
	
	/* (non-Javadoc)
	 * @see protocol.OLSRv2Protocol.IOLSRv2Protocol#sendDataMessage(java.lang.String)
	 */
	@Override
	public void sendDataMessage(String dst) {
		
		Dispatcher dipatcher = Dispatcher.getInstance();
		
		// if we are transmitting or receiving then we should drop this message
		if (lastReceiveTime != -1 &&
			dipatcher.getCurrentVirtualTime() >= lastReceiveTime && dipatcher.getCurrentVirtualTime() <= lastReceiveTime + SimulationParameters.transmitionTime){
			logEvent(SimEvents.BUSSY_MSG_DROPPED.name(), stationID, dst, null, null ,false, "Data message cann't be sent from global source");
			return;
		}
		
//		lastReceiveTime = dipatcher.getCurrentVirtualTime();
		
		//log
		logEvent(SimEvents.DATA_SENT_FROM_SOURCE.name(), stationID, dst, null, null, false, null);
		
		//Create new data message from me to me
		DataMessage dataMsg = new DataMessage(stationID, stationID, stationID, dst, Dispatcher.getInstance().getCurrentVirtualTime() + SimulationParameters.transmitionTime);
		
		//Receive the message this will triger to forward the message in the network 
		reciveDataMessage(dataMsg);
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveHelloMessage(events.MessageEvent)
	 */
	@Override
	public void reciveHelloMessage(MessageEvent helloMsg) {
		/* if NHDP layer returns true after proccesing meaning that there
		 * was a new 1-jop symmetric neighbor added or 2-hop neighbor
		 * we must invoke the recalculation of MPRs */
		
		// if we are transmitting or receiving then we should drop this message
		if (lastReceiveTime != -1 &&
			helloMsg.getTime() >= lastReceiveTime && helloMsg.getTime() <= lastReceiveTime + SimulationParameters.transmitionTime){
			logEvent(SimEvents.BUSSY_MSG_DROPPED.name(), null, null, helloMsg.getSource(), stationID ,false, "Cann't proccess Hello message cause bussy");
			return;
		}
		
		lastReceiveTime = helloMsg.getTime();
		
		//clear the sets of invalid entries
		cleanExpiredSetEntries();
		
		try {
			if (HelloMessage.class.isAssignableFrom(helloMsg.getClass())){
				if (nhdpLayer.receiveHelloMessage((HelloMessage)helloMsg)){
					//if we need to calculate MPR sets do so 
					olsrLayer.calculateMPRs();
				}
				// Proccess the message by OLSR layer
				olsrLayer.processHelloMessage((HelloMessage)helloMsg);
			}
		} catch (ProtocolException e) {
			//Shouldn't fail
			e.printStackTrace();
		}
		
		//TODO remove
		//if(stationID.equals("8")){
			//neighborInfo.logStationTables(stationID);
			//topologyInfo.logStationTables(stationID);
		//}
//		if(stationID.equals("8")){
//			neighborInfo.logStationTables(stationID);
//			topologyInfo.logStationTables(stationID);
//		}
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveTCMessage(events.MessageEvent)
	 */
	@Override
	public void reciveTCMessage(MessageEvent tcMsg) {
		
		// if we are transmitting or receiving then we should drop this message
		if (lastReceiveTime != -1 &&
			tcMsg.getTime() >= lastReceiveTime && tcMsg.getTime() <= lastReceiveTime + SimulationParameters.transmitionTime){
			logEvent(SimEvents.BUSSY_MSG_DROPPED.name(), ((TCMessage)tcMsg).getGlobalSrc(), null, tcMsg.getSource(), stationID ,false, "Cann't proccess TC message cause bussy");
			return;
		}
		
		lastReceiveTime = tcMsg.getTime();
		
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
		
		//TODO remove
		//if(stationID.equals("8")){
			//neighborInfo.logStationTables(stationID);
			//topologyInfo.logStationTables(stationID);
		//}
//		if(stationID.equals("8")){
//			neighborInfo.logStationTables(stationID);
//			topologyInfo.logStationTables(stationID);
//		}
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
		TCIntervalEndEvent nexTriger = new TCIntervalEndEvent(stationID, tcTrigerMsg.getTime() + SimulationParameters.TCInterval);
		// This call already pushes the event in to the dispatcher
		TCMessage newTCMsg = olsrLayer.generateTCMessage(tcTrigerMsg.getTime());
		
		// Send them to the dispatcher
		Dispatcher dispacher = Dispatcher.getInstance();
		// if we are not transmitting or receiving we can send the data
		if (lastReceiveTime != -1 &&
			tcTrigerMsg.getTime() > lastReceiveTime + SimulationParameters.transmitionTime){
			dispacher.pushEvent(newTCMsg);
		}
		dispacher.pushEvent(nexTriger);
	}
	
	public void logEvent(String eventType, String globalSrc, String globalDst, String localSrc, String localDst, boolean error, String errorDetails) {
		Log log = Log.getInstance();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
		data.put(SimLabels.EVENT_TYPE.name(), eventType);
		data.put(SimLabels.NODE_ID.name(),stationID);
		data.put(SimLabels.LOCAL_SOURCE.name(), localSrc);
		data.put(SimLabels.LOCAL_TARGET.name(),localDst);
		data.put(SimLabels.GLOBAL_SOURCE.name(), globalSrc);
		data.put(SimLabels.GLOBAL_TARGET.name(),globalDst);
		data.put(SimLabels.ERROR.name(), (error ? "1" : "0"));
		data.put(SimLabels.DETAILS.name(), errorDetails);
		try {
			log.writeDown(data);
		} catch (LogException le) {
			System.out.println(le.getMessage());
		}
	}

}
