/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ProtocolParameters.java
 * Author: Asi
 * Date: 10/01/2010
 *
 */
package gui;

import gui.input_params.HelloInterval;
import gui.input_params.InputException;
import gui.input_params.InputParam;
import gui.input_params.ProtocolDataSendMode;
import gui.input_params.ProtocolMode;
import gui.input_params.TCInterval;
import gui.input_params.EntryValidPeriod;
import gui.input_params.TransmissionTime;

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
public class ProtocolTab extends JPanel {
	private static final long serialVersionUID = -4190880490292837168L;;
	
	private List<InputParam> parameters;
	
	/**
	 * 
	 */
	public ProtocolTab() {
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
		parameters.add(new EntryValidPeriod());
		parameters.add(new HelloInterval());
		parameters.add(new TCInterval());
		parameters.add(new TransmissionTime());
		parameters.add(new ProtocolMode());
		parameters.add(new ProtocolDataSendMode());
	}
}
