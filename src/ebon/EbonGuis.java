package ebon;

import ebon.options.*;
import ebon.uis.*;
import flounder.devices.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.logger.*;
import flounder.profiling.*;

import static org.lwjgl.glfw.GLFW.*;

public class EbonGuis extends IExtension implements IGuiMaster {
	private MainMenu mainMenu;
	private OverlayStatus overlayStatus;

	private CompoundButton openMenuKey;
	private boolean menuIsOpen;
	private boolean forceOpenGUIs;

	public EbonGuis() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderGuis.class);
	}

	@Override
	public void init() {
		this.mainMenu = new MainMenu();
		this.overlayStatus = new OverlayStatus();

		this.openMenuKey = new CompoundButton(new KeyButton(GLFW_KEY_ESCAPE), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_TOGGLE));
		this.menuIsOpen = true;
		this.forceOpenGUIs = true;

		FlounderGuis.addComponent(mainMenu, 0, 0, 1, 1);
		FlounderGuis.addComponent(overlayStatus, 0, 0, 1, 1);
		FlounderGuis.getSelector().initJoysticks(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT, OptionsControls.JOYSTICK_GUI_RIGHT, OptionsControls.JOYSTICK_AXIS_X, OptionsControls.JOYSTICK_AXIS_Y);
		FlounderMouse.setCursorHidden(false);
	}

	@Override
	public void update() {
		if (forceOpenGUIs) {
			mainMenu.display(true);
			overlayStatus.show(false);
			forceOpenGUIs = false;
		}

		menuIsOpen = mainMenu.isDisplayed();

		if (openMenuKey.wasDown() && (!menuIsOpen || !mainMenu.getMainSlider().onStartScreen())) {
			mainMenu.display(!mainMenu.isDisplayed());
			overlayStatus.show(!mainMenu.isDisplayed());
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
		return mainMenu.getBlurFactor();
	}

	@Override
	public boolean isActive() {
		return true;
	}

	public MainMenu getMainMenu() {
		return mainMenu;
	}

	public OverlayStatus getOverlayStatus() {
		return overlayStatus;
	}

	@Override
	public void dispose() {

	}
}
