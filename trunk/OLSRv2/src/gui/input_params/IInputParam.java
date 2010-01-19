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

/**
 * @author Asi
 *
 */
public interface IInputParam {
	
	/**
	 * Updates the value that was entered by the user into the correct
	 * parameter.
	 */
	public void updateParamValue();
	/**
	 * @return the input given by the user as a string
	 */
	public String getInputValue();
	
}
