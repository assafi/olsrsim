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

import gui.input_pannels.ComboBoxEntry;
import gui.input_pannels.InputEntry;
import gui.input_pannels.TextEntry;

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
public class InputsPanel extends JPanel {
	private static final Dimension ENTRY_SIZE = new Dimension(100, 25);
	private static final String[] SimulationSpeedOptions = {"FAST", "NORMAL", "SLOW"};
	
	private int width, height;
	private List<InputEntry> entries;
	
	public InputsPanel(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Main.BACKGROUND);
		this.setBorder(new LineBorder(Color.black));
		this.setInputEntries();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(entries.size(), 1));
		this.add(innerPanel);
		for (InputEntry entry : entries) {
			innerPanel.add(entry);
		}
	}

	private void setInputEntries() {
		entries = new LinkedList<InputEntry>();
		entries.add(new TextEntry("World size:", ENTRY_SIZE, "500x500"));
		entries.add(new TextEntry("Maximum number of stations:", ENTRY_SIZE, "100"));
		entries.add(new TextEntry("Initial number of stations:", ENTRY_SIZE, "50"));
	}
}
