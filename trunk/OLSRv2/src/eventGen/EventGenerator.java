/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: EventGenerator.java
 * Author: Assaf
 * Date: Nov 15, 2009
 *
 */
package eventGen;

import dispatch.Dispatcher;

/**
 * @author Assaf
 *
 */
public class EventGenerator {

	private float factor;
	private long nextEventTime;
	private Dispatcher dispatcher = null;
	private static EventGenerator instance = null;
	
	/**
	 * 
	 */
	private EventGenerator(float factor) {
		this.factor = factor;
		this.dispatcher = Dispatcher.getInstance();
		this.nextEventTime = 0;
	}
	
	/**
	 * @param factor An optional parameter specify a factor to take into consideration 
	 * while determine when to generate events. 
	 * @return The EventGenerator singleton
	 */
	public static EventGenerator getInstance(Float factor){
		
		if (null == factor){
			factor = new Float(0.5);
		}
		
		if (null == instance){
			instance = new EventGenerator(factor);
		}
		return instance;
	}

	/**
	 * Virtual time tick
	 */
	public void tick(){
		if (dispatcher.getCurrentVirtualTime() == this.nextEventTime){
			generateEvent();
			nextEventTime();
		}
	}

	/**
	 * 
	 */
	public void generateEvent() {
		
		if (this.nextEventTime == 0){
			createNode();
			return;
		}
		
		// TODO ...
	}
	
	private void nextEventTime(){
		int timeUntilNextEvent = 1;
		
		/*
		 * We've found that the most natural way to schedule events based on a user's
		 * input is to use the Poisson probability. 
		 * Unfortunately there's no easy way calculate the time which the the Pr > 0.5
		 * since the equation is not solvable: t*e^(-factor*t) > 1/(2*factor) in an easy way.
		 */
		
		//TODO Check if this can be solved in an easy way. I think that Marios talked about it in Numerical Analysis !!!
		// Maybe Gamma distribution is a more correct way to solve this.
		for (;poisson(1,factor,timeUntilNextEvent) > 0.5; timeUntilNextEvent++);
		
		this.nextEventTime += timeUntilNextEvent;
	}
	
	private float poisson(int k, float gamma, int t){
		
		/*
		 * We'll be using Math.exp which is fine as long as gamma*t is not a large
		 * negative number. (which is not suppose to be the case here anyway)
		 */
		return (float) ((gamma*t)/Math.exp(gamma*t));
	}
	
}
