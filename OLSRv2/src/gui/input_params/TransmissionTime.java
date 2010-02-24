/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TransmitionTime.java
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
public class TransmissionTime extends TextEntry {
	private static final long serialVersionUID = -7429538762729218009L;
	private static final String labelText = "Transmission time:";

	/**
	 * 
	 */
	public TransmissionTime() {
		super(labelText, String.valueOf(SimulationParameters.transmissionTime));
	}

	@Override
	public void updateParamValue() throws InputException {
		int transmissionTime;
		try {
			transmissionTime = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("Transmission time must be of type integer");
		}
		SimulationParameters.transmissionTime = transmissionTime;
	}

}
