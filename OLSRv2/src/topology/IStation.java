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
	String getStationID();
	
	/**
	 * @return  Physical location of the station
	 */
	Point getStationLocation();
	
	/**
	 * @return  OLSRv2 protocol layer of the station as defined in IOLSRv2Protocol interface
	 */
	IOLSRv2Protocol getOLSRv2Protocol();
	
	/**
	 * @param radius A station reception radius
	 */
	void setStationReceptionRadius(double radius);
	
}
