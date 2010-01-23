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
	private String dstName;
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
	 * @param srcName the src to set
	 */
	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}
	

	/**
	 * @return the dst
	 */
	public String getDstName() {
		return dstName;
	}

	/**
	 * @param dstName 
	 */
	public void setDstName(String dstName) {
		this.dstName = dstName;
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
