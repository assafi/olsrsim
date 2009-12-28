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
package protocol;

import dispatch.Dispatcher;
import messages.GenerateHelloMsg;
import messages.HelloMessage;
import messages.TCMessage;
import protocol.InformationBases.LocalInformationBase;
import protocol.InformationBases.NeighborInformationBase;
import protocol.InformationBases.ReceivedMessageInformationBase;
import protocol.InformationBases.TopologyInformationBase;
import protocol.NHDPLayer.INHDPLayer;
import protocol.NHDPLayer.NHDPLayer;
import protocol.OLSRv2Layer.IOLSRv2Layer;
import protocol.OLSRv2Layer.OLSRv2Layer;
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
		
		this.olsrLayer = new OLSRv2Layer(stationID, localInfo, neighborInfo, topologyInfo, receivedMsgInfo);
		this.nhdpLayer = new NHDPLayer(stationID, localInfo, neighborInfo, this.olsrLayer);
		
	}
	
	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#helloIntervalTriger()
	 */
	@Override
	public void helloIntervalTriger(MessageEvent helloTrigerMsg) {
		/* when we recive an event that the hello interval is over
		we should generate hello massage and insert GenerateHelloMsg
		event so that we will know next time that the interval is over
		Impotant: the distination should be us meaning src=dst
		TODO: See that when a dispacher procceses this event only I get it
		*/
		
		//Create the messages
		GenerateHelloMsg nexTriger = new GenerateHelloMsg(stationID, helloTrigerMsg.getTime() + ProtocolDefinitions.HelloInterval);
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
		//TODO implement
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveHelloMessage(events.MessageEvent)
	 */
	@Override
	public void reciveHelloMessage(MessageEvent helloMsg) {
		/* if NHDP layer returns true after proccesing meaning that there
		 * was a new 1-jop symmetric neighbor added or 2-hop neighbor
		 * we must invoke the recalculation of MPRs */
		
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
	public void tcIntervalTriger(MessageEvent tcTrigerMsg) {
		/* when we receive an event that the TC interval is over
		we should generate hello massage and insert GenerateTCMsg
		event so that we will know next time that the interval is over
		here we must send a new TC Message*/
		
		//Create the messages
		GenerateHelloMsg nexTriger = new GenerateHelloMsg(stationID, tcTrigerMsg.getTime() + ProtocolDefinitions.TCInterval);
		TCMessage newTCMsg = olsrLayer.generateTCMessage(tcTrigerMsg.getTime());
		
		// Send them to the dispatcher
		Dispatcher dispacher = Dispatcher.getInstance();
		dispacher.pushEvent(newTCMsg);
		dispacher.pushEvent(nexTriger);
	}

}
