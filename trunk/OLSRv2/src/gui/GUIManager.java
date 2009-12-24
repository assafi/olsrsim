/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: GUIManager.java
 * Author: Asi
 * Date: 22/12/2009
 *
 */
package gui;

import topology.IStation;
import events.Event;
import events.TopologyEvent;

/**
 * @author Asi
 * 
 */
public class GUIManager implements Runnable {
	private WorldTopology graphicalTopology;
	
	public GUIManager() {}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		GuiEventsQueue queue = GuiEventsQueue.getInstance();
		Event currEvent = null;
		try {
			long timeDelta;
			while (true) {		
				currEvent = queue.popEvent();
				timeDelta = currEvent.getTime() - GuiTick.getInstance().getTickCount();
				System.out.println("Latest Event Time: " + currEvent.getTime());
				if(timeDelta > 0) {
					// In case it was stopped
					GuiTick.getInstance().start();
					// synchronize simulation time with GUI tick time
					Thread.sleep(timeDelta * GuiTick.GUI_TICK);
				}
				else if(timeDelta < 0) {
					GuiTick.getInstance().stop();
				}
				
				if (currEvent.getClass() == TopologyEvent.class) {
					TopologyEvent topologyEvent = (TopologyEvent)currEvent;
					IStation station = topologyEvent.getStation();
					switch (topologyEvent.getType()) {
					case NODE_CREATE:	
						GuiStation.createStation(station.getID(), station.getLocation());
						break;
					case NODE_DESTROY:	
						GuiStation.removeStation(station.getID());
						break;
					case NODE_MOVE:		
						GuiStation.moveStation(station.getID(), station.getLocation());
						break;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
