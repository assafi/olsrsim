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

import gui.input_params.ComboBoxEntry;
import gui.input_params.InputException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import main.SimulationParameters;

/**
 * @author Asi
 * 
 */
public class GUIManager {

	public static final Color BACKGROUND = Color.black;
	public static final Color FONT_COLOR = Color.white;
	public static final Dimension MAX_WORLD_SIZE = new Dimension(500,500);
	public static final Dimension MIN_WORLD_SIZE = new Dimension(100,100);
	private static GUIManager instance = null;

	private Thread topologyUpdaterThread;
	private Thread dispatcherThread;
	
	private JPanel worldOuterPanel;
	private FlowLayout worldFlowLayout;
	
	private JFrame mainFrame;
	private WorldTopology worldTopology;
	private SimulationTab simulationParams;
	private ProtocolTab protocolParams;
	
	private SimulationSpeed simulationSpeed = SimulationSpeed.NORMAL;
	private JButton applyParametersButton;
	private JButton startSimulationButton;
	private JButton stopSimulationButton;
	private ComboBoxEntry simulationSpeedBox;
	
	public enum AlertType {
		ERROR, WARNING
	}
	
	public enum SimulationSpeed {
		NORMAL, FAST, SLOW, REAL_TIME
	}
	
	/**
	 * @return An instance of GUIManager
	 */
	public static GUIManager getInstance() {
		if(instance == null) {
			instance = new GUIManager();
		}
		return instance;
	}
	
	/**
	 * 
	 */
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
		SimulatorTime timerPanel = new SimulatorTime(220, 30);
		
		UIManager.put("TabbedPane.selected", new Color(248, 217, 133));
		UIManager.put("TabbedPane.borderHightlightColor", Color.white);
		
		JTabbedPane inputsTabbedPane = new JTabbedPane();
		inputsTabbedPane.setBackground(BACKGROUND);
		inputsTabbedPane.setForeground(Color.white);
		

		inputsTabbedPane.setPreferredSize(new Dimension(220,450));
		
		simulationParams = new SimulationTab();
		protocolParams = new ProtocolTab();
		inputsTabbedPane.addTab("simulation", simulationParams);
		inputsTabbedPane.addTab("layout", LayoutTab.getInstance());
		inputsTabbedPane.addTab("protocol", protocolParams);
		leftPanel.add(timerPanel);
		leftPanel.add(Box.createRigidArea(new Dimension(0,20)));
		leftPanel.add(inputsTabbedPane);
		
		// Creating the world topology panel
		worldOuterPanel = new JPanel();
		worldOuterPanel.setPreferredSize(new Dimension(MAX_WORLD_SIZE.width, MAX_WORLD_SIZE.width));
		worldFlowLayout = new FlowLayout(FlowLayout.CENTER);
		worldTopology = new WorldTopology();
		setWorldDimension(SimulationParameters.xBoundry, SimulationParameters.yBoundry);
		worldOuterPanel.add(worldTopology);
		
		// Creating the Start and stop simulation buttons
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
		applyParametersButton = new JButton("Apply Parameters");
		startSimulationButton = new JButton("Start Simulation");
		startSimulationButton.setEnabled(false);
		stopSimulationButton = new JButton("Stop Simulation");
		stopSimulationButton.setEnabled(false);
		
		simulationSpeedBox = new ComboBoxEntry("Simulation speed:",
				SimulationSpeed.values(), false);
		
		bottomPanel.add(applyParametersButton);
		bottomPanel.add(simulationSpeedBox);
		bottomPanel.add(startSimulationButton);
		bottomPanel.add(stopSimulationButton);

		mainPanel.add(leftPanel, BorderLayout.LINE_START);
		mainPanel.add(worldOuterPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		addListeners();
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		applyParametersButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIManager.getInstance().applyParameters();
			}
		});
		
		simulationSpeedBox.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimulationSpeed speed = SimulationSpeed.valueOf(simulationSpeedBox.getInputValue());
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


	/**
	 * 
	 */
	public void applyParameters() {
		try {
			simulationParams.updateParams();
			LayoutTab.getInstance().updateParams();
			protocolParams.updateParams();
		} catch (InputException e) {
			startSimulationButton.setEnabled(false);
			popAlertMessage(e.getMessage(), AlertType.ERROR);
			return;
		}
		startSimulationButton.setEnabled(true);
	}

	/**
	 * 
	 */
	public void startSimulation() {
		startSimulationButton.setEnabled(false);
		topologyUpdaterThread.start();
		dispatcherThread.start();
		stopSimulationButton.setEnabled(true);
	}
	
	/**
	 * 
	 */
	public void stopSimulation() {
		System.out.println("Simulation stoped!!! - not implemented yet");
	}

	/**
	 * @return The simulation speed
	 */
	public SimulationSpeed getSimulationSpeed() {
		return simulationSpeed;
	}
	
	/**
	 * @param speed
	 */
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
	
	/**
	 * 
	 */
	public void initDispatcherThread() {
		Runnable dispatcher = new DispatcherThread();
		dispatcherThread = new Thread(dispatcher);
	}
	
	/**
	 * @param width
	 * @param height
	 */
	public void setWorldDimension(int width, int height) {
		worldTopology.setWorldSize(width, height);
		worldFlowLayout.setHgap((MAX_WORLD_SIZE.width - width)/2);
		worldFlowLayout.setVgap((MAX_WORLD_SIZE.height - height)/2);
		worldOuterPanel.setLayout(worldFlowLayout);
		worldOuterPanel.updateUI();
	}
	
	public void popAlertMessage(String message, AlertType type) {
		JOptionPane p = new JOptionPane(message);
		if(type == AlertType.ERROR) {
			p.setMessageType(JOptionPane.ERROR_MESSAGE);
		}
		else if(type == AlertType.WARNING) {
			p.setMessageType(JOptionPane.WARNING_MESSAGE);
		}
		Object[] o = new String[] { "OK" };
		p.setOptions(o);
		JFrame popupFrame = new JFrame();
		popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JDialog d = p.createDialog(popupFrame, null);
		d.pack();
		d.setVisible(true);
		popupFrame.dispose();
	}
	
}
