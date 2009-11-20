/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ILayout.java
 * Author: Assaf
 * Date: Nov 16, 2009
 *
 */
package layout;

import java.awt.geom.Point2D;

/**
 * @author Assaf
 *
 */
public abstract class Layout {
	
	protected double xBoundry;
	protected double yBoundry;
	
	/**
	 * @return return a random point within the simulation boundaries.
	 * @throws LayoutException Is thrown if the Layout object cannot
	 * calculate a point with in it's current state. 
	 */
	public abstract Point2D.Double getRandomPoint() throws LayoutException;
}
