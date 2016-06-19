package game.uis;

import flounder.engine.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.resources.*;
import flounder.textures.*;
import flounder.visual.*;

import java.util.*;

public class OverlayCursor extends GuiComponent {
	private GuiTexture cursorTexture;
	private Colour inactiveColour;
	private Colour clickLeftColour;
	private Colour clickRightColour;

	public OverlayCursor() {
		cursorTexture = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "Cursor.png")).createInSecondThread(), false);
		cursorTexture.getTexture().setNumberOfRows(1);
		cursorTexture.setSelectedRow(1);
		inactiveColour = new Colour(0.0f, 0.0f, 0.0f);
		clickLeftColour = new Colour(0.0f, 0.0f, 0.8f);
		clickRightColour = new Colour(0.8f, 0.0f, 0.0f);
	}

	public GuiTexture getCursorTexture() {
		return cursorTexture;
	}

	public int getTotalRows() {
		return cursorTexture.getTexture().getNumberOfRows();
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

	public void setAlphaDriver(ValueDriver alphaDriver) {
		cursorTexture.setAlphaDriver(alphaDriver);
	}

	@Override
	protected void updateSelf() {
		if (super.isShown()) {
			if (FlounderEngine.getDevices().getMouse().isDisplaySelected()) {
				cursorTexture.setColourOffset(FlounderEngine.getGuis().getSelector().isLeftClick() ? clickLeftColour : FlounderEngine.getGuis().getSelector().isRightClick() ? clickRightColour : inactiveColour);
				float averageArea = (FlounderEngine.getDevices().getDisplay().getWidth() + FlounderEngine.getDevices().getDisplay().getHeight()) / 2.0f;
				cursorTexture.setPosition(FlounderEngine.getGuis().getSelector().getCursorX(), FlounderEngine.getGuis().getSelector().getCursorY(), (33.75f / averageArea), (33.75f / averageArea) * FlounderEngine.getDevices().getDisplay().getAspectRatio());
				cursorTexture.update();
			}
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		if (super.isShown()) {
			guiTextures.add(cursorTexture);
		}
	}
}
