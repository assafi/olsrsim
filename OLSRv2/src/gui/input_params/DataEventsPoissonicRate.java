/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: DataEventsPoissonicRate.java
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
public class DataEventsPoissonicRate extends TextEntry {
	private static final long serialVersionUID = 278397558477221365L;
	private static final String labelText = "Data events poissonic rate:";

	/**
	 * 
	 */
	public DataEventsPoissonicRate() {
		super(labelText, String.valueOf(SimulationParameters.dataEventsPoissonicRate));
	}

	@Override
	public void updateParamValue() throws InputException {
		float dataEventsPoissonicRate;
		try {
			dataEventsPoissonicRate = Float.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("Data events poissonic rate must be of type float");
		}
		SimulationParameters.dataEventsPoissonicRate = dataEventsPoissonicRate;
	}

}
