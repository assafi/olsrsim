/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: LayoutMode.java
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
public class LayoutMode extends ComboBoxEntry {
	private static final long serialVersionUID = 2250367363809517L;
	private static final String labelText = "Layout mode:";

	/**
	 * 
	 */
	public LayoutMode() {
		super(labelText, SimulationParameters.LayoutMode.values(), true);
	}
	
	@Override
	public void updateParamValue() {
		SimulationParameters.layoutMode = SimulationParameters.LayoutMode.valueOf(this.getInputValue());
	}

}
