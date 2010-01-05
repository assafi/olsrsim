/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: IStation.java
 * Author: Asi
 * Date: 15/11/2009
 *
 */
package topology;

import java.awt.Point;

import protocol.IOLSRv2Protocol;

/**
 * @author Asi
 *
 */
public interface IStation {

	/**
	 * @return  Unique identification of a station
	 */
	String getID();
	
	/**
	 * @return  Physical location of the station
	 */
	Point getLocation();
	
	/**
	 * @param location
	 * @throws Exception
	 */
	void setLocation(Point location) throws Exception;
	
	/**
	 * @return  OLSRv2 protocol layer of the station as defined in IOLSRv2Protocol interface
	 */
	IOLSRv2Protocol getOLSRv2Protocol();
	
	/**
	 * @param radius A station reception radius
	 */
	void setReceptionRadius(double radius);
	
	/**
	 * @return The station reception radius
	 */
	double getReceptionRadius();

	/**
	 * @param newStation
	 * @return Returns true if newStation is in the radius of the station
	 */
	boolean isInRange(IStation newStation);
	
	/**
	 * Starts the station activity
	 */
	void start();
}
