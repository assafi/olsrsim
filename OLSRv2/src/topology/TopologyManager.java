/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TopologyManager.java
 * Author: Asi
 * Date: 16/11/2009
 *
 */
package topology;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author Asi
 *
 */
public class TopologyManager implements ITopologyManager {
	private Map<String, IStation> stationsByID;
	private Map<Point, IStation> stationsByLocation;
	private Map<IStation, List<IStation>> stationNeigbors;
	private Random rand;
	/**
	 * 
	 */
	public TopologyManager() {
		stationsByID = new HashMap<String, IStation>();
		stationsByLocation = new HashMap<Point, IStation>();
		stationNeigbors = new HashMap<IStation, List<IStation>>();
		rand = new Random(System.currentTimeMillis());
	}
	
	public IStation createNewStation(String stationID, Point stationLocation) throws Exception {
		if(Station.defaultReceptionRadius <= 0) {
			throw new Exception("Can't create a new station with a negative " +
					"reception radius, probbably the default reception radius " +
					"in class Station wasn't defined");
		}
		return createNewStation(stationID, stationLocation, Station.defaultReceptionRadius);
	}
	
	public IStation createNewStation(String stationID, Point stationLocation, double radius) throws Exception {
		if(radius <= 0) {
			throw new Exception("Radius must be a positive value");
		}
		if(doesStationExist(stationID)) {
			throw new Exception("Station id already in use");
		}
		if(doesStationExist(stationLocation)) {
			throw new Exception("Station position already in use");
		}
		
		IStation newStation = new Station(stationID, stationLocation);
		newStation.setReceptionRadius(radius);
		
		// iterating over all stations, for every station that sees the new station
		// add the new station to its neighbors list
		for (IStation station : stationNeigbors.keySet()) {
			if(station.isInRange(newStation)) {
				stationNeigbors.get(station).add(newStation);
			}
		}
		
		List<IStation> newStationNeigbors = getNeighborsInReceptionArea(stationLocation, radius);
		stationsByID.put(stationID, newStation);
		stationsByLocation.put(stationLocation, newStation);
		stationNeigbors.put(newStation, newStationNeigbors);
		
		return newStation;
	}
	
	public List<IStation> getNeighborsInReceptionArea(Point location, double radius) {
		List<IStation> res = new LinkedList<IStation>();
		IStation dummy = new Station("DUMMY", location);
		dummy.setReceptionRadius(radius);
		
		// iterating over all stations, for every station that is in the radius of
		// the dummy station add to the neigbor list
		for (IStation station : stationNeigbors.keySet()) {
			if(dummy.isInRange(station)) {
				res.add(station);
			}
		}
		
		return res;
	}

	public IStation removeStation(String stationID) throws Exception {
		if(!doesStationExist(stationID)) {
			throw new Exception("Station " + stationID + "doesn't exist");
		}
		IStation iStation = stationsByID.get(stationID);
		stationsByID.remove(stationID);
		stationsByLocation.remove(iStation.getLocation());
		stationNeigbors.remove(iStation);
		
		for (IStation station : stationNeigbors.keySet()) {
			stationNeigbors.get(station).remove(iStation);
		}
		return iStation;
	}
	
	public IStation removeStation(Point stationLocation) throws Exception {
		return removeStation(stationsByLocation.get(stationLocation).getID());
	}
	
	public void changeStationPosition(String stationID, Point newPosition) throws Exception {
		if(doesStationExist(newPosition)) {
			throw new Exception("New position already in use");
		}
		
		IStation iStation = stationsByID.get(stationID);
		stationsByLocation.remove(iStation.getLocation());
		stationNeigbors.remove(iStation);
		iStation.setLocation(newPosition);
		
		// Iterate on all station, set station new neighbors
		// Makes sure there are no duplicates
		for (IStation station : stationNeigbors.keySet()) {
			if(station.isInRange(iStation)) {
				if(!stationNeigbors.get(station).contains(iStation)) {
					stationNeigbors.get(station).add(iStation);
				}
			}
			else {
				if(stationNeigbors.get(station).contains(iStation)) {
					stationNeigbors.get(station).remove(iStation);
				}
			}
		}
		
		List<IStation> newNeigbors = getNeighborsInReceptionArea(newPosition, iStation.getReceptionRadius());
		stationNeigbors.put(iStation, newNeigbors);
		stationsByLocation.put(newPosition, iStation);
	}

	public boolean doesStationExist(String stationID) {
		return stationsByID.containsKey(stationID);
	}

	public boolean doesStationExist(Point stationLocation) {
		return stationsByLocation.containsKey(stationLocation);
	}

	public List<IStation> getStationNeighbors(String stationID) throws Exception {
		if(!doesStationExist(stationID)) {
			throw new Exception("Station doesn't exist");
		}
		IStation iStation = stationsByID.get(stationID);
		return stationNeigbors.get(iStation);
	}

	public int count() {
		return stationsByID.size();
	}

	public String getRandomStation() {
		int randomStationIndex = rand.nextInt(this.count());
		return new String((String)stationsByID.keySet().toArray()[randomStationIndex]);
	}
	
	public Set<Point> getAllStationsPositions() {
		return stationsByLocation.keySet();
	}

}
