/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ReceivedMessageInformationBase.java
 * Author: Eli Nazarov
 * Date: Nov 18, 2009
 *
 */
package protocol.InformationBases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Eli Nazarov
 *
 */
public class ReceivedMessageInformationBase {
	
	/**
	 * Mapping between the station that send messages
	 * and the details of the received message.
	 * 
	 * TODO see if this statement is correct
	 * Each message that we receive in the simulator we process so
	 * this set has all the information in the Process set.
	 * 
	 */
	private HashMap<String, ArrayList<ReceivedSetData>> receivedSet = null;
	
	/**
	 * Mapping between the station that send messages
	 * and the details of the forward message.
	 *
	 */
	private HashMap<String, ArrayList<ReceivedSetData>> forwardSet = null;

	public ReceivedMessageInformationBase(){
		receivedSet = new HashMap<String, ArrayList<ReceivedSetData>>();
		forwardSet = new HashMap<String, ArrayList<ReceivedSetData>>();
	}
	
	/**
	 * @return the receivedSet
	 */
	public HashMap<String, ArrayList<ReceivedSetData>> getReceivedSet() {
		return receivedSet;
	}

	/**
	 * @return the forwardSet
	 */
	public HashMap<String, ArrayList<ReceivedSetData>> getForwardSet() {
		return forwardSet;
	}
	
	private void setCleaner(HashMap<String, ArrayList<ReceivedSetData>> set, long currTime){
		ArrayList<ReceivedSetData> keysToRemove1 = new ArrayList<ReceivedSetData>();
		ArrayList<String> keysToRemove2 = new ArrayList<String>();
		
		
		for (Entry<String,ArrayList<ReceivedSetData>> entry : set.entrySet()) {
			for (ReceivedSetData data : entry.getValue()) {
				if(data.getTTL() < currTime){
					keysToRemove1.add(data);
				}
			}
			entry.getValue().removeAll(keysToRemove1);
		}
		
		//delete all entries with empty lists
		for (String key : set.keySet()) {
			if(set.get(key).isEmpty()){
				keysToRemove2.add(key);
			}
		}
		
		for (String key : keysToRemove2) {
			set.remove(key);
		}
	}
	
	public void clearExpiredEntries(long currTime){		
		//clear Received Set
		setCleaner(receivedSet, currTime);
		//clear Forward Set
		setCleaner(forwardSet, currTime);
	}

}
