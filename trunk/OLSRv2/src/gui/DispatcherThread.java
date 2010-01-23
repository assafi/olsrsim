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

import gui.GUIManager.AlertType;
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
	
	/**
	 * 
	 */
	public DispatcherThread() {
		dispatcher = Dispatcher.getInstance();
	}
	
	@Override
	public void run() {
		try {
			Log.getInstance().createLog(new CsvWriter());
			dispatcher.startSimulation();
		} catch (DispatcherException e) {
			GUIManager.getInstance().popAlertMessage(e.getMessage(), AlertType.ERROR);
		} catch (Exception e) {
			GUIManager.getInstance().popAlertMessage("Internal error occured, simulation failed", AlertType.ERROR);
			e.printStackTrace();
		}
		// Writing the log file into the database
		Log.getInstance().updateDB();
		Log.getInstance().close();
		System.exit(-1);
	}

}
