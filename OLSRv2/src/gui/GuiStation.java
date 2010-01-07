/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: GuiStation.java
 * Author: Asi
 * Date: 23/12/2009
 *
 */
package gui;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author Asi
 *
 */
public class GuiStation {
	private static Map<String, GuiStation> stations = new HashMap<String, GuiStation>();
	private static List<GuiStation> movingStations = new LinkedList<GuiStation>();
	
	// The max diameter size of a new station
	private static final int MAX_DISPLAY_SIZE = 12;
	// The normal diameter size of a station
	private static final int NORMAL_DISPLAY_SIZE = 6;
	// In GUI ticks
	private static final int NEW_STATION_DISPLAY_TIME = 20;
	private static final int DESTROY_STATION_DISPLAY_TIME = 6;
	
	private String stationID;
	private Point stationPosition;
	// in case the station is moving
	private Point stationNewPosition;
	private long creationTime;
	private long destroyedTime;
	private StationState state;
	
	private enum StationState{
		NEW, DESTROYED, MOVING, NORMAL
	}
	
	/**
	 * @return
	 */
	public static List<GuiStation> getAllStations() {
		List<GuiStation> ret = new LinkedList<GuiStation>();
		synchronized (stations) {
			Collection<GuiStation> values = stations.values();
			for (GuiStation guiStation : values) {
				ret.add(new GuiStation(guiStation));
			}
		}

		return ret;
	}
	
	public GuiStation(String id, Point location) {
		this.stationID = new String(id);
		this.stationPosition = new Point(location);
		this.creationTime = GuiTick.getInstance().getTickCount();
		this.state = StationState.NEW;
	}
	
	/**
	 * @param guiStation
	 */
	public GuiStation(GuiStation guiStation) {
		synchronized (stations) {
			this.stationID = new String(guiStation.stationID);
			this.stationPosition = new Point(guiStation.getPosition());
			this.creationTime = guiStation.creationTime;
			this.state = guiStation.state;
		}
	}

	public int getStationDisplaySize() {
		
		long currTickCount = GuiTick.getInstance().getTickCount();
		long stationUpTime = currTickCount - creationTime;
		
		synchronized (stations) {
			switch(this.state) {
			case NEW:	
				if(stationUpTime > NEW_STATION_DISPLAY_TIME) {
					this.state = StationState.NORMAL;
				}
				break;
			case DESTROYED:
				if((currTickCount - destroyedTime) > DESTROY_STATION_DISPLAY_TIME) {
					stations.remove(stationID);
					return 0;
				}
			case MOVING:
			case NORMAL:
			}
			
			double midPoint;
			double sizeFactor;
			
			switch(this.state) {
			case NEW:	
				midPoint = NEW_STATION_DISPLAY_TIME / 2.0;
				sizeFactor = 1.0 - Math.abs((midPoint - (double)stationUpTime) / midPoint);
				return (NORMAL_DISPLAY_SIZE + (int)Math.ceil((MAX_DISPLAY_SIZE - NORMAL_DISPLAY_SIZE) * sizeFactor));
			case DESTROYED:
				sizeFactor = (DESTROY_STATION_DISPLAY_TIME - (currTickCount - destroyedTime)) / (double)DESTROY_STATION_DISPLAY_TIME;
				return (int)Math.ceil(NORMAL_DISPLAY_SIZE * sizeFactor);
			case MOVING:
			case NORMAL:
				return NORMAL_DISPLAY_SIZE;
			}
		}
		
		return 0;
	}

	/**
	 * @return
	 */
	public Point getPosition() {
		Point ret;
		synchronized (stations) {
			ret = new Point(stationPosition);
		}
		return ret;
	}

	/**
	 * @param id
	 * @param location
	 */
	public static void createStation(String id, Point location) {
		synchronized (stations) {
			stations.put(id, new GuiStation(id, location));
		}
	}

	/**
	 * @param id
	 */
	public static void removeStation(String id) {
		synchronized (stations) {
			GuiStation gStation = stations.get(id);
			gStation.state = StationState.DESTROYED;
			gStation.destroyedTime = GuiTick.getInstance().getTickCount();
		}
	}

	/**
	 * @param id
	 * @param location
	 */
	public static void moveStation(String id, Point location) {
		synchronized (stations) {
			GuiStation gStation = stations.get(id);
			gStation.state = StationState.MOVING;
			gStation.stationNewPosition = new Point(location);
			movingStations.add(gStation);
		}	
	}

	/**
	 * 
	 */
	public static void updateStationsAttributes() {
		synchronized (stations) {
			GuiStation gStation;
			for(int i=0; i < movingStations.size(); ++i) {
				gStation = movingStations.get(i);
				if(gStation.stationPosition.equals(gStation.stationNewPosition)) {
					gStation.state = StationState.NORMAL;
					movingStations.remove(gStation);
				}
				else {
					int newX = gStation.stationPosition.x;
					int newY = gStation.stationPosition.y;
					if(newX < gStation.stationNewPosition.x) {
						newX++;
					}
					else if(newX > gStation.stationNewPosition.x) {
						newX--;
					}
					
					if(newY < gStation.stationNewPosition.y) {
						newY++;
					}
					else if(newY > gStation.stationNewPosition.y) {
						newY--;
					}
					gStation.stationPosition.setLocation(newX, newY);
				}
			}
		}
	}
	
}
