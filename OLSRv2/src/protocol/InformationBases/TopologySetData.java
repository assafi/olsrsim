/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TopologySetData.java
 * Author: Eli Nazarov
 * Date: Nov 18, 2009
 *
 */
package protocol.InformationBases;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eli Nazarov
 *
 */
public class TopologySetData extends TopologyCommonData {
	
	private List<String> toAddresses = null;
	/**
	 * @param sequenceNumber
	 * @param time
	 */
	public TopologySetData(long sequenceNumber, long time) {
		super(sequenceNumber, time);
		toAddresses = new ArrayList<String>();
	}
	
	/**
	 * @return the toAddresses
	 */
	public List<String> getToAddresses() {
		return toAddresses;
	}
	/**
	 * @param toAddresses the toAddresses to set
	 */
	public void setToAddresses(List<String> toAddresses) {
		this.toAddresses = toAddresses;
	}
	
	public void addToAddress(String address) {
		toAddresses.add(address);
	}
	
	public boolean isReachedAddress(String address) {
		return toAddresses.contains(address);
	}
	
}
