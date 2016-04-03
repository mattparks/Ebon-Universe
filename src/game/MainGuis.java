package game;

import flounder.guis.*;
import flounder.inputs.*;
import game.uis.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class in charge of the main GUIs in the test game.
 */
public class MainGuis {
	private static GameMenuBackground gameMenu;
	private static KeyButton openKey;
	private static boolean menuOpen;

	/**
	 * Carries out any necessary initialization of Guis.
	 */
	public static void init() {
		gameMenu = new GameMenuBackground();
		openKey = new KeyButton(GLFW_KEY_ESCAPE);
		menuOpen = false;
		GuiManager.addComponent(gameMenu, 0, 0, 1, 1);
	}

	/**
	 * Checks inputs and updates Guis.
	 */
	public static void update() {
		if (openKey.wasDown()) {
			gameMenu.display(!gameMenu.isDisplayed());
		}

		menuOpen = gameMenu.isShown();
	}

	public static float getBlurFactor() {
		return gameMenu.getBlurFactor();
	}

	public static boolean isMenuOpen() {
		return menuOpen;
	}
}
