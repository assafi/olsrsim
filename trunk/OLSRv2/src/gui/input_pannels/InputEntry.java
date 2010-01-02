/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: InputEntry.java
 * Author: Asi
 * Date: 01/01/2010
 *
 */
package gui.input_pannels;

import javax.swing.JPanel;

/**
 * @author Asi
 *
 */
public abstract class InputEntry extends JPanel {

	/**
	 * @return The entry value as a string.
	 */
	abstract Object getEntryValue();
	
}
