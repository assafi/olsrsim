/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: LogException.java
 * Author: Assaf
 * Date: 13/11/2009
 *
 */
package log;

/**
 * @author Assaf
 * 
 */
public class LogException extends Exception {

	private static final long serialVersionUID = -8151240805856076477L;

	/**
	 * 
	 */
	public LogException() {
		super("Log error");
	}

	/**
	 * @param errorMsg The error message. will be printed using the getMessage() method.
	 */
	public LogException(String errorMsg) {
		super(errorMsg);
	}

	/**
	 * @param exp Implementation of Throwable, usually another extended Exception object.
	 */
	public LogException(Throwable exp) {
		super(exp);
	}

	/**
	 * @param errorMsg The error message. will be printed using the getMessage() method. 
	 * @param exp Implementation of Throwable, usually another extended Exception object.
	 */
	public LogException(String errorMsg, Throwable exp) {
		super(errorMsg, exp);
	}

}
