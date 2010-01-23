/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: SimulationSpeed.java
 * Author: Asi
 * Date: 23/01/2010
 *
 */
package gui.input_params;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.GUIManager;


/**
 * @author Asi
 *
 */
public class SimulationSpeed extends ComboBoxEntry {
	private static final long serialVersionUID = -7314374298883614607L;
	private static final String labelText = "Simulation speed:";

	/**
	 * 
	 */
	public SimulationSpeed() {
		super(labelText, gui.GUIManager.SimulationSpeed.values(), true);
		this.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateParamValue();
			}
		});
	}
	
	@Override
	public void updateParamValue() {
		gui.GUIManager.SimulationSpeed speed = gui.GUIManager.SimulationSpeed.valueOf(this.getInputValue());
		GUIManager.getInstance().setSimulationSpeed(speed);
	}

}
