/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: HelloInterval.java
 * Author: Asi
 * Date: 19/01/2010
 *
 */
package gui.input_params;

import main.SimulationParameters;

/**
 * @author Asi
 *
 */
public class HelloInterval extends TextEntry {
	private static final long serialVersionUID = -2831521249759133237L;
	private static final String labelText = "Hello message interval:";

	/**
	 * 
	 */
	public HelloInterval() {
		super(labelText, String.valueOf(SimulationParameters.HelloInterval));
	}
	
	@Override
	public void updateParamValue() {
		SimulationParameters.HelloInterval = Integer.valueOf(this.getInputValue());
	}
}
