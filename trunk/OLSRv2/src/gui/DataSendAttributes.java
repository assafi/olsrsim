/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: DataSendAttributes.java
 * Author: Asi
 * Date: 22/01/2010
 *
 */
package gui;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import main.SimulationParameters;

/**
 * @author Asi
 *
 */
public class DataSendAttributes {
	private Point src;
	private Point dst;
	private int displayTicksLeft;
	
	private static List<DataSendAttributes> allDataSends = new LinkedList<DataSendAttributes>();
	
	/**
	 * @return A duplicated list of all messages that needs to be displayed
	 */
	public static List<DataSendAttributes> getAllDataSends() {
		List<DataSendAttributes> ret = new LinkedList<DataSendAttributes>();
		synchronized (allDataSends) {
			for (DataSendAttributes dataSendAttributes : allDataSends) {
				ret.add(new DataSendAttributes(dataSendAttributes.getSource(), 
						dataSendAttributes.getDestination(), 
						dataSendAttributes.getDisplayTicksLeft()));
			}
		}
		return ret;
	}
	
	/**
	 * @param src
	 * @param dst
	 */
	public static void addDataSend(Point src, Point dst) {
		synchronized (allDataSends) {
			allDataSends.add(new DataSendAttributes(src, dst, SimulationParameters.transmissionTime));
		}
	}
	
	/**
	 * 
	 */
	public static void updateDataSend() {
		synchronized (allDataSends) {
			List<DataSendAttributes> removeDataSends = new LinkedList<DataSendAttributes>();
			for (DataSendAttributes dataSend : allDataSends) {
				dataSend.decreaseDisplayTickCount();
				if(dataSend.getDisplayTicksLeft() == 0) {
					removeDataSends.add(dataSend);
				}
			}
			for (DataSendAttributes dataSend : removeDataSends) {
				allDataSends.remove(dataSend);
			}
		}
	}
	
	/**
	 * @param src 
	 * @param dst 
	 * @param displayTicks 
	 */
	public DataSendAttributes(Point src, Point dst, int displayTicks) {
		this.src = new Point(src);
		this.dst = new Point(dst);
		this.displayTicksLeft = displayTicks;
	}

	/**
	 * 
	 */
	public void decreaseDisplayTickCount() {
		this.displayTicksLeft--;
	}

	/**
	 * @return the number of ticks this message still needs to be displayed.
	 */
	public int getDisplayTicksLeft() {
		return displayTicksLeft;
	}

	/**
	 * @return the destination of this message.
	 */
	public Point getDestination() {
		return dst;
	}
	
	/**
	 * @return the source of this message.
	 */
	public Point getSource() {
		return src;
	}

	/**
	 * 
	 */
	public static void simulationFinished() {
		synchronized (allDataSends) {
			allDataSends.clear();
		}
	}
	
}
