/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: GUIManager.java
 * Author: Asi
 * Date: 22/12/2009
 *
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dispatch.Dispatcher;
import dispatch.DispatcherException;

import layout.LayoutException;
import layout.UniformLayout;

import topology.IStation;
import events.Event;
import events.TopologyEvent;
import gui.input_pannels.ComboBoxEntry;

/**
 * @author Asi
 * 
 */
public class GUIManager {
	private static GUIManager instance = null;
	public static final Color BACKGROUND = new Color(200,230,250);
	
	private Thread topologyUpdaterThread;
	private Thread dispatcherThread;
	
	private JFrame mainFrame;
	private SimulationSpeed simulationSpeed = SimulationSpeed.NORMAL;
	private JButton startSimulationButton;
	private JButton stopSimulationButton;
	private ComboBoxEntry simulationSpeedBox;
	
	public static GUIManager getInstance() {
		if(instance == null) {
			instance = new GUIManager();
		}
		return instance;
	}
	
	public enum SimulationSpeed {
		NORMAL, FAST, SLOW
	}
	
	public void createGUI() {
		mainFrame = new JFrame();
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
		startSimulationButton = new JButton("Start simulation");
		stopSimulationButton = new JButton("Stop Simulation");
		
		simulationSpeedBox = new ComboBoxEntry("Simulation speed:",
				stopSimulationButton.getPreferredSize(), SimulationSpeed.values(), false);
		
		bottomPanel.add(simulationSpeedBox);
		bottomPanel.add(startSimulationButton);
		bottomPanel.add(stopSimulationButton);

		mainPanel.add(leftPanel, BorderLayout.LINE_START);
		mainPanel.add(worldPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		addListeners();
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		simulationSpeedBox.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimulationSpeed speed = (SimulationSpeed) simulationSpeedBox.getEntryValue();
				GUIManager.getInstance().setSimulationSpeed(speed);
			}
		});
		
		startSimulationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIManager.getInstance().startSimulation();
			}
		});
		
		stopSimulationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIManager.getInstance().stopSimulation();
			}
		});
	}


	public void startSimulation() {
		System.out.println("Simulation started!!!");
		topologyUpdaterThread.start();
		dispatcherThread.start();
	}
	
	public void stopSimulation() {
		System.out.println("Simulation stoped!!!");
	}

	public SimulationSpeed getSimulationSpeed() {
		return simulationSpeed;
	}
	
	public void setSimulationSpeed(SimulationSpeed speed) {
		if(simulationSpeed != speed) {
			simulationSpeed = speed;
			GuiTick.getInstance().updateSimulationSpeed();			
		}
	}
	
	private GUIManager() {}

	/**
	 * 
	 */
	public void initTopologyThread() {
		Runnable topologyUpdater = new TopologyUpdater();
		topologyUpdaterThread = new Thread(topologyUpdater);
	}
	
	public void initDispatcherThread() {
		Runnable dispatcher = new DispatcherThread();
		dispatcherThread = new Thread(dispatcher);
	}
	
	
}
