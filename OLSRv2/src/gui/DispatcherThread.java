/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: DispatcherThread.java
 * Author: Asi
 * Date: 02/01/2010
 *
 */
package gui;

import layout.LayoutException;
import layout.UniformLayout;
import log.Log;
import data.CsvWriter;
import dispatch.Dispatcher;
import dispatch.DispatcherException;

/**
 * @author Asi
 *
 */
public class DispatcherThread implements Runnable {

	private Dispatcher dispatcher;
	
	public DispatcherThread() {
		dispatcher = Dispatcher.getInstance();
	}
	
	@Override
	public void run() {
		try {
			Log.getInstance().createLog(new CsvWriter());
			dispatcher.startSimulation((float) 0.01, new UniformLayout(100,100), 10, 3, 2500);
		} catch (DispatcherException e) {
			e.printStackTrace();
		} catch (LayoutException e) {
			e.printStackTrace();
		}
	}

}
