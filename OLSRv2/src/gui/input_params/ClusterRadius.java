/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ClusterRadius.java
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
public class ClusterRadius extends TextEntry {
	private static final long serialVersionUID = 5055566070743448684L;
	private static final String labelText = "Cluster radius (Cluster mode):";
	
	/**
	 * 
	 */
	public ClusterRadius() {
		super(labelText, String.valueOf(SimulationParameters.clusterRadius));
	}

	
	@Override
	public void updateParamValue() throws InputException {
		int clusterRadius;
		try {
			clusterRadius = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("Cluster radius must be of type integer");
		}
		SimulationParameters.clusterRadius = clusterRadius;
	}

}
