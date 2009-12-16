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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import topology.ITopologyManager;
import topology.Station;
import topology.TopologyManager;

/**
 * @author Asi
 *
 */
public class WorldTopology extends JPanel {
	private static final int DIAMETER = 6;
	private static final long serialVersionUID = -4369922278122645088L;
	private ITopologyManager topology;
	private int worldWidth;
	private int worldHeight;

	public WorldTopology(ITopologyManager topology, int width, int height) {
		this.topology = topology;
		this.worldWidth = width;
		this.worldHeight = height;
		this.setPreferredSize(new Dimension(width, height));
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d= (Graphics2D)g; 
		Set<Point> stations = topology.getAllStationsPositions();
		for (Point point : stations) {
			drawCircle(point, DIAMETER, g2d);
		}
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
		ITopologyManager topology = new TopologyManager();
		try {
			Station.defaultReceptionRadius = 30;
			topology.createNewStation("Asi1", new Point(20,20));
			topology.createNewStation("Asi2", new Point(200,50));
			topology.createNewStation("Asi3", new Point(60,300));
			topology.createNewStation("Asi4", new Point(100,200));
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().add(new WorldTopology(topology, 500, 500));
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

}
