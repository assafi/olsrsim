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
	
	protected int xBoundry;
	protected int yBoundry;
	
	/**
	 * @return return a random point within the simulation boundaries.
	 * @throws LayoutException Is thrown if the Layout object cannot
	 * calculate a point in it's current state. 
	 */
	public abstract Point getRandomPoint() throws LayoutException;
}
