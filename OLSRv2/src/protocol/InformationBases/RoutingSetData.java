/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: RoutingSetData.java
 * Author: Eli Nazarov
 * Date: Nov 18, 2009
 *
 */
package protocol.InformationBases;

/**
 * @author Eli Nazarov
 *
 */
public class RoutingSetData extends TopologyCommonData {
	
	private String nextHop;
	private long numberOfHops;
	
	/**
	 * @param sequenceNumber
	 * @param time
	 */
	public RoutingSetData(long timeToLeave, 
						  String nextHop,
						  long numberOfHops) {
		super(timeToLeave);
		this.nextHop = nextHop;
		this.numberOfHops = numberOfHops;
	}	

	/**
	 * @return the nextHop
	 */
	public String getNextHop() {
		return nextHop;
	}

	/**
	 * @param nextHop the nextHop to set
	 */
	public void setNextHop(String nextHop) {
		this.nextHop = nextHop;
	}
	

	/**
	 * @return the hopsNumber
	 */
	public long getHopsNumber() {
		return numberOfHops;
	}

	/**
	 * @param hopsNumber the hopsNumber to set
	 */
	public void setHopsNumber(long numberOfHops) {
		this.numberOfHops = numberOfHops;
	}
	
	
	
	
}
