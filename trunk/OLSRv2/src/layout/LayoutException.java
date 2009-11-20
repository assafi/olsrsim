/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: LayoutException.java
 * Author: Assaf
 * Date: 20/11/2009
 *
 */
package layout;

/**
 * @author Assaf
 *
 */
public class LayoutException extends Exception {

	/**
	 * 
	 */
	public LayoutException() {
		super("Layout error");
	}

	/**
	 * @param arg0
	 */
	public LayoutException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public LayoutException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LayoutException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
