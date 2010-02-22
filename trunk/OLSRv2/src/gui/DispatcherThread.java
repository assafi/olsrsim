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

import java.sql.SQLException;

import log.Log;
import log.sqlproxy.SqlProxyException;
import main.Main;
import data.CsvWriter;
import dispatch.Dispatcher;
import dispatch.DispatcherException;

/**
 * @author Asi
 *
 */
public class DispatcherThread implements Runnable {
	private static DispatcherThread instance = null;
	private Dispatcher dispatcher;
	
	
	/**
	 * @return An instance of type DispatcherThread
	 */
	public static DispatcherThread getInstance() {
		if(null == instance) {
			instance = new DispatcherThread();
		}
		return instance;
	}
	
	/**
	 * 
	 */
	private DispatcherThread() {
		dispatcher = Dispatcher.getInstance();
	}
	
	/**
	 * 
	 */
	public void notifySimulationEnded() {
		synchronized (this) {
			notify();
		}
	}
	
	@Override
	public void run() {
		try {
			Log.getInstance().createLog(new CsvWriter());
			dispatcher.startSimulation();
			if(!Main.commandLineMode) {
				synchronized (this) {
					wait();
				}
				GUIManager.getInstance().popAlertMessage("Simulation Ended Successfully", AlertType.NORMAL);
			}
			// Writing the log file into the database
			Log.getInstance().updateDB();
			Log.getInstance().close();
			if(!Main.commandLineMode) {
				GUIManager.getInstance().popAlertMessage("Simulation information was written to the database", AlertType.NORMAL);
			}
		} catch (DispatcherException e) {
			GUIManager.getInstance().popAlertMessage(e.getMessage(), AlertType.FATAL);
			Log.getInstance().close();
		} catch (SQLException e) {
			Log.getInstance().close();
			String message = "Error occured while writing simulation results to the database";
			GUIManager.getInstance().popAlertMessage(message, AlertType.FATAL);
		} catch (SqlProxyException e) {
			Log.getInstance().close();
			String message = "Error occured while trying to connect to database";
			GUIManager.getInstance().popAlertMessage(message, AlertType.FATAL);
		} catch (Exception e) {
			Log.getInstance().close();
			e.printStackTrace();
			GUIManager.getInstance().popAlertMessage("Internal error occured, simulation failed", AlertType.FATAL);
		}
	}
}
