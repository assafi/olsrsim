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
	private long timeUntillBusy = -1; 
	
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
		if (timeUntillBusy != -1 &&
			helloTrigerMsg.getTime() > timeUntillBusy + SimulationParameters.transmissionTime){
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
		
		Dispatcher dispatcher = Dispatcher.getInstance();
		
		if (msg.getLocalDst().equals(stationID)){// check if this message is for me
			
			// if we are transmitting or receiving then we should drop this message
			if (timeUntillBusy != -1 &&
				msg.getTime() <= timeUntillBusy + SimulationParameters.transmissionTime){
				logEvent(SimEvents.BUSSY_MSG_IGNORED.name(), 
						msg.getGlobalSrc(), msg.getGlobalDst(), 
						msg.getLocalSrc(), msg.getLocalDst() ,
						false, "Cannot receive Data message", true);
				return;
			}
			
			timeUntillBusy = msg.getTime(); // update the last receive time
			
			logEvent(SimEvents.DATA_REACHED_2_RELAY.name(), 
					msg.getGlobalSrc(), msg.getGlobalDst(), 
					msg.getLocalSrc(), msg.getLocalDst() ,
					false, "1-hop neighbour received the data message",false);
			
			if(msg.getGlobalDst().equals(stationID)){
				//we got the message!!!
				logEvent(SimEvents.DATA_REACHED_2_TARGET.name(), 
						null, msg.getLocalDst(), null, null ,false, 
						"The message reached global destination",false);
			}
			else{
				sendMsgOnRoute(msg, false);
			}
		}
		else{
			logEvent(SimEvents.DATA_IGNORED_AT_RELAY.name(), 
					msg.getGlobalSrc(), msg.getGlobalDst(), 
					msg.getLocalSrc(), msg.getLocalDst() ,
					false, "The station is not the local target of the message - The message is ignored", false);
		}
	}
	
	/* (non-Javadoc)
	 * @see protocol.OLSRv2Protocol.IOLSRv2Protocol#sendDataMessage(java.lang.String)
	 */
	@Override
	public void sendDataMessage(String dst) {
		
		Dispatcher dipatcher = Dispatcher.getInstance();
		
		// if we are transmitting or receiving then we should drop this message
		if (timeUntillBusy != -1 &&
				dipatcher.getCurrentVirtualTime() <= 
							timeUntillBusy + SimulationParameters.transmissionTime){
			
			logEvent(SimEvents.BUSSY_MSG_IGNORED.name(), stationID, 
					dst, null, null ,false, "Data message cannot be sent from global source"
					,true);
			return;
		}
		
		//Create new data message and send on the calculated route
		DataMessage dataMsg = new DataMessage(stationID, stationID, stationID, dst, Dispatcher.getInstance().getCurrentVirtualTime() + SimulationParameters.transmissionTime);
		
		sendMsgOnRoute(dataMsg, true);
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
		if (timeUntillBusy != -1 &&
			helloMsg.getTime() <= timeUntillBusy + SimulationParameters.transmissionTime){
			logEvent(SimEvents.BUSSY_MSG_IGNORED.name(), null, null, helloMsg.getSource(), 
					stationID ,false, "Cannot proccess Hello message cause bussy",true);
			return;
		}
		
		timeUntillBusy = helloMsg.getTime();
		
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
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveTCMessage(events.MessageEvent)
	 */
	@Override
	public void reciveTCMessage(MessageEvent tcMsg) {
		
		// if we are transmitting or receiving then we should drop this message
		if (timeUntillBusy != -1 &&
			tcMsg.getTime() <= timeUntillBusy + SimulationParameters.transmissionTime){
			logEvent(SimEvents.BUSSY_MSG_IGNORED.name(), ((TCMessage)tcMsg).getGlobalSrc(), null, tcMsg.getSource(), stationID ,
					false, "Cann't proccess TC message cause bussy",true);
			return;
		}
		
		timeUntillBusy = tcMsg.getTime();
		
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
		TCIntervalEndEvent nexTriger = new TCIntervalEndEvent(stationID, tcTrigerMsg.getTime() + SimulationParameters.TCInterval);
		// This call already pushes the event in to the dispatcher
		TCMessage newTCMsg = olsrLayer.generateTCMessage(tcTrigerMsg.getTime());
		
		// Send them to the dispatcher
		Dispatcher dispacher = Dispatcher.getInstance();
		// if we are not transmitting or receiving we can send the data
		if (timeUntillBusy != -1 &&
			tcTrigerMsg.getTime() > timeUntillBusy + SimulationParameters.transmissionTime){
			dispacher.pushEvent(newTCMsg);
		}
		dispacher.pushEvent(nexTriger);
	}
	
	private void sendMsgOnRoute(DataMessage msg, boolean isGlobalSource){
		
		Dispatcher dispatcher = Dispatcher.getInstance();
		
		if(neighborInfo.is1HopNeighbor(msg.getGlobalDst())){
			// if the destination is my neighbor send him the message
			if(isGlobalSource){
				logEvent(SimEvents.DATA_SENT_FROM_SOURCE.name(), 
						msg.getGlobalSrc(), msg.getGlobalDst(), 
						msg.getLocalSrc(), msg.getLocalDst() ,
						false, "Data sent to the next station on the route from global source",false);
			}
			else{
				logEvent(SimEvents.DATA_SENT_FROM_RELAY.name(), 
						msg.getGlobalSrc(), msg.getGlobalDst(), 
						msg.getLocalSrc(), msg.getLocalDst() ,
						false, "Data sent to the next station on the route",false);
			}
			
			msg.setLocalSrc(stationID);
			msg.setLocalDst(msg.getGlobalDst());
			msg.updateTime(dispatcher.getCurrentVirtualTime() + SimulationParameters.transmissionTime);
			timeUntillBusy = dispatcher.getCurrentVirtualTime();
			dispatcher.pushEvent(msg);
		}
		else{
			/*
			 *  the destination is not our neighbor 
			 *  -> we should send to the next node in the route 
			 */
			
			HashMap<String, RoutingSetData>  routingSet =  topologyInfo.getRoutingSet();
			
			if (!routingSet.containsKey(msg.getGlobalDst())){
				if (isGlobalSource){
					logEvent(SimEvents.DATA_NOT_SENT_NO_ROUTE.name(), 
							msg.getGlobalSrc(), msg.getGlobalDst(), 
							msg.getLocalSrc(), msg.getLocalDst() ,
							false, "Data wasnt sent from source. Cannot find route",true);
				}
				else{
					logEvent(SimEvents.DATA_LOSS_NO_ROUTE.name(), 
							msg.getGlobalSrc(), msg.getGlobalDst(), 
							msg.getLocalSrc(), msg.getLocalDst() ,
							false, "Cannot find route",true);
				}
				return;
			}
			
			/*
			 * Found destination routing set
			 */
			RoutingSetData entryData = routingSet.get(msg.getGlobalDst());
			msg.setSource(stationID);
			msg.setLocalDst(entryData.getNextHop());
			msg.updateTime(dispatcher.getCurrentVirtualTime() + SimulationParameters.transmissionTime);
			if(isGlobalSource){
				logEvent(SimEvents.DATA_SENT_FROM_SOURCE.name(), 
						msg.getGlobalSrc(), msg.getGlobalDst(), 
						msg.getLocalSrc(), msg.getLocalDst() ,
						false, "Data sent to the next station on the route from global source",false);
			}
			else{
				logEvent(SimEvents.DATA_SENT_FROM_RELAY.name(), 
						msg.getGlobalSrc(), msg.getGlobalDst(), 
						msg.getLocalSrc(), msg.getLocalDst() ,
						false, "Data sent to the next station on the route",false);
			}
			timeUntillBusy = dispatcher.getCurrentVirtualTime();
			dispatcher.pushEvent(msg);
		}
	}
	
	public void logEvent(String eventType, String globalSrc, String globalDst, String localSrc, String localDst, boolean error, String errorDetails, boolean lost) {
		Log log = Log.getInstance();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(Dispatcher.getInstance().getCurrentVirtualTime()));
		data.put(SimLabels.EVENT_TYPE.name(), eventType);
		data.put(SimLabels.NODE_ID.name(),stationID);
		data.put(SimLabels.LOCAL_SOURCE.name(), localSrc);
		data.put(SimLabels.LOCAL_TARGET.name(),localDst);
		data.put(SimLabels.GLOBAL_SOURCE.name(), globalSrc);
		data.put(SimLabels.GLOBAL_TARGET.name(),globalDst);
		data.put(SimLabels.LOST.name(), (lost ? "1" : "0"));
		data.put(SimLabels.ERROR.name(), (error ? "1" : "0"));
		data.put(SimLabels.DETAILS.name(), errorDetails);
		try {
			log.writeDown(data);
		} catch (LogException le) {
			System.out.println(le.getMessage());
		}
	}

}
