/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: StationsSpeed.java
 * Author: Asi
 * Date: 22/02/2010
 *
 */
package gui.input_params;

import main.SimulationParameters;

/**
 * @author Asi
 *
 */
public class StationsSpeed extends ComboBoxEntry {
	private static final long serialVersionUID = 8970279564405383134L;
	private static final String labelText = "Stations speed:";

	/**
	 * 
	 */
	public StationsSpeed() {
		super(labelText, SimulationParameters.StationsSpeed.values());
	}
	
	@Override
	public void updateParamValue() {
		SimulationParameters.stationSpeed = SimulationParameters.StationsSpeed.valueOf(this.getInputValue());
	}

}
