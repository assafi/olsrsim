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
package gui.input_pannels;


import gui.Main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author Asi
 *
 */
public class ComboBoxEntry extends InputEntry {
	private static final long serialVersionUID = -6286788893563544325L;
	
	JComboBox comboBox;
	
	/**
	 * @param parameterText
	 * @param fieldSize
	 * @param values
	 */
	public ComboBoxEntry(String parameterText, Dimension fieldSize, String[] values, boolean vertical) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel(parameterText);
		comboBox = new JComboBox(values);
		comboBox.setPreferredSize(fieldSize);
		
		JPanel innerPanel;
		if(vertical) {
			setBackground(Main.BACKGROUND);
			innerPanel = new JPanel(new GridLayout(2, 1));
			innerPanel.setBackground(Main.BACKGROUND);
			innerPanel.add(label);
			innerPanel.add(comboBox);
			this.add(innerPanel);
		}
		else {
			this.add(label);
			this.add(comboBox);
		}
			
	}

	@Override
	public String getEntryValue() {
		return (String)comboBox.getSelectedItem();
	}

}
