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

import main.SimulationParameters;
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
		super(labelText, SimulationParameters.xBoundry + "x" + SimulationParameters.yBoundry);
	}

	@Override
	public void updateParamValue() throws InputException {
		String[] worldAttributes = this.getInputValue().split("x");
		if((worldAttributes.length != 2)) {
			throw new InputException("World size input should be in the form of <width>x<height>");
		}
		int width = 0, height = 0;
		try {
			width = Integer.valueOf(worldAttributes[0]);
			height = Integer.valueOf(worldAttributes[1]);
		}
		catch (NumberFormatException e) {
			throw new InputException("World size width or height are not numeric");
		}
		if((width > GUIManager.MAX_WORLD_SIZE.width) || (width < GUIManager.MIN_WORLD_SIZE.width)) {
			throw new InputException("World width must be between " +
					GUIManager.MIN_WORLD_SIZE.width + " and " + 
					GUIManager.MAX_WORLD_SIZE.width);
		}
		if((height > GUIManager.MAX_WORLD_SIZE.height) || (height < GUIManager.MIN_WORLD_SIZE.height)) {
			throw new InputException("World height must be between " +
					GUIManager.MIN_WORLD_SIZE.height + " and " + 
					GUIManager.MAX_WORLD_SIZE.height);
		}

		GUIManager.getInstance().setWorldDimension(width, height);
		SimulationParameters.xBoundry = width;
		SimulationParameters.yBoundry = height;
	}

}
