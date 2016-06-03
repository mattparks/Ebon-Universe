package game.uis;

import flounder.devices.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.resources.*;
import flounder.textures.*;

import java.util.*;

public class OverlayCursor extends GuiComponent {
	private GuiTexture cursorPos;
	private Colour inactiveColour;
	private Colour clickLeftColour;
	private Colour clickRightColour;

	public OverlayCursor() {
		cursorPos = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "Cursor.png")).createInSecondThread(), false);
		cursorPos.getTexture().setNumberOfRows(1);
		cursorPos.setSelectedRow(1);
		inactiveColour = new Colour(0.0f, 0.0f, 0.0f);
		clickLeftColour = new Colour(0.0f, 0.0f, 0.8f);
		clickRightColour = new Colour(0.8f, 0.0f, 0.0f);
	}

	public int getTotalRows() {
		return cursorPos.getTexture().getNumberOfRows();
	}

	public Colour getInactiveColour() {
		return inactiveColour;
	}

	public void setInactiveColour(float r, float g, float b) {
		this.inactiveColour.set(r, g, b);
	}

	public Colour getClickLeftColour() {
		return clickLeftColour;
	}

	public void setClickLeftColour(float r, float g, float b) {
		this.clickLeftColour.set(r, g, b);
	}

	public Colour getClickRightColour() {
		return clickRightColour;
	}

	public void setClickRightColour(float r, float g, float b) {
		this.clickRightColour.set(r, g, b);
	}

	@Override
	protected void updateSelf() {
		if (super.isShown()) {
			if (FlounderDevices.getMouse().isDisplaySelected()) {
				cursorPos.setColourOffset(GuiManager.getSelector().isLeftClick() ? clickLeftColour : GuiManager.getSelector().isRightClick() ? clickRightColour : inactiveColour);
				float averageArea = (FlounderDevices.getDisplay().getWidth() + FlounderDevices.getDisplay().getHeight()) / 2.0f;
				cursorPos.setPosition(GuiManager.getSelector().getCursorX(), GuiManager.getSelector().getCursorY(), (33.75f / averageArea), (33.75f / averageArea) * FlounderDevices.getDisplay().getAspectRatio());
				cursorPos.update();
			}
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		if (super.isShown()) {
			guiTextures.add(cursorPos);
		}
	}
}
