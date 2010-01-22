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
import java.awt.Font;
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
	 * @param fieldSize
	 * @param values
	 */
	public ComboBoxEntry(String parameterText, Object[] values, boolean vertical) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel(parameterText);
		label.setForeground(GUIManager.FONT_COLOR);
		comboBox = new JComboBox(values);
		comboBox.setBackground(Color.white);

		JPanel innerPanel;
		if(vertical) {
			setBackground(GUIManager.BACKGROUND);
			innerPanel = new JPanel(new GridLayout(2, 1));
			innerPanel.setBackground(GUIManager.BACKGROUND);
			innerPanel.add(label);
			innerPanel.add(comboBox);
			this.add(innerPanel);
		}
		else {
			this.add(label);
			this.add(comboBox);
		}
	}

	
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
