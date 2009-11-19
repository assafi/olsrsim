/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ITopologyManager.java
 * Author: Asi
 * Date: 15/11/2009
 *
 */
package topology;

import java.awt.Point;
import java.util.List;

/**
 * This interface indicates the methods that is implemented in the topology manger class.
 * @author Asi
 */
public interface ITopologyManager {

	/**
	 * @param stationID
	 * @param stationLocation
	 * @return An object that represents a station.
	 * @throws Exception
	 */
	IStation createNewStation(String stationID, Point stationLocation) throws Exception;
	
	/**
	 * @param stationID
	 *            Unique identifier.
	 * @param stationLocation
	 *            Physical location of the station, represented as a Point
	 *            object.
	 * @param radius 
	 * @return An object that represents a station.
	 * @throws Exception 
	 */
	IStation createNewStation(String stationID, Point stationLocation, double radius) throws Exception;

	/**
	 * @param location
	 *            A physical location, represented as a Point object.
	 * @param radius 
	 * @return A list of objects that represent a station.
	 */
	List<IStation> getNeighborsInReceptionArea(Point location, double radius);
	
	/**
	 * @param stationID
	 *            Unique identifier.
	 * @throws Exception 
	 */
	void removeStation(String stationID) throws Exception;

	/**
	 * @param stationLocation
	 *            Physical location of the station, represented as a Point
	 *            object.
	 * @throws Exception 
	 */
	void removeStation(Point stationLocation) throws Exception;

	/**
	 * @param stationID
	 *            Unique identifier.
	 * @param newPosition
	 *            New position of the station, represented as a Point object.
	 * @throws Exception 
	 */
	void changeStationPosition(String stationID, Point newPosition) throws Exception;

	/**
	 * @param stationID
	 *            Unique identifier.
	 * @return true/false according to the existence of the station.
	 */
	boolean doesStationExist(String stationID);

	/**
	 * @param stationLocation
	 *            Physical location of the station, represented as a Point
	 *            object.
	 * @return true/false according to the existence of the station.
	 */
	boolean doesStationExist(Point stationLocation);

	/**
	 * @param stationID
	 *            Unique identifier.
	 * @return A list of objects that represent a station.
	 * @throws Exception 
	 */
	List<IStation> getStationNeighbors(String stationID) throws Exception;
}

