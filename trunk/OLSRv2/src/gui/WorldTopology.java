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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Asi
 *
 */
public class WorldTopology extends JPanel {
	private static final long serialVersionUID = -4369922278122645088L;
	private static final Color pointColor = new Color(255,0,0);

	/**
	 * @param width
	 * @param height
	 */
	public WorldTopology() {
		this.setBackground(GUIManager.BACKGROUND);
		this.setBorder(new LineBorder(Color.black));
	}
	
	/**
	 * @param width
	 * @param height
	 */
	public void setWorldSize(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
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
	
}
