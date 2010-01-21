/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: InputsPanel.java
 * Author: Asi
 * Date: 01/01/2010
 *
 */
package gui;

import gui.input_params.InputException;
import gui.input_params.InputParam;
import gui.input_params.LayoutMode;
import gui.input_params.WorldSize;

import java.awt.Color;
import java.awt.Dimension;
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
public class LayoutParameters extends JPanel {
	private static final long serialVersionUID = -8962273795833586231L;

	private static final Dimension ENTRY_SIZE = new Dimension(100, 25);
	
	private List<InputParam> parameters;
	
	/**
	 * 
	 */
	public LayoutParameters() {
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
		parameters.add(new WorldSize());
		parameters.add(new LayoutMode());
	}
}
