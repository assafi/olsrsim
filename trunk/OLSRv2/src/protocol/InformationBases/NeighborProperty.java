/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: NeighborProperty.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package protocol.InformationBases;

/**
 * @author Eli Nazarov
 *
 */
public class NeighborProperty {
	//TODO add fields used in the OLSRv2 layer
	private boolean symetricLink;
	private long quality;
	private int valideTime;

	/**
	 * @return the quality
	 */
	public long getQuality() {
		return quality;
	}
	/**
	 * @param quality the quality to set
	 */
	public void setQuality(long quality) {
		this.quality = quality;
	}
	
	/**
	 * @return the valideTime
	 */
	public int getValideTime() {
		return valideTime;
	}
	/**
	 * @param valideTime the valideTime to set
	 */
	public void setValideTime(int valideTime) {
		this.valideTime = valideTime;
	}
	
	/**
	 * @return the symetricLink
	 */
	public boolean isSymetricLink() {
		return symetricLink;
	}

	/**
	 * @param symetricLink the symetricLink to set
	 */
	public void setSymetricLink(boolean symetricLink) {
		this.symetricLink = symetricLink;
	}
	
	
}
