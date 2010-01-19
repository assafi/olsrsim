/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: WorldSize.java
 * Author: Asi
 * Date: 19/01/2010
 *
 */
package gui.input_params;

import gui.GUIManager;

/**
 * @author Asi
 *
 */
public class WorldSize extends TextEntry {
	private static final long serialVersionUID = 768089065478406528L;
	private static final String labelText = "World size:";

	/**
	 * 
	 */
	public WorldSize() {
		super(labelText, "500x500"); // [asi] - change to the correct simulator parameter
	}

	@Override
	public void updateParamValue() {
		String[] worldAttributes = this.getInputValue().split("x");
		GUIManager.getInstance().setWorldDimension(
				Integer.valueOf(worldAttributes[0]), 
				Integer.valueOf(worldAttributes[1]));
	}

}
