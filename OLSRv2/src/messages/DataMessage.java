/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: DataMessage.java
 * Author: Eli Nazarov
 * Date: Nov 16, 2009
 *
 */
package messages;

import java.awt.Point;
import java.util.Collection;

import data.SimEvents;

import protocol.OLSRv2Protocol.IOLSRv2Protocol;

import topology.IStation;

import events.MessageEvent;

/**
 * @author Eli Nazarov
 *
 */
public class DataMessage extends MessageEvent {

	/*
	 * Global source and destination defines the src 
	 * of the data and the station that should receive it.
	 * the locaDst along with the src that is defined in MessageEvent
	 * are setting the stations that should receive and forward on
	 * the route the data message that was received. 
	 * 
	 */
	private String globalSrc = null;
	private String globalDst = null;
	private String localDst = null;
	private Point localDstLocation = null;
	/**
	 * @param src
	 * @param time
	 */
	public DataMessage(String localSrc, 
					   String localDst, 
					   String globalSrc, 
					   String globalDst,  
					   long time) {
		super(localSrc, time);
		this.localDst = localDst;
		this.globalSrc = globalSrc;
		this.globalDst = globalDst;
		this.messageType = SimEvents.DATA_SENT.name();
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.util.Map)
	 */
	@Override
	public void execute(Object nodes) {
		Collection<IStation> stations = (Collection<IStation>)nodes;
		for (IStation station : stations) {
			logEvent(station.getID(), globalSrc, globalDst);
			IOLSRv2Protocol olsrProtocol = station.getOLSRv2Protocol();
			olsrProtocol.reciveDataMessage(this);
		}
	}

	
	public Point getLocalDestinationLocation() {
		return this.localDstLocation;
	}
	
	public void setLocalDestinationLocation(Point loc) {
		this.localDstLocation = loc;
	}
	
	/**
	 * @return the globalSrc
	 */
	public String getGlobalSrc() {
		return globalSrc;
	}

	/**
	 * @param globalSrc the globalSrc to set
	 */
	public void setGlobalSrc(String globalSrc) {
		this.globalSrc = globalSrc;
	}
	

	/**
	 * @return the globalDst
	 */
	public String getGlobalDst() {
		return globalDst;
	}

	/**
	 * @param globalDst the globalDst to set
	 */
	public void setGlobalDst(String globalDst) {
		this.globalDst = globalDst;
	}
	

	/**
	 * @return the localDst
	 */
	public String getLocalDst() {
		return localDst;
	}

	/**
	 * @param localDst the localDst to set
	 */
	public void setLocalDst(String localDst) {
		this.localDst = localDst;
	}
	
	/**
	 * @return the localSrc
	 */
	public String getLocalSrc() {
		return this.getSource();
	}

	/**
	 * @param localDst the localDst to set
	 */
	public void setLocalSrc(String localSst) {
		this.setSource(localSst);
	}
	
	

}
