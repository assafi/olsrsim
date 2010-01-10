/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TextEntry.java
 * Author: Asi
 * Date: 01/01/2010
 *
 */
package gui.input_pannels;

import gui.GUIManager;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;


/**
 * @author Asi
 *
 */
public class TextEntry extends InputEntry {
	private static final long serialVersionUID = -748916497644881582L;
	
	private JTextField field;
	private JLabel label;
	
	/**
	 * @param parameterText
	 * @param fieldSize
	 * @param defaultValue
	 */
	public TextEntry(String parameterText, Dimension fieldSize, String defaultValue) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(GUIManager.BACKGROUND);
		JPanel innerPanel = new JPanel(new GridLayout(2, 1));
		innerPanel.setBackground(GUIManager.BACKGROUND);
		label = new JLabel(parameterText);
		innerPanel.add(label);
		field = new JTextField(defaultValue);
		field.setPreferredSize(fieldSize);
		
		innerPanel.add(field);
		this.add(innerPanel);
	}
	
	@Override
	public String getEntryValue() {
		return field.getText();
	}
}
