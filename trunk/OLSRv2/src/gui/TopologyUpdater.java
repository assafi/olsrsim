/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: TopologyUpdater.java
 * Author: Asi
 * Date: 02/01/2010
 *
 */
package gui;

import events.Event;
import events.TopologyEvent;
import topology.IStation;

/**
 * @author Asi
 *
 */
public class TopologyUpdater implements Runnable {
	private WorldTopology graphicalTopology;

	@Override
	public void run() {
		GuiEventsQueue queue = GuiEventsQueue.getInstance();
		Event currEvent = null;
		
		try {
			long eventTime, timeDelta, tickCount;
			while (true) {		
				currEvent = queue.popEvent();
				tickCount = GuiTick.getInstance().getTickCount();
				eventTime = currEvent.getTime();
				timeDelta = eventTime - tickCount;
				System.out.println("\nEvent Time: " + eventTime);
				System.out.println("TickCount: " + tickCount);
				if(timeDelta > 0) {
					// In case it was stopped
					GuiTick.getInstance().start();
					// synchronize simulation time with GUI tick time
					Thread.sleep(timeDelta * GuiTick.getInstance().getCurrentTickSize());
				}
				else if(timeDelta < 0) {
					System.out.println("Stoped timer at: " + GuiTick.getInstance().getTickCount());
					GuiTick.getInstance().stop();
				}
				
				if (currEvent.getClass() == TopologyEvent.class) {
					TopologyEvent topologyEvent = (TopologyEvent)currEvent;
					IStation station = topologyEvent.getStation();
					
					System.out.println("Event type: " + topologyEvent.getType().name());
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