/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: IInputParam.java
 * Author: Asi
 * Date: 19/01/2010
 *
 */
package gui.input_params;

import javax.swing.JPanel;

/**
 * @author Asi
 *
 */
public abstract class InputParam extends JPanel{
	private static final long serialVersionUID = -4313663317577581584L;
	
	/**
	 * Updates the value that was entered by the user into the correct
	 * parameter.
	 * @throws InputException 
	 * @throws Exception 
	 */
	public abstract void updateParamValue() throws InputException;
	
	/**
	 * @return the input given by the user as a string.
	 */
	public abstract String getInputValue();
}
