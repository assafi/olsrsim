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

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Random;

/**
 * @author Assaf
 *
 */
public class UniformLayout extends Layout {

	private double xBoundry;
	private double yBoundry;
	
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
	public UniformLayout(double xBoundry, double yBoundry) 
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
	public Double getRandomPoint() throws LayoutException {
		
		double xCoor = new Random().nextDouble() * xBoundry;
		double yCoor = new Random().nextDouble() * yBoundry;
		return new Point2D.Double(xCoor, yCoor);
	}

}
