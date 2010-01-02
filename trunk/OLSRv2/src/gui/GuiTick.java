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

import gui.GUIManager.SimulationSpeed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

/**
 * @author Asi
 *
 */
public class GuiTick {
	
	private static GuiTick instance = null;
	private long currentTickCount = 0;
	private Timer timer;
	private int currentTickSize;
	
	public static final Map<SimulationSpeed, Integer> tickSpeeds = new HashMap<SimulationSpeed, Integer>() {
		{
			// number of milliseconds for a tick
			put(SimulationSpeed.FAST, 20);
			put(SimulationSpeed.NORMAL, 50);
			put(SimulationSpeed.SLOW, 100);
		}
	};
	
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
		timer = new Timer(tickSpeeds.get(GUIManager.getInstance().getSimulationSpeed()), new ActionListener() {
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

	/**
	 * 
	 */
	public synchronized void updateSimulationSpeed() {
		SimulationSpeed speed = GUIManager.getInstance().getSimulationSpeed();
		currentTickSize = tickSpeeds.get(speed);
		timer.setDelay(currentTickSize);
	}
	
	public synchronized int getCurrentTickSize() {
		return currentTickSize;
	}
	
}
