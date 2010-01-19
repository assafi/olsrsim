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

import java.sql.SQLException;

import layout.ClustersLayout;
import layout.LayoutException;
import layout.UniformLayout;
import log.Log;
import log.dataserver.SqlWriter;
import log.sqlproxy.SqlProxyException;
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
			dispatcher.startSimulation();
		} catch (DispatcherException e) {
			e.printStackTrace();
		} catch (LayoutException e) {
			e.printStackTrace();
		}
	}

}
