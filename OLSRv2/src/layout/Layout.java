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

import java.awt.Point;

/**
 * @author Assaf
 *
 */
public abstract class Layout {
	
	protected static double xBoundry;
	protected static double yBoundry;
	
	/**
	 * @return return a random point within the simulation boundaries.
	 */
	public abstract Point getRandomPoint();
}
