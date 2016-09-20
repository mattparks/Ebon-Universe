package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.inputs.*;
import game.options.*;
import game.uis.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class in charge of the main GUIs in the test game.
 */
public class MainGuis extends IManagerGUI {
	private MainMenu mainMenu;
	private OverlayStatus overlayStatus;

	private CompoundButton openMenuKey;
	private boolean menuIsOpen;
	private boolean forceOpenGUIs;

	@Override
	public void init() {
		this.mainMenu = new MainMenu();
		this.overlayStatus = new OverlayStatus();

		this.openMenuKey = new CompoundButton(new KeyButton(GLFW_KEY_ESCAPE), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_TOGGLE));
		this.menuIsOpen = true;
		this.forceOpenGUIs = true;

		FlounderEngine.getGuis().addComponent(mainMenu, 0, 0, 1, 1);
		FlounderEngine.getGuis().addComponent(overlayStatus, 0, 0, 1, 1);
		FlounderEngine.getGuis().getSelector().initJoysticks(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT, OptionsControls.JOYSTICK_GUI_RIGHT, OptionsControls.JOYSTICK_AXIS_X, OptionsControls.JOYSTICK_AXIS_Y);
		FlounderEngine.getDevices().getMouse().setCursorHidden(false);
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
	public void openMenu() {
		forceOpenGUIs = true;
	}

	@Override
	public float getBlurFactor() {
		return mainMenu.getBlurFactor();
	}

	public boolean isMenuIsOpen() {
		return menuIsOpen;
	}

	public MainMenu getMainMenu() {
		return mainMenu;
	}

	public OverlayStatus getOverlayStatus() {
		return overlayStatus;
	}
}
