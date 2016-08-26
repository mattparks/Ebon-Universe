package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.inputs.*;
import flounder.maths.*;
import game.options.*;
import game.uis.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class in charge of the main GUIs in the test game.
 */
public class MainGuis extends IManagerGUI {
	public static final Colour STARTUP_COLOUR = new Colour(1.0f, 1.0f, 1.0f);

	private MainMenu gameMenu;
	private OverlayStatus overlayStatus;

	private CompoundButton openKey;
	private boolean menuOpen;

	private boolean startingGame;
	private boolean forceOpenGUIs;

	@Override
	public void init() {
		this.gameMenu = new MainMenu();
		this.overlayStatus = new OverlayStatus();

		this.openKey = new CompoundButton(new KeyButton(GLFW_KEY_ESCAPE), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_TOGGLE));
		this.menuOpen = false;

		this.startingGame = true;
		this.forceOpenGUIs = false;

		FlounderEngine.getGuis().addComponent(gameMenu, 0, 0, 1, 1);
		FlounderEngine.getGuis().addComponent(overlayStatus, 0, 0, 1, 1);
		FlounderEngine.getGuis().getSelector().initJoysticks(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT, OptionsControls.JOYSTICK_GUI_RIGHT, OptionsControls.JOYSTICK_AXIS_X, OptionsControls.JOYSTICK_AXIS_Y);
		FlounderEngine.getDevices().getDisplay().setCursorHidden(true);
	}

	@Override
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

	@Override
	public boolean isStartingGame() {
		return startingGame;
	}

	@Override
	public void openMenu() {
		forceOpenGUIs = true;
	}

	@Override
	public float getBlurFactor() {
		return gameMenu.getBlurFactor();
	}

	@Override
	public boolean isMenuOpen() {
		return menuOpen;
	}

	public MainMenu getGameMenu() {
		return gameMenu;
	}

	public OverlayStatus getOverlayStatus() {
		return overlayStatus;
	}
}
