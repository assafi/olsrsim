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

import main.SimulationParameters.LayoutMode;
import main.SimulationParameters.ProtocolDataSendMode;
import main.SimulationParameters.ProtocolMprMode;
import main.SimulationParameters.StationsMode;
import main.SimulationParameters.StationsSpeed;
import gui.GUIManager;


/**
 * @author Asi
 *
 */
public class Main {
	public static boolean commandLineMode = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length > 0) {
			commandLineMode = true;
		}
		
		if(commandLineMode) {
			SimulationParameters.entryValidPeriod = Integer.valueOf(args[0]);
			SimulationParameters.TCInterval =  Integer.valueOf(args[1]);
			SimulationParameters.HelloInterval = Integer.valueOf(args[2]);
			SimulationParameters.transmitionTime = Integer.valueOf(args[3]);
			SimulationParameters.protocolMode = ProtocolMprMode.valueOf(args[4]);
			SimulationParameters.protocolDataSendMode = ProtocolDataSendMode.valueOf(args[5]);
			SimulationParameters.layoutMode = LayoutMode.valueOf(args[6]);
			SimulationParameters.clusterRadius = Integer.valueOf(args[7]);
			SimulationParameters.clusterNum = Integer.valueOf(args[8]);
			SimulationParameters.stationsMode = StationsMode.valueOf(args[9]);
			SimulationParameters.xBoundry = Integer.valueOf(args[10]);
			SimulationParameters.yBoundry = Integer.valueOf(args[11]);
			SimulationParameters.receptionRadius = Integer.valueOf(args[12]);
			SimulationParameters.simulationEndTime = Integer.valueOf(args[13]);
			SimulationParameters.topologyPoissonicRate = Float.valueOf(args[14]);	
			SimulationParameters.dataEventsPoissonicRate = Float.valueOf(args[15]);
			SimulationParameters.maxStations = Integer.valueOf(args[16]);
			SimulationParameters.stationSpeed = StationsSpeed.valueOf(args[17]);
			SimulationParameters.stationHopDistance = Integer.valueOf(args[18]);
		}
		else {
			// Creating the GUI
			GUIManager.getInstance().createGUI();
		}
		// Initializing the topology events thread
		GUIManager.getInstance().initTopologyThread();
		// Initializing the dispatcher thread
		GUIManager.getInstance().initDispatcherThread();
		
		if(commandLineMode) {
			GUIManager.getInstance().dispatcherThread.start();
		}
	}

}
