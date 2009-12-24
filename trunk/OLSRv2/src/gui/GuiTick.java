/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: GuiTick.java
 * Author: Asi
 * Date: 23/12/2009
 *
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * @author Asi
 *
 */
public class GuiTick {
	// number of milliseconds for a tick
	public static final int GUI_TICK = 50;
	private static GuiTick instance = null;
	private long currentTickCount = 0;
	private Timer timer;
	
	public static GuiTick getInstance() {
		if(instance == null) {
			instance = new GuiTick();
		}
		return instance;
	}
	
	private void tick() {
		currentTickCount++;
	}
	
	public long getTickCount() {
		return currentTickCount;
	}
	
	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}
	
	private GuiTick() {
		timer = new Timer(GUI_TICK, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tick();
			}
		});
	}

	/**
	 * 
	 */
	public void stopTime() {
	}
	
}
