/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: SimulationParameters.java
 * Author: Asi
 * Date: 21/01/2010
 *
 */
package gui;

import gui.input_params.DataEventsPoissonicRate;
import gui.input_params.InputException;
import gui.input_params.InputParam;
import gui.input_params.MaxStations;
import gui.input_params.ReceptionRadius;
import gui.input_params.SimulationEndTime;
import gui.input_params.SimulationSpeed;
import gui.input_params.StationsSpeed;
import gui.input_params.TopologyPoissonicRate;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Asi
 *
 */
public class SimulationTab extends JPanel {
	private static final long serialVersionUID = 5537757885135458185L;
	
	private List<InputParam> parameters;
	
	/**
	 * 
	 */
	public SimulationTab() {
		this.setBackground(GUIManager.BACKGROUND);
		this.setBorder(new LineBorder(Color.black));
		this.setInputEntries();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(parameters.size(), 1));
		this.add(innerPanel);
		for (InputParam entry : parameters) {
			innerPanel.add(entry);
		}
	}
	
	/**
	 * @throws InputException 
	 * 
	 */
	public void updateParams() throws InputException {
		for (InputParam param : parameters) {
			param.updateParamValue();
		}
	}

	private void setInputEntries() {
		parameters = new LinkedList<InputParam>();
		parameters.add(new SimulationEndTime());
		parameters.add(new MaxStations());
		parameters.add(new ReceptionRadius());
		parameters.add(new TopologyPoissonicRate());
		parameters.add(new DataEventsPoissonicRate());
		parameters.add(new SimulationSpeed());
		parameters.add(new StationsSpeed());
	}

}
