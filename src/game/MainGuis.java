package game;

import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import game.options.*;
import game.uis.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class in charge of the main GUIs in the test game.
 */
public class MainGuis {
	public static final Colour STARTUP_COLOUR = new Colour(1.0f, 1.0f, 1.0f);

	private static MenuGameBackground gameMenu;
	private static OverlayStatus overlayStatus;

	private static CompoundButton openKey;
	private static boolean menuOpen;

	private static boolean startingGame;
	private static boolean forceOpenGUIs;

	/**
	 * Carries out any necessary initialization of Guis.
	 */
	public MainGuis() {
		MainGuis.gameMenu = new MenuGameBackground();
		MainGuis.overlayStatus = new OverlayStatus();

		MainGuis.openKey = new CompoundButton(new KeyButton(GLFW_KEY_ESCAPE), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_TOGGLE));
		MainGuis.menuOpen = false;

		MainGuis.startingGame = true;
		MainGuis.forceOpenGUIs = false;

		FlounderEngine.getGuis().addComponent(gameMenu, 0, 0, 1, 1);
		FlounderEngine.getGuis().addComponent(overlayStatus, 0, 0, 1, 1);
		FlounderEngine.getGuis().getSelector().initJoysticks(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT, OptionsControls.JOYSTICK_GUI_RIGHT, OptionsControls.JOYSTICK_AXIS_X, OptionsControls.JOYSTICK_AXIS_Y);
		FlounderEngine.getDevices().getDisplay().setCursorHidden(true);
	}

	/**
	 * Checks inputs and updates Guis.
	 */
	public void update() {
		if (forceOpenGUIs) {
			gameMenu.display(true);
			overlayStatus.show(false);
			FlounderEngine.getCursor().show(true);
			forceOpenGUIs = false;
		}

		startingGame = gameMenu.startingGame();

		if (!startingGame && openKey.wasDown()) {
			gameMenu.display(!gameMenu.isDisplayed());
			overlayStatus.show(!startingGame && !gameMenu.isDisplayed());
			FlounderEngine.getCursor().show(true);
		}

		menuOpen = gameMenu.isDisplayed();

		if (!gameMenu.getSlideDriver().equals(FlounderEngine.getCursor().getCursorTexture().getAlphaDriver())) {
			FlounderEngine.getCursor().setAlphaDriver(gameMenu.getSlideDriver());
		}

		//	overlayStatus.show(!menuOpen && !startingGame);
	}

	public static boolean isStartingGame() {
		return startingGame;
	}

	public static void openMenu() {
		forceOpenGUIs = true;
	}

	public static float getBlurFactor() {
		return gameMenu.getBlurFactor();
	}

	public static OverlayStatus getOverlayStatus() {
		return overlayStatus;
	}

	public static boolean isMenuOpen() {
		return menuOpen;
	}
}
