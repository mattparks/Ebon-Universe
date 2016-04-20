package game.uis;

import flounder.devices.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.resources.*;
import flounder.textures.*;

import java.util.*;

public class OverlayCursor extends GuiComponent {
	private final GuiTexture cursorPos;
	private final Colour inactiveColour;
	private final Colour activeColour;
	private boolean displayed;

	public OverlayCursor() {
		cursorPos = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "cursor.png")).createInBackground(), false);
		cursorPos.getTexture().setNumberOfRows(1);
		cursorPos.setSelectedRow(1);
		displayed = true;
		inactiveColour = new Colour(1, 0, 0);
		activeColour = new Colour(0, 0, 1);
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void display(final boolean displayed) {
		this.displayed = displayed;
	}

	public int getTotalRows() {
		return cursorPos.getTexture().getNumberOfRows();
	}

	public Colour getInactiveColour() {
		return inactiveColour;
	}

	public void setInactiveColour(final float r, final float g, final float b) {
		this.inactiveColour.set(r, g, b);
	}

	public Colour getActiveColour() {
		return activeColour;
	}

	public void setActiveColour(final float r, final float g, final float b) {
		this.activeColour.set(r, g, b);
	}

	@Override
	protected void updateSelf() {
	//	if (displayed) {
			cursorPos.setColourOffset(GuiManager.getSelector().isLeftClick() || GuiManager.getSelector().isRightClick() ? activeColour : inactiveColour);
			cursorPos.setPosition(GuiManager.getSelector().getCursorX(), GuiManager.getSelector().getCursorY(), 0.0375f, 0.0375f * ManagerDevices.getDisplay().getAspectRatio());
			cursorPos.update();
	//	}
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	//	if (displayed) {
			guiTextures.add(cursorPos);
	//	}
	}
}
