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
	private int symTime;
	
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
		
		//TODO OLSR layer should receive more bases.
		this.olsrLayer = new OLSRv2Layer(stationID, localInfo, neighborInfo, topologyInfo, receivedMsgInfo);
		this.nhdpLayer = new NHDPLayer(stationID, localInfo, neighborInfo, this.olsrLayer);
		
		symTime = 0; //TODO see if need to make it better time;
	}
	
	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#helloIntervalTriger()
	 */
	@Override
	public void helloIntervalTriger() {
		/* when we recive an event that the hello interval is over
		we should generate hello massage and insert GenerateHelloMsg
		event so that we will know next time that the interval is over*/
	
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveDataMessage(events.MessageEvent)
	 */
	@Override
	public void reciveDataMessage(MessageEvent dataMsg) {
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveHelloMessage(events.MessageEvent)
	 */
	@Override
	public void reciveHelloMessage(MessageEvent helloMsg) {
		/* if NHDP layer returns true after proccesing meaning that there
		 * was a new 1-jop symmetric neighbor added or 2-hop neighbor
		 * we must invoke the recalculation of MPRs */
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#reciveTCMessage(events.MessageEvent)
	 */
	@Override
	public void reciveTCMessage(MessageEvent tcMsg) {
	}

	/* (non-Javadoc)
	 * @see protocol.IOLSRv2Protocol#tcIntervalTriger()
	 */
	@Override
	public void tcIntervalTriger() {
		/* when we recive an event that the tc interval is over
		we should generate hello massage and insert GenerateTCMsg
		event so that we will know next time that the interval is over
		TODO here we must send a new TC Message*/
	}

}
