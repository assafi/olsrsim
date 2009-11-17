/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: Station.java
 * Author: Asi
 * Date: 16/11/2009
 *
 */
package topology;

import java.awt.Point;

import protocol.IOLSRv2Protocol;
import protocol.OLSRv2Protocol;

/**
 * @author Asi
 * 
 */
public class Station implements IStation {
	private String stationID;
	private Point location;
	private double recptionRadius;
	private OLSRv2Protocol protocolObject;

	public static double defaultReceptionRadius = -1;
	
	/**
	 * @param stationID
	 * @param location
	 */
	public Station(String stationID, Point location) {
		this.stationID = stationID;
		this.location = location;
		this.recptionRadius = Station.defaultReceptionRadius;
		this.protocolObject = new OLSRv2Protocol(stationID);
	}
	
	public String getID() {
		return stationID;
	}

	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point location) throws Exception {
		if((location.x < 0) || (location.y < 0)) {
			throw new Exception("Illegal location");
		}
		this.location = location;
	}
	
	public IOLSRv2Protocol getOLSRv2Protocol() {
		return protocolObject;
	}

	public void setReceptionRadius(double radius) {
		recptionRadius = radius;
	}
		
	public double getReceptionRadius() {
		return recptionRadius;
	}

	public boolean isInRange(IStation newStation) {
		if(this.location.distance(newStation.getLocation()) <= this.recptionRadius) {
			return true;
		}
		return false;
	}

}



