/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: Link.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package protocol.InformationBases;

/**
 * @author Eli Nazarov
 *
 */
public class Link {
	private long quality;
	private int valideTime;
	/**
	 * @param quality
	 * @param valideTime
	 */
	public Link(long quality, int valideTime) {
		this.quality = quality;
		this.valideTime = valideTime;
	}
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
	

	
}
