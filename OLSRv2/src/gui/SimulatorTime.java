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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Asi
 *
 */
public class SimulatorTime extends JPanel {
	private static final long serialVersionUID = -3249725079459887708L;
	private static final Color background = Color.blue;
	private JLabel timeLabel;

	/**
	 * @param width
	 * @param height
	 */
	public SimulatorTime(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		this.setBackground(background);
		this.setBorder( new LineBorder(Color.black));
		this.timeLabel = new JLabel("0");
		timeLabel.setForeground(Color.yellow);
		timeLabel.setFont(new Font("Ariel", Font.BOLD, 16));
		this.add(timeLabel);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		String currentTime = String.valueOf(GuiTick.getInstance().getTickCount());
		this.timeLabel.setText(currentTime);
		repaint();
	}
	
}
