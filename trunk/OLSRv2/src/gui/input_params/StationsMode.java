/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: StationsMode.java
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
public class StationsMode extends ComboBoxEntry {
	private static final long serialVersionUID = 8212235826451182035L;
	private static final String labelText = "Stations Mode:";

	/**
	 * 
	 */
	public StationsMode() {
		super(labelText, SimulationParameters.StationsMode.values(), true);
	}

	@Override
	public void updateParamValue() {
		SimulationParameters.stationsMode = SimulationParameters.StationsMode.valueOf(this.getInputValue());
	}

}
