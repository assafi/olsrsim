/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ClusterNum.java
 * Author: Asi
 * Date: 21/01/2010
 *
 */
package gui.input_params;

import main.SimulationParameters;

/**
 * @author Asi
 *
 */
public class ClusterNum extends TextEntry {
	private static final long serialVersionUID = 1763485994023944783L;
	private static final String labelText = "Number of clusters:";

	/**
	 * 
	 */
	public ClusterNum() {
		super(labelText, String.valueOf(SimulationParameters.clusterNum));
	}

	@Override
	public void updateParamValue() throws InputException {
		int numberOfClusters;
		try {
			numberOfClusters = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("Number of clusters must be of type integer");
		}
		SimulationParameters.clusterNum = numberOfClusters;
	}

}
