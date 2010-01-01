/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: SimulatorTime.java
 * Author: Asi
 * Date: 24/12/2009
 *
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Asi
 *
 */
public class SimulatorTime extends JPanel {
	private static final Color background = new Color(150,10,100);
	private int panelWidth = 50;
	private int panelHeight = 30;
	/**
	 * 
	 */
	public SimulatorTime() {
		this.setPreferredSize(new Dimension(panelWidth, panelHeight));
		this.setBackground(background);
		this.setBorder(new LineBorder(Color.black));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d= (Graphics2D)g.create();
		
		g2d.drawString(String.valueOf(GuiTick.getInstance().getTickCount()), 10, 10);
		this.repaint();
	}
	
}
