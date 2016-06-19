package game;

import flounder.engine.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.maths.*;
import game.options.*;
import game.uis.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class in charge of the main GUIs in the test game.
 */
public class MainGuis {
	public static final Colour STARTUP_COLOUR = new Colour();

	private MenuGameBackground gameMenu;
	private OverlayCursor overlayCursor;

	private CompoundButton openKey;
	private boolean menuOpen;

	private static boolean startingGame;
	private static boolean forceOpenGUIs;

	/**
	 * Carries out any necessary initialization of Guis.
	 */
	public MainGuis() {
		this.gameMenu = new MenuGameBackground();
		this.overlayCursor = new OverlayCursor();

		this.openKey = new CompoundButton(new KeyButton(GLFW_KEY_ESCAPE), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_TOGGLE));
		this.menuOpen = false;

		MainGuis.startingGame = true;
		MainGuis.forceOpenGUIs = false;

		FlounderEngine.getGuis().addComponent(gameMenu, 0, 0, 1, 1);
		FlounderEngine.getGuis().addComponent(overlayCursor, 0, 0, 1, 1);
		FlounderEngine.getGuis().getSelector().initJoysticks(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT, OptionsControls.JOYSTICK_GUI_RIGHT, OptionsControls.JOYSTICK_AXIS_X, OptionsControls.JOYSTICK_AXIS_Y);
		FlounderEngine.getDevices().getDisplay().setCursorHidden(true);
	}

	/**
	 * Checks inputs and updates Guis.
	 */
	public void update() {
		if (forceOpenGUIs) {
			gameMenu.display(true);
			overlayCursor.show(true);
			forceOpenGUIs = false;
		}

		startingGame = gameMenu.startingGame();

		if (!startingGame && openKey.wasDown()) {
			gameMenu.display(!gameMenu.isDisplayed());
			overlayCursor.show(true);
		}

		if (!gameMenu.getSlideDriver().equals(overlayCursor.getCursorTexture().getAlphaDriver())) {
			overlayCursor.setAlphaDriver(gameMenu.getSlideDriver());
		}

		menuOpen = gameMenu.isDisplayed();
	}

	public static boolean isStartingGame() {
		return startingGame;
	}

	public static void openMenu() {
		forceOpenGUIs = true;
	}

	public OverlayCursor getOverlayCursor() {
		return overlayCursor;
	}

	public float getBlurFactor() {
		return gameMenu.getBlurFactor();
	}

	public boolean isMenuOpen() {
		return menuOpen;
	}
}
