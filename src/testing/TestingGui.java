package testing;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.logger.*;

public class TestingGui extends IExtension implements IGuiMaster {
	private TestingGuiMain guiMain;

	public TestingGui() {
		super(FlounderLogger.class, FlounderMouse.class, FlounderGuis.class, FlounderFonts.class);
	}

	@Override
	public void init() {
		guiMain = new TestingGuiMain();
		FlounderGuis.addComponent(guiMain, 0.0f, 0.0f, 1.0f, 1.0f);
		FlounderMouse.setCursorHidden(false);
		guiMain.show(true);
	}

	@Override
	public void update() {

	}

	@Override
	public boolean isGamePaused() {
		return true;
	}

	@Override
	public void openMenu() {

	}

	@Override
	public float getBlurFactor() {
		return 0;
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isActive() {
		return true;
	}
}
