/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: CommonData.java
 * Author: Eli Nazarov
 * Date: Nov 18, 2009
 *
 */
package protocol.InformationBases;

/**
 * @author Eli Nazarov
 *
 */
public class TopologyCommonData {
	//TODO see if we need to use sequence numbers in TC messages
	/*private long sequenceNumber;*/
	private long timeToLeave;
	
	public TopologyCommonData(/*long sequenceNumber,*/ long timeToLeave){
		//this.sequenceNumber = sequenceNumber;
		this.timeToLeave = timeToLeave;
	}

	/**
	 * @return the sequenceNumber
	 */
	/*public long getSequenceNumber() {
		return sequenceNumber;
	}*/

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	/*public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}*/
	

	/**
	 * @return the time
	 */
	public long getTime() {
		return timeToLeave;
	}

	/**
	 * @param time the time to set
	 */
	public void setTTL(long timeToLeave) {
		this.timeToLeave = timeToLeave;
	}
	
	
	

}
