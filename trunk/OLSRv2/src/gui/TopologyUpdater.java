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

import messages.DataMessage;
import events.Event;
import events.SendDataEvent;
import events.StopEvent;
import events.TopologyEvent;
import topology.IStation;

/**
 * @author Asi
 *
 */
public class TopologyUpdater implements Runnable {

	@Override
	public void run() {
		GuiEventsQueue queue = GuiEventsQueue.getInstance();
		Event currEvent = null;
		
		try {
			long eventTime, timeDelta, tickCount;
			
			// pop the first event
			currEvent = queue.popEvent();
			while (currEvent.getClass() != StopEvent.class) {		
				tickCount = GuiTick.getInstance().getTickCount();
				eventTime = currEvent.getTime();
				timeDelta = eventTime - tickCount;
				if(timeDelta > 0) {
					// In case it was stopped
					GuiTick.getInstance().start();
					// synchronize simulation time with GUI tick time
					Thread.sleep(timeDelta * GuiTick.getInstance().getCurrentTickSize());
				}
				else if(timeDelta < 0) {
					GuiTick.getInstance().stop();
				}
							
				if (currEvent.getClass() == TopologyEvent.class) {
					TopologyEvent topologyEvent = (TopologyEvent)currEvent;
					IStation station = topologyEvent.getStation();
					
					System.out.println(topologyEvent.getType().toString());
					
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
				
				if(currEvent.getClass() == DataMessage.class) {
					DataMessage event = (DataMessage)currEvent;
					DataSendAttributes.addDataSend(event.getEventSourceLocation(), event.getLocalDestinationLocation());
				}
				// pop next event
				currEvent = queue.popEvent();
			}
			// making sure the timer stops and the simulation time displays the correct end time
			GuiTick.getInstance().stop();
			GuiTick.getInstance().setTickCount(currEvent.getTime());
			GuiStation.simulationFinished();
			DataSendAttributes.simulationFinished();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
