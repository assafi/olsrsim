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
package gui.input_params;

import gui.GUIManager;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * @author Asi
 *
 */
public abstract class TextEntry extends InputParam {
	private static final long serialVersionUID = -748916497644881582L;
	
	private JTextField field;
	private JLabel label;
	
	/**
	 * @param parameterText
	 * @param defaultValue
	 */
	public TextEntry(String parameterText, String defaultValue) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(GUIManager.BACKGROUND);
		JPanel innerPanel = new JPanel(new GridLayout(2, 1));
		innerPanel.setBackground(GUIManager.BACKGROUND);
		label = new JLabel(parameterText);
		label.setForeground(GUIManager.FONT_COLOR);
		innerPanel.add(label);
		field = new JTextField(defaultValue);
		innerPanel.add(field);
		this.add(innerPanel);
	}
	
	public String getInputValue() {
		return field.getText();
	}
}
