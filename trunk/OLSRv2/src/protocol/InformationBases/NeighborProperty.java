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
	
	private boolean symetricLink;
	private long quality;
	private long valideTime;
	
	/* OLSRv2 property fileds */
	private int willingness; //TODO maybe define an enum
	private boolean mpr;
	private boolean mpr_selector;
	private boolean advertised;
	
	public NeighborProperty(){
	}

	public NeighborProperty(boolean symetricLink,long quality, int valideTime){
		this.symetricLink = symetricLink;
		this.quality = quality;
		this.valideTime = valideTime;
		this.willingness = 0; //TODO should be WILL_NEVER
		this.mpr = this.mpr_selector = this.advertised  = false;
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
	public long getValideTime() {
		return valideTime;
	}
	/**
	 * @param valideTime the valideTime to set
	 */
	public void setValideTime(long valideTime) {
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

	/**
	 * @return the willingness
	 */
	public int getWillingness() {
		return willingness;
	}

	/**
	 * @param willingness the willingness to set
	 */
	public void setWillingness(int willingness) {
		this.willingness = willingness;
	}
	

	/**
	 * @return the mpr
	 */
	public boolean isMpr() {
		return mpr;
	}

	/**
	 * @param mpr the mpr to set
	 */
	public void setMpr(boolean mpr) {
		this.mpr = mpr;
	}
	

	/**
	 * @return the mpr_selector
	 */
	public boolean isMpr_selector() {
		return mpr_selector;
	}

	/**
	 * @param mprSelector the mpr_selector to set
	 */
	public void setMpr_selector(boolean mprSelector) {
		this.mpr_selector = mprSelector;
	}

	/**
	 * @return the advertised
	 */
	public boolean isAdvertised() {
		return advertised;
	}

	/**
	 * @param advertised the advertised to set
	 */
	public void setAdvertised(boolean advertised) {
		this.advertised = advertised;
	}
	
	
	
	
}
