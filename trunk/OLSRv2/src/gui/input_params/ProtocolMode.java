/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: MPRMode.java
 * Author: Asi
 * Date: 19/01/2010
 *
 */
package gui.input_params;

import main.SimulationParameters;

/**
 * @author Asi
 *
 */
public class ProtocolMode extends ComboBoxEntry {
	private static final long serialVersionUID = -8804483062884521743L;
	private static final String labelText = "Protocol mode:";

	/**
	 * 
	 */
	public ProtocolMode() {
		super(labelText, SimulationParameters.ProtocolMprMode.values(), true);
	}

	@Override
	public void updateParamValue() {
		SimulationParameters.protocolMode = SimulationParameters.ProtocolMprMode.valueOf(this.getInputValue());
	}

}
