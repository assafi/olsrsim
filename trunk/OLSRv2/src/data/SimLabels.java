/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: SimLabels.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package data;

/**
 * Defines the Column headings in the CSV file.
 * i.e. type of data to log to file
 * @author Assaf
 */
public enum SimLabels {
	VIRTUAL_TIME, NODE_ID, EVENT_TYPE, 
	GLOBAL_SOURCE, LOCAL_SOURCE, LOCAL_TARGET, GLOBAL_TARGET,
	X_COOR, Y_COOR, RADIUS, LOST, ERROR, DETAILS;

	/**
	 * @return Array of the SimLabels names 
	 */
	public static String[] stringify() {
		
		SimLabels[] simLabels = SimLabels.values();
		int numLabels = simLabels.length;
		String[] labels = new String[numLabels];
		
		for (int i = 0; i < numLabels; i++){
			labels[i] = simLabels[i].name();
		}
		return labels;
	}
	
//	public static int size() {
//		return SimLabels.size();
//	}
}
