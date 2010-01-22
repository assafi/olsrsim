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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Asi
 *
 */
public class WorldTopology extends JPanel {
	private static final long serialVersionUID = -4369922278122645088L;
	private static final Color pointColor = new Color(255,0,0);
	
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
		List<DataSendAttributes> dataSends = DataSendAttributes.getAllDataSends();
		for (DataSendAttributes dataSend : dataSends) {
			drawLine(dataSend.getSource(), dataSend.getDestination(), g2d);
		}
		this.repaint();
	}
	

	/**
	 * @param src
	 * @param dst
	 * @param g2d
	 */
	private void drawLine(Point src, Point dst, Graphics2D g2d) {
		g2d.drawLine(src.x, src.y, dst.x, dst.y);
	}

	private void drawCircle(Point p, int diameter, Graphics2D g) {
		g.drawOval(p.x - diameter/2, p.y - diameter/2, diameter, diameter);
		g.fillOval(p.x - diameter/2, p.y - diameter/2, diameter, diameter);
	}

	
}
