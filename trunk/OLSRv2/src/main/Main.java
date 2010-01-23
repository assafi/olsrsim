/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: Main.java
 * Author: Asi
 * Date: 01/01/2010
 *
 */
package main;

import gui.GUIManager;


/**
 * @author Asi
 *
 */
public class Main {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Creating the GUI
		GUIManager.getInstance().createGUI();
		// Initializing the topology events thread
		GUIManager.getInstance().initTopologyThread();
		// Initializing the dispatcher thread
		GUIManager.getInstance().initDispatcherThread();	
	}

}
