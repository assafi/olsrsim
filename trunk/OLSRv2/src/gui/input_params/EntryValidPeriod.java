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

import gui.GUIManager;
import gui.GUIManager.AlertType;
import main.SimulationParameters;

/**
 * @author Asi
 *
 */
public class EntryValidPeriod extends TextEntry {
	private static final long serialVersionUID = 1805589625838775295L;
	private static final String labelText = "Entry validity period:";
	
	/**
	 * 
	 */
	public EntryValidPeriod() {
		super(labelText, String.valueOf(SimulationParameters.entryValidPeriod));
	}
	
	@Override
	public void updateParamValue() {
		int entryValidPeriod;
		try {
			entryValidPeriod = Integer.valueOf(this.getInputValue());
		}
		catch (NumberFormatException e) {
			GUIManager.getInstance().popAlertMessage("Entry validity period must be of type integer", 
					AlertType.ERROR);
			return;
		}
		SimulationParameters.entryValidPeriod = entryValidPeriod;
	}
}
