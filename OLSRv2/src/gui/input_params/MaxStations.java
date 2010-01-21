/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: MaxStations.java
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
public class MaxStations extends TextEntry {
	private static final long serialVersionUID = 8542137156718264797L;
	private static final String labelText = "Maximum stations:";
	
	/**
	 * 
	 */
	public MaxStations() {
		super(labelText, String.valueOf(SimulationParameters.maxStations));
	}

	@Override
	public void updateParamValue() throws InputException {
		int maxStations;
		try {
			maxStations = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("Maximum stations must be of type integer");
		}
		SimulationParameters.maxStations = maxStations;
	}

}
