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
