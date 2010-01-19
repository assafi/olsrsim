/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: InputException.java
 * Author: Asi
 * Date: 19/01/2010
 *
 */
package gui.input_params;

/**
 * @author Asi
 *
 */
public class InputException extends Exception {
	private static final long serialVersionUID = -7113053063630124961L;

	/**
	 * @param message
	 */
	public InputException(String message) {
		super(message);
	}
}
