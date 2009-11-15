/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: DispatcherException.java
 * Author: Assaf
 * Date: Nov 15, 2009
 *
 */
package dispatch;

/**
 * @author Assaf
 *
 */
public class DispatcherException extends Exception {

	private static final long serialVersionUID = -1416855103796200971L;

	/**
	 * 
	 */
	public DispatcherException() {
		super("Dispatcher error");
	}

	/**
	 * @param message
	 */
	public DispatcherException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DispatcherException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DispatcherException(String message, Throwable cause) {
		super(message, cause);
	}

}
