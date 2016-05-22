package game;

import flounder.devices.*;
import flounder.guis.*;
import flounder.inputs.*;
import game.options.*;
import game.uis.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class in charge of the main GUIs in the test game.
 */
public class MainGuis {
	private static MenuGameBackground gameMenu;
	private static OverlayCursor overlayCursor;

	private static CompoundButton openKey;
	private static boolean menuOpen;

	/**
	 * Carries out any necessary initialization of Guis.
	 */
	public static void init() {
		gameMenu = new MenuGameBackground();
		overlayCursor = new OverlayCursor();

		openKey = new CompoundButton(new KeyButton(GLFW_KEY_ESCAPE), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_TOGGLE));
		menuOpen = false;

		GuiManager.addComponent(gameMenu, 0, 0, 1, 1);
		GuiManager.addComponent(overlayCursor, 0, 0, 1, 1);
		GuiManager.getSelector().initJoysticks(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT, OptionsControls.JOYSTICK_GUI_RIGHT, OptionsControls.JOYSTICK_AXIS_X, OptionsControls.JOYSTICK_AXIS_Y);
		ManagerDevices.getDisplay().setCursorHidden(true);
	}

	/**
	 * Checks inputs and updates Guis.
	 */
	public static void update() {
		if (openKey.wasDown()) {
			gameMenu.display(!gameMenu.isDisplayed());
			overlayCursor.show(true);
		}

		menuOpen = gameMenu.isDisplayed();
	}

	public static OverlayCursor getOverlayCursor() {
		return overlayCursor;
	}

	public static float getBlurFactor() {
		return gameMenu.getBlurFactor();
	}

	public static boolean isMenuOpen() {
		return menuOpen;
	}
}
