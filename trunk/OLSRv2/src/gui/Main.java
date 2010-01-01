/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: Main.java
 * Author: Asi
 * Date: 01/01/2010
 *
 */
package gui;

import gui.input_pannels.ComboBoxEntry;
import gui.input_pannels.TextEntry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Asi
 *
 */
public class Main {
	public static final Color BACKGROUND = new Color(200,230,250);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FlowLayout fLayout = new FlowLayout(FlowLayout.CENTER, 20, 20);
		mainFrame.setLayout(fLayout);
		
		JPanel mainPanel = new JPanel(new BorderLayout(20, 10));
		mainFrame.getContentPane().add(mainPanel);
		
		// Creating the left part of the GUI, this part includes the
		// simulation time and the parameters entries.
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		SimulatorTime timerPanel = new SimulatorTime(200, 30);
		InputsPanel inputsPanel = new InputsPanel(200, 450);
		leftPanel.add(timerPanel);
		leftPanel.add(Box.createRigidArea(new Dimension(0,20)));
		leftPanel.add(inputsPanel);
		
		// Creating the world topology panel
		WorldTopology worldPanel = new WorldTopology(500, 500);
		
		// Creating the Start and stop simulation buttons
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
		JButton startSimulation = new JButton("Start simulation");
		JButton stopSimulation = new JButton("Stop Simulation");
		
		String[] simulationSpeedOptions = {"NORMAL", "FAST", "SLOW"};
		ComboBoxEntry simulationSpeed = new ComboBoxEntry("Simulation speed:",
				stopSimulation.getPreferredSize(), simulationSpeedOptions, false);
		
		bottomPanel.add(simulationSpeed);
		bottomPanel.add(startSimulation);
		bottomPanel.add(stopSimulation);

		mainPanel.add(leftPanel, BorderLayout.LINE_START);
		mainPanel.add(worldPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
		
	}

}
