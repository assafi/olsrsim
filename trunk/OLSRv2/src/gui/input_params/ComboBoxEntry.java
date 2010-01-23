/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ComboBoxEntry.java
 * Author: Asi
 * Date: 01/01/2010
 *
 */
package gui.input_params;


import gui.GUIManager;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author Asi
 *
 */
public class ComboBoxEntry extends InputParam {
	private static final long serialVersionUID = -6286788893563544325L;
	
	JComboBox comboBox;
	
	/**
	 * @param parameterText
	 * @param values
	 * @param vertical 
	 */
	public ComboBoxEntry(String parameterText, Object[] values) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel(parameterText);
		comboBox = new JComboBox(values);
		comboBox.setBackground(Color.white);

		JPanel innerPanel;
		label.setForeground(GUIManager.FONT_COLOR);
		setBackground(GUIManager.BACKGROUND);
		innerPanel = new JPanel(new GridLayout(2, 1));
		innerPanel.setBackground(GUIManager.BACKGROUND);
		innerPanel.add(label);
		innerPanel.add(comboBox);
		this.add(innerPanel);
	}

	
	/**
	 * Wrapper to the JComboBox addListener method.
	 * @param listener
	 */
	public void addListener(ActionListener listener) {
		comboBox.addActionListener(listener);
	}

	@Override
	public String getInputValue() {
		return comboBox.getSelectedItem().toString();
	}

	@Override
	public void updateParamValue() {}


}
