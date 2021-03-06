/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ClustersLayout.java
 * Author: Assaf
 * Date: 20/11/2009
 *
 */
package layout;

import java.awt.Point;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

/**
 * @author Assaf
 *
 */
public class ClustersLayout extends Layout{

	private static final int defaultNmbClusters = 5;
	private static final double HARD_LOWER_LIMIT = 40.0;

	private int nmbClusters;
	private int radius;
	private Map<Point, Integer> clusters;

	/**
	 * This object defines a clusters layout in 2D space.
	 * the space is defined in terms of the first quarter of a
	 * Cartesian grid, as if the south west corner is the (0,0)
	 * Coordinates. The number of clusters defined is the default
	 * value (5).
	 * @param xBoundry The right border of the space.   
	 * @param yBoundry The upper border of the space.  
	 * @param radius If defined as a positive value, all clusters 
	 * will be defined by this value. if set to 0, clusters will have random
	 * radius in which they can define relative points.
	 * @throws LayoutException Will be thrown if space boundaries are not
	 * positive, or if radius is negative.
	 * 
	 */
	public ClustersLayout(int xBoundry, int yBoundry, int radius) 
	throws LayoutException {

		this.nmbClusters = defaultNmbClusters;

		if (xBoundry <= 0 || yBoundry <= 0){
			throw new LayoutException("Boundries must be positive");
		}
		this.xBoundry = xBoundry;
		this.yBoundry = yBoundry;

		if (radius < 0){
			throw new LayoutException("Radius must be non-negative");
		}
		this.radius = radius;

		this.clusters = new Hashtable<Point, Integer>();

		defineClusters();
	}

	/**
	 * This object defines a clusters layout in 2D space.
	 * the space is defined in terms of the first quarter of a
	 * Cartesian grid, as if the south west corner is the (0,0)
	 * Coordinates.
	 * @param nmbClusters The number of clusters. 
	 * @param xBoundry the right border of the space.   
	 * @param yBoundry the upper border of the space.  
	 * @param radius If defined as a positive value, all clusters 
	 * will be defined by this value. if set to 0, clusters will have random
	 * radius in which they can define relative points.
	 * @throws LayoutException Will be thrown if space boundaries are not
	 * positive, or if radius is negative, or if the number of clusters
	 * is not positive.
	 * 
	 */
	public ClustersLayout(int nmbClusters,int xBoundry, int yBoundry
			, int radius) throws LayoutException{

		if (nmbClusters <= 0){
			throw new LayoutException("Number of clusters must be positive");
		}
		this.nmbClusters = nmbClusters;

		if (xBoundry <= 0 || yBoundry <= 0){
			throw new LayoutException("Boundries must be positive");
		}
		this.xBoundry = xBoundry;
		this.yBoundry = yBoundry;

		if (radius < 0){
			throw new LayoutException("Radius must be non-negative");
		}
		this.radius = radius;

		this.clusters = new Hashtable<Point, Integer>();

		defineClusters();
	}

	/**
	 * 
	 */
	private void defineClusters() {

		for (int i=0; i< this.nmbClusters; i++){

			int xCoor = new Random().nextInt(xBoundry);
			int yCoor = new Random().nextInt(yBoundry);

			double upperLimit = Math.min(Math.min(xCoor, xBoundry - xCoor),
					Math.min(yCoor, yBoundry - yCoor));
			
			if (upperLimit < HARD_LOWER_LIMIT) {
				i--;
				continue;
			}

			int clusterRadius;

			if (radius <= HARD_LOWER_LIMIT){
				/*
				 * Generate random radius
				 */
				while ((clusterRadius = new Random().nextInt((int)upperLimit)) < HARD_LOWER_LIMIT);
			} else {
				upperLimit = Math.min(this.radius, upperLimit);
				while ((clusterRadius = new Random().nextInt((int)upperLimit)) < HARD_LOWER_LIMIT);
			}

			Point p = new Point(xCoor,yCoor);

			if (clusters.containsKey(p)){
				/*
				 * try again
				 */
				i--;
				continue;
			}

			clusters.put(p, clusterRadius);
		}
	}

	@Override
	public Point getRandomPoint() throws LayoutException{

		return getRandomPoint(null);
	}

	/* (non-Javadoc)
	 * @see layout.Layout#getRandomPoint(java.awt.Point)
	 */
	@Override
	public Point getRandomPoint(Point environment) throws LayoutException {

			if (clusters.isEmpty()){
				throw new LayoutException("clusters map is empty");
			}

			Point clusterCenter = getOverlappingCluster(environment);
			double theta = new Random().nextDouble() % (2*Math.PI);
			int clusterRadius = clusters.get(clusterCenter);
			int radius = (int) (Math.pow(new Random().nextDouble(),2) * clusterRadius);

			int xCoor = (int) (Math.cos(theta)*radius + clusterCenter.getX());
			int yCoor = (int) (Math.sin(theta)*radius + clusterCenter.getY());

			assert(xCoor >= 0 && xCoor < this.xBoundry);
			assert(yCoor >= 0 && yCoor < this.yBoundry);

//			System.out.println("New point at [" + xCoor + "," + yCoor + "] grid is: " + this.xBoundry + "X" + this.yBoundry);

			Point p = new Point(xCoor,yCoor);
			return p;
		}

	/**
	 * @param environment
	 * @return
	 */
	private Point getOverlappingCluster(Point environment) {
		Point candidate = null;
		if (null != environment) {
			for (Point cluster : clusters.keySet()) {
				if ((null == candidate && cluster.distance(environment) <= clusters.get(cluster)) ||
					(null != candidate && cluster.distance(environment) < candidate.distance(environment)))
					candidate = cluster;
			}
		}
		
		if (null != candidate) {
			return candidate;
		}
		
		int clusterIndex = new Random().nextInt(clusters.size());
		return (Point) clusters.keySet().toArray()[clusterIndex];
	}

	}
