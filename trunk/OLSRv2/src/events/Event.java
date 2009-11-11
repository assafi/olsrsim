package events;

public abstract class Event {
	private long time;
	
	public Event(long time){
		this.time = time;
	}

	public long getTime() {
		return time;
	}
}
 