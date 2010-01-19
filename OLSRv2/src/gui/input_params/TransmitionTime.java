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

import gui.GUIManager;
import gui.GUIManager.AlertType;
import main.SimulationParameters;

/**
 * @author Asi
 *
 */
public class TransmitionTime extends TextEntry {
	private static final long serialVersionUID = -7429538762729218009L;
	private static final String labelText = "Transmition time:";

	/**
	 * 
	 */
	public TransmitionTime() {
		super(labelText, String.valueOf(SimulationParameters.transmitionTime));
	}

	@Override
	public void updateParamValue() {
		int transmitionTime;
		try {
			transmitionTime = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			GUIManager.getInstance().popAlertMessage("Transmition time must be of type integer", 
					AlertType.ERROR);
			return;
		}
		SimulationParameters.transmitionTime = transmitionTime;
	}

}
