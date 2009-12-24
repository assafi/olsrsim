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

/**
 * @author Asi
 *
 */
public class GuiTick {
	// number of milliseconds for a tick
	public static final int GUI_TICK = 50;
	private static GuiTick instance = null;
	private long currentTickCount = 0;
	
	public static GuiTick getInstance() {
		if(instance == null) {
			instance = new GuiTick();
		}
		return instance;
	}
	
	public void tick() {
		currentTickCount++;
	}
	
	public long getTickCount() {
		return currentTickCount;
	}
	
	private GuiTick() {}
	
}
