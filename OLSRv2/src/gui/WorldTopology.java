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
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

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
	private static final Color background = new Color(200,230,250);
	private static final Color pointColor = new Color(255,0,0);
	private static final Color borderColor = new Color(0,0,0);
	private ITopologyManager topology;
	private int worldWidth;
	private int worldHeight;

	public WorldTopology(ITopologyManager topology, int width, int height) {
		this.topology = topology;
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
		
		Random rand = new Random();
	
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
		WorldTopology wt = new WorldTopology(topology, 500, 500);
		mainFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		mainFrame.getContentPane().add(wt);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		while(true) {
			int nextStationXPosition = rand.nextInt(497);
			int nextStationYPosition = rand.nextInt(497);
			
			if(topology.doesStationExist(new Point(nextStationXPosition, nextStationYPosition))) {
				continue;
			}
			
			String stationID = topology.getRandomStation();
			Point currPosition = topology.getStationPosition(stationID);
		
			while(true) {
		
				if(currPosition.x < nextStationXPosition) {
					currPosition.x++;
				}
				else if(currPosition.x > nextStationXPosition){
					currPosition.x--;
				}
				
				if(currPosition.y < nextStationYPosition) {
					currPosition.y++;
				}
				else if(currPosition.y > nextStationYPosition){
					currPosition.y--;
				}
					
				try {
					if(topology.doesStationExist(currPosition)) {
						continue;
					}
					else {
						topology.changeStationPosition(stationID, currPosition);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				wt.repaint();
				if((currPosition.x == nextStationXPosition) && (currPosition.y == nextStationYPosition)) {
					break;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
