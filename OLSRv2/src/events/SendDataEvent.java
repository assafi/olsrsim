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

import topology.IStation;

/**
 * @author Eli Nazarov
 *
 */
public class SendDataEvent extends Event {

	private String src;
	private String dst;
	/**
	 * @param time
	 */
	public SendDataEvent(long time) {
		super(time);
	}

	/**
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * @param src the src to set
	 */
	public void setSrc(String src) {
		this.src = src;
	}


	/**
	 * @return the dst
	 */
	public String getDst() {
		return dst;
	}

	/**
	 * @param dst the dst to set
	 */
	public void setDst(String dst) {
		this.dst = dst;
	}

	/* (non-Javadoc)
	 * @see events.Event#execute(java.lang.Object)
	 */
	@Override
	public void execute(Object node) throws Exception {
		IStation station = (IStation)node;
		station.getOLSRv2Protocol().sendDataMessage(dst);
	}

}
