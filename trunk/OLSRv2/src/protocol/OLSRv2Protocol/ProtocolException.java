/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: ProtocolException.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package protocol.OLSRv2Protocol;

/**
 * @author Eli Nazarov
 *
 */
public class ProtocolException extends Exception {

	private static final long serialVersionUID = 1326345953497198129L;

	public ProtocolException() {
		super("Protocol error");
	}

	/**
	 * @param errorMsg The error message. will be printed using the getMessage() method.
	 */
	public ProtocolException(String errorMsg) {
		super(errorMsg);
	}

	/**
	 * @param exp Implementation of Throwable, usually another extended Exception object.
	 */
	public ProtocolException(Throwable exp) {
		super(exp);
	}

	/**
	 * @param errorMsg The error message. will be printed using the getMessage() method. 
	 * @param exp Implementation of Throwable, usually another extended Exception object.
	 */
	public ProtocolException(String errorMsg, Throwable exp) {
		super(errorMsg, exp);
	}
 
}
