/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: SimulationEndTime.java
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
public class SimulationEndTime extends TextEntry {
	private static final long serialVersionUID = -3265671167232005067L;
	private static final String labelText = "Simulation end time:";


	/**
	 * 
	 */
	public SimulationEndTime() {
		super(labelText, String.valueOf(SimulationParameters.simulationEndTime));
	}


	@Override
	public void updateParamValue() throws InputException {
		int simulationEndTime;
		try {
			simulationEndTime = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("Simulation end time must be of type integer");
		}
		SimulationParameters.simulationEndTime = simulationEndTime;
	}

}
