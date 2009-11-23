/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ReceivedSetData.java
 * Author: Eli Nazarov
 * Date: Nov 18, 2009
 *
 */
package protocol.InformationBases;

/**
 * @author Eli Nazarov
 *
 */
public class ReceivedSetData extends TopologyCommonData {

	private MessegeTypes type = null;
	private int msgHashCode;
	
	/**
	 * @param sequenceNumber
	 * @param time
	 */
	public ReceivedSetData(MessegeTypes type, int msgHashCode, long timeToLeave) {
		super(timeToLeave);
		this.type = type;
		this.msgHashCode = msgHashCode;
	}

	/**
	 * @return the type
	 */
	public MessegeTypes getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MessegeTypes type) {
		this.type = type;
	}
	

	/**
	 * @return the msgHashCode
	 */
	public int getMsgHashCode() {
		return msgHashCode;
	}

	/**
	 * @param msgHashCode the msgHashCode to set
	 */
	public void setMsgHashCode(int msgHashCode) {
		this.msgHashCode = msgHashCode;
	}
	
	
	

}
