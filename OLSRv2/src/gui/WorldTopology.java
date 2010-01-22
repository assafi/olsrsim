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
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Asi
 *
 */
public class WorldTopology extends JPanel {
	private static final long serialVersionUID = -4369922278122645088L;
	private static final Color STATION_COLOR = Color.blue;
	private static final Color DATA_MESSAGE_COLOR = Color.yellow;
	private Image backgroundImage;
	
	public WorldTopology() {
		this.setBackground(GUIManager.BACKGROUND);
		this.setBorder(new LineBorder(Color.black));
		try {
			URL imageURL = getClass().getResource("../world_background.jpg");
			this.backgroundImage = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
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
		g.drawImage(backgroundImage, 0, 0, null);
		Graphics2D g2d= (Graphics2D)g.create();
		g2d.setColor(STATION_COLOR);
		List<GuiStation> stations = GuiStation.getAllStations();
		for (GuiStation station : stations) {
			drawCircle(station.getPosition(), station.getStationDisplaySize(), g2d);
		}
		g2d.setColor(DATA_MESSAGE_COLOR);
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
