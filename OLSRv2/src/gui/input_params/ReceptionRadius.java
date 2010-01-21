/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ReceptionRadius.java
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
public class ReceptionRadius extends TextEntry {
	private static final long serialVersionUID = 3126785646192492755L;
	private static final String labelText = "Reception radius:";

	/**
	 * 
	 */
	public ReceptionRadius() {
		super(labelText, String.valueOf(SimulationParameters.receptionRadius));
	}

	@Override
	public void updateParamValue() throws InputException {
		int receptionRadius;
		try {
			receptionRadius = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("Reception radius must be of type integer");
		}
		SimulationParameters.receptionRadius = receptionRadius;
	}

}
