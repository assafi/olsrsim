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
import gui.GUIManager.AlertType;

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
		GUIManager GUIManagerInstance = GUIManager.getInstance();
		String[] worldAttributes = this.getInputValue().split("x");
		if((worldAttributes.length != 2)) {
			GUIManagerInstance.popAlertMessage(
					"World size input should be in the form of <width>x<height>", 
					AlertType.ERROR);
			return;
		}
		int width = 0, height = 0;
		try {
			width = Integer.valueOf(worldAttributes[0]);
			height = Integer.valueOf(worldAttributes[1]);
		}
		catch (NumberFormatException e) {
			GUIManagerInstance.popAlertMessage("World size width or height are not numeric", 
					AlertType.ERROR);
			return;
		}
		if((width > GUIManager.MAX_WORLD_SIZE.width) || (width < GUIManager.MIN_WORLD_SIZE.width)) {
			GUIManagerInstance.popAlertMessage("World width must be between " +
					GUIManager.MIN_WORLD_SIZE.width + " and " + GUIManager.MAX_WORLD_SIZE.width, 
					AlertType.ERROR);
			return;
		}
		if((height > GUIManager.MAX_WORLD_SIZE.width) || (height < GUIManager.MIN_WORLD_SIZE.width)) {
			GUIManagerInstance.popAlertMessage("World height must be between " +
					GUIManager.MIN_WORLD_SIZE.height + " and " + GUIManager.MAX_WORLD_SIZE.height, 
					AlertType.ERROR);
			return;
		}

		GUIManager.getInstance().setWorldDimension(width, height);
	}

}
