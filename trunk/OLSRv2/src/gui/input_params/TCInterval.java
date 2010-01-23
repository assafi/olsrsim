/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TCInterval.java
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
public class TCInterval extends TextEntry {
	private static final long serialVersionUID = -725819449484038512L;
	private static final String labelText = "TC message interval:";
	
	/**
	 * 
	 */
	public TCInterval() {
		super(labelText,  String.valueOf(SimulationParameters.TCInterval));
	}

	@Override
	public void updateParamValue() throws InputException {
		int TCInterval;
		try {
			TCInterval = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			throw new InputException("TC message interval must be of type integer");
		}
		SimulationParameters.TCInterval = TCInterval;
	}

}
