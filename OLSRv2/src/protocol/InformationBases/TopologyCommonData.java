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
	private long sequenceNumber;
	private long time;
	
	public TopologyCommonData(long sequenceNumber, long time){
		this.sequenceNumber = sequenceNumber;
		this.time = time;
	}

	/**
	 * @return the sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}
	
	
	

}
