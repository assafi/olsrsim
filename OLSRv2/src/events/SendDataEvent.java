/**
 * OLSRv2_2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: SendDataEvent.java
 * Author: Administrator
 * Date: Jan 12, 2010
 *
 */
package events;

import java.awt.Point;

import topology.IStation;

/**
 * @author Eli Nazarov
 *
 */
public class SendDataEvent extends Event {

	private String srcName;
	private Point srcLocation;
	private String dstName;
	private Point dstLocation;
	/**
	 * @param time
	 */
	public SendDataEvent(long time) {
		super(time);
	}

	/**
	 * @return the src
	 */
	public String getSrcName() {
		return srcName;
	}
	
	/**
	 * @return the location
	 */
	public Point getSrcLocation() {
		return srcLocation;
	}

	/**
	 * @param srcName the src to set
	 */
	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}
	
	/**
	 * @param loc
	 */
	public void setSrcLocation(Point loc) {
		this.srcLocation = loc;
	}


	/**
	 * @return the dst
	 */
	public String getDstName() {
		return dstName;
	}
	
	/**
	 * @return the loc
	 */
	public Point getDstLocation() {
		return dstLocation;
	}

	/**
	 * @param dstName 
	 */
	public void setDstName(String dstName) {
		this.dstName = dstName;
	}
	
	/**
	 * @param loc
	 */
	public void setDstLocation(Point loc) {
		this.dstLocation = loc;
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.lang.Object)
	 */
	@Override
	public void execute(Object node) throws Exception {
		IStation station = (IStation)node;
		station.getOLSRv2Protocol().sendDataMessage(dstName);
	}

}
