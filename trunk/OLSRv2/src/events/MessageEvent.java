package events;

import java.util.HashMap;

import log.Log;
import log.LogException;
import data.SimLabels;

/**
 * @author olsr1
 *
 */
public abstract class MessageEvent extends Event {

	private String eventSource;
	
	/**
	 * @param src The originator of the current Message event
	 * @param time The Message timestamp
	 */
	public MessageEvent(String src, long time){
		super(time);
		this.eventSource = src;
	}
	
	/**
	 * @return The originator of the current Message event
	 */
	public String getSource() {
		return eventSource;
	}
	
	/**
	 * 
	 */
	public void setSource(String src) {
		this.eventSource = src; 
	}
	
	public void logEvent(String localDst, String globalSrc, String globalDst) {
		Log log = Log.getInstance();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(SimLabels.VIRTUAL_TIME.name(), Long.toString(this.getTime()));
		data.put(SimLabels.EVENT_TYPE.name(), this.getClass().getName());
		data.put(SimLabels.GLOBAL_SOURCE.name(),globalSrc);
		data.put(SimLabels.GLOBAL_TARGET.name(),globalDst);
		data.put(SimLabels.LOCAL_SOURCE.name(), eventSource);
		data.put(SimLabels.LOCAL_TARGET.name(), localDst);
		try {
			log.writeDown(data);
		} catch (LogException le) {
			System.out.println(le.getMessage());
		}
	}
}
