/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TopologyPoissonicRate.java
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
public class TopologyPoissonicRate extends TextEntry {
	private static final long serialVersionUID = -7903278608483970814L;
	private static final String labelText = "Topology poissonic rate:";

	/**
	 * 
	 */
	public TopologyPoissonicRate() {
		super(labelText, String.valueOf(SimulationParameters.topologyPoissonicRate));
	}

	@Override
	public void updateParamValue() throws InputException {
		float topologyPoissonicRate;
		try {
			topologyPoissonicRate = Float.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("Topology poissonic rate must be of type float");
		}
		SimulationParameters.topologyPoissonicRate = topologyPoissonicRate;
	}

}
