/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: WorldTopology.java
 * Author: Asi
 * Date: 07/12/2009
 *
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import events.TopologyEvent;
import events.TopologyEvent.TopologyEventType;

import topology.IStation;
import topology.ITopologyManager;
import topology.Station;
import topology.TopologyManager;

/**
 * @author Asi
 *
 */
public class WorldTopology extends JPanel {
	private static final long serialVersionUID = -4369922278122645088L;
	private static final Color background = new Color(200,230,250);
	private static final Color pointColor = new Color(255,0,0);
	private static final Color borderColor = new Color(0,0,0);
	private int worldWidth;
	private int worldHeight;

	public WorldTopology(int width, int height) {
		this.worldWidth = width;
		this.worldHeight = height;
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(background);
		this.setBorder(new LineBorder(borderColor));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d= (Graphics2D)g.create();
		g2d.setColor(pointColor);
		List<GuiStation> stations = GuiStation.getAllStations();
		for (GuiStation station : stations) {
			drawCircle(station.getPosition(), station.getStationDisplaySize(), g2d);
		}
		this.repaint();
	}
	
	private void drawCircle(Point p, int diameter, Graphics2D g) {
		g.drawOval(p.x, p.y, diameter, diameter);
		g.fillOval(p.x, p.y, diameter, diameter);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SimulatorTime sTimer = new SimulatorTime();
		WorldTopology wt = new WorldTopology(500, 500);
		mainFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		mainFrame.getContentPane().add(sTimer);
		mainFrame.getContentPane().add(wt);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		// Create the object with the run() method
	    Runnable runnable = new GUIManager();
	    
	    // Create the thread supplying it with the runnable object
	    Thread thread = new Thread(runnable);
	    
	    // Start the thread
	    thread.start();
		
	    GuiTick.getInstance().start();
	    
	    Random rand = new Random();
	    
	    GuiEventsQueue queue = GuiEventsQueue.getInstance();
		long time = 0;
	    while(true) {
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	time += rand.nextInt(30);
	    	int xCord = rand.nextInt(497);
	    	int yCord = rand.nextInt(497);
	    	
	    	queue.addEvent(new TopologyEvent(time, TopologyEventType.NODE_CREATE, 
	    			new Station(new String("" + time + ":" + xCord + "x" + yCord), new Point(xCord,yCord))));
		}
		
	}

}
