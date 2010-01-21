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

import gui.LayoutTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		this.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(SimulationParameters.LayoutMode.valueOf(getInputValue()) == 
					SimulationParameters.LayoutMode.CLUSTER) {
					LayoutTab.getInstance().setClusterEntries(true);
				}
				else {
					LayoutTab.getInstance().setClusterEntries(false);
				}
			}
		});
	}
	
	@Override
	public void updateParamValue() {
		SimulationParameters.layoutMode = SimulationParameters.LayoutMode.valueOf(this.getInputValue());
	}

}
