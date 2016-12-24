package ebon;

import ebon.options.*;
import ebon.uis.*;
import ebon.uis.screens.*;
import flounder.devices.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.logger.*;
import flounder.profiling.*;

import static org.lwjgl.glfw.GLFW.*;

public class EbonGuis extends IExtension implements IGuiMaster {
	private MasterMenu masterMenu;
	private MasterOverlay masterOverlay;

//	private TestingGuiMain testingGuiMain;

	private CompoundButton openMenuKey;
	private boolean menuIsOpen;
	private boolean forceOpenGUIs;

	public EbonGuis() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderGuis.class);
	}

	@Override
	public void init() {
		this.masterMenu = new MasterMenu();
		this.masterOverlay = new MasterOverlay();

		this.openMenuKey = new CompoundButton(new KeyButton(GLFW_KEY_ESCAPE), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_TOGGLE));
		this.menuIsOpen = true;
		this.forceOpenGUIs = true;

		FlounderGuis.addComponent(masterMenu, 0.0f, 0.0f, 1.0f, 1.0f);
		FlounderGuis.addComponent(masterOverlay, 0.0f, 0.0f, 1.0f, 1.0f);
		FlounderGuis.getSelector().initJoysticks(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT, OptionsControls.JOYSTICK_GUI_RIGHT, OptionsControls.JOYSTICK_AXIS_X, OptionsControls.JOYSTICK_AXIS_Y);
		FlounderMouse.setCursorHidden(false);

//		testingGuiMain = new TestingGuiMain();
//		FlounderGuis.addComponent(testingGuiMain, 0.0f, 0.0f, 1.0f, 1.0f);
	}

	@Override
	public void update() {
		if (forceOpenGUIs) {
			masterMenu.display(true);
			masterOverlay.show(false);
			forceOpenGUIs = false;
		}

		menuIsOpen = masterMenu.isDisplayed();

		if (openMenuKey.wasDown() && (!menuIsOpen || !masterMenu.getMasterSlider().onStartScreen())) {
			masterMenu.display(!masterMenu.isDisplayed());
			masterOverlay.show(!masterMenu.isDisplayed());
		}
	}

	@Override
	public boolean isGamePaused() {
		return menuIsOpen;
	}

	@Override
	public void openMenu() {
		forceOpenGUIs = true;
	}

	@Override
	public float getBlurFactor() {
		return masterMenu.getBlurFactor();
	}

	public MasterOverlay getMasterOverlay() {
		return masterOverlay;
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
