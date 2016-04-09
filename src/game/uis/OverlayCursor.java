package game.uis;

import flounder.devices.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.textures.*;

import java.util.*;

public class OverlayCursor extends GuiComponent {
	private final GuiTexture cursorPos;
	private boolean displayed;
	private int inactiveRow;
	private int activeRow;

	public OverlayCursor() {
		cursorPos = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "cursor.png")).createInBackground(), false);
		cursorPos.getTexture().setNumberOfRows(2);
		cursorPos.setSelectedRow(1);
		displayed = true;
		inactiveRow = 0;
		activeRow = 2;
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

	public int getInactiveRow() {
		return inactiveRow;
	}

	public void setInactiveRow(final int inactiveRow) {
		this.inactiveRow = inactiveRow;
	}

	public int getActiveRow() {
		return activeRow;
	}

	public void setActiveRow(final int activeRow) {
		this.activeRow = activeRow;
	}

	@Override
	protected void updateSelf() {
		if (displayed) {
			cursorPos.setSelectedRow(GuiManager.getSelector().isLeftClick() || GuiManager.getSelector().isRightClick() ? activeRow : inactiveRow);
			cursorPos.setPosition(GuiManager.getSelector().getCursorX(), GuiManager.getSelector().getCursorY(), 0.0375f, 0.0375f * ManagerDevices.getDisplay().getAspectRatio());
			cursorPos.update();
		}
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
		if (displayed) {
			guiTextures.add(cursorPos);
		}
	}
}
