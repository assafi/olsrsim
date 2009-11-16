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
	 *            Unique identifier.
	 * @param stationLocation
	 *            Physical location of the station, represented as a Point
	 *            object.
	 * @return An object that represents a station.
	 */
	IStation createNewStation(String stationID, Point stationLocation);

	/**
	 * @param stationID
	 *            Unique identifier.
	 */
	void removeStation(String stationID);

	/**
	 * @param stationLocation
	 *            Physical location of the station, represented as a Point
	 *            object.
	 */
	void removeStation(Point stationLocation);

	/**
	 * @param stationID
	 *            Unique identifier.
	 * @param newPosition
	 *            New position of the station, represented as a Point object.
	 */
	void changeStationPosition(String stationID, Point newPosition);

	/**
	 * @param radius
	 *            A default reception radius for a station.
	 */
	void setStationsDefaultReceptionRadius(double radius);

	/**
	 * @param stationID
	 *            Unique identifier.
	 * @return A list of objects that represent a station.
	 */
	List<IStation> getStationNeighbors(String stationID);

	/**
	 * @param location
	 *            A physical location, represented as a Point object.
	 * @return A list of objects that represent a station.
	 */
	List<IStation> getNeighborsInReceptionArea(Point location);

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

}
