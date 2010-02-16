/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: UniformLayout.java
 * Author: Assaf
 * Date: Nov 25, 2009
 *
 */
package layout;

import java.awt.Point;
import java.util.Random;

/**
 * @author Assaf
 *
 */
public class UniformLayout extends Layout {
	
	/**
	 * This object defines a uniform scatter layout in 2D space. </br>
	 * the space is defined in terms of the first quarter of a </br>
	 * Cartesian grid, as if the south west corner is the (0,0) </br>
	 * Coordinates.
	 * @param xBoundry The right border of the space.   
	 * @param yBoundry The upper border of the space.  
	 * @throws LayoutException LayoutException Will be thrown if space boundaries are not
	 * positive, or if radius is negative.
	 * 
	 */
	public UniformLayout(int xBoundry, int yBoundry) 
		throws LayoutException {
		
		if (xBoundry <= 0 || yBoundry <= 0){
			throw new LayoutException("Boundries must be positive values");
		}
		
		this.xBoundry = xBoundry;
		this.yBoundry = yBoundry;
	}

	/* (non-Javadoc)
	 * @see layout.Layout#getRandomPoint()
	 */
	@Override
	public Point getRandomPoint() throws LayoutException {
		
		int xCoor = new Random().nextInt() % xBoundry;
		int yCoor = new Random().nextInt() % yBoundry;
		return new Point(Math.abs(xCoor), Math.abs(yCoor));
	}

	/* (non-Javadoc)
	 * @see layout.Layout#getRandomPoint(java.awt.Point)
	 */
	@Override
	public Point getRandomPoint(Point environment) throws LayoutException {
		return getRandomPoint();
	}

}
