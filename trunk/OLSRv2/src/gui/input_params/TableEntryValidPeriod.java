/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TableEntryValidPeriod.java
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
public class TableEntryValidPeriod extends TextEntry {
	private static final long serialVersionUID = 1805589625838775295L;
	private static final String labelText = "Entry validity period:";
	
	/**
	 * 
	 */
	public TableEntryValidPeriod() {
		super(labelText, String.valueOf(SimulationParameters.entryValidPeriod));
	}
	
	@Override
	public void updateParamValue() {
		SimulationParameters.entryValidPeriod = Integer.valueOf(this.getInputValue());
	}
}
