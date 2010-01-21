/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ProtocolDataSendMode.java
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
public class ProtocolDataSendMode extends ComboBoxEntry {
	private static final long serialVersionUID = -8270052049723749371L;
	private static final String labelText = "Protocol data send mode:";

	/**
	 * 
	 */
	public ProtocolDataSendMode() {
		super(labelText, SimulationParameters.ProtocolDataSendMode.values(), true);
	}

	@Override
	public void updateParamValue() {
		SimulationParameters.protocolDataSendMode = SimulationParameters.ProtocolDataSendMode.valueOf(this.getInputValue());
	}
}
