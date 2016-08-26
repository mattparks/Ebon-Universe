package game.uis;

import flounder.devices.*;
import flounder.engine.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.resources.*;
import flounder.sounds.*;
import flounder.visual.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class MainSlider extends GuiComponent {
	public static final Colour TEXT_COLOUR = new Colour(0.85f, 0.85f, 0.85f);
	public static KeyButton BACK_KEY = new KeyButton(GLFW.GLFW_KEY_BACKSPACE);

	public static final float FONT_SIZE = 1.625f;

	public static final float BUTTONS_X_POS = 0.05f;
	public static final float BUTTONS_X_WIDTH = 1.0f - 0.25f * 2.0f;
	public static final float BUTTONS_Y_SEPARATION = 0.18f;
	public static final float BUTTONS_Y_SIZE = 0.2f;

	public static final Sound SOUND_MOUSE_HOVER = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button1.wav"), 0.8f);
	public static final Sound SOUND_MOUSE_LEFT = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button2.wav"), 0.8f);
	public static final Sound SOUND_MOUSE_RIGHT = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button3.wav"), 0.8f);

	public static final float MAIN_TITLE_FONT_SIZE = 3.25f;
	public static final float MAIN_MENU_Y_POS = 0.25f;
	public static final float MAIN_MENU_Y_SIZE = 0.6f;

	public static final Colour FADE_COLOUR_STARTUP = new Colour(0.2f, 0.2f, 0.2f);

	public static final int SLIDE_SCALAR = 2;

	private MainMenu superMenu;

	private MenuStart menuStart;
	private MenuPause menuPause;

	private GuiComponent menuActive;

	private ValueDriver mainDriver;
	private GuiComponent secondaryScreen;
	private GuiComponent newSecondaryScreen;
	private ValueDriver secondaryDriver;

	private int secondaryDepth;
	private boolean displayed;
	private boolean slidingForwards;
	private boolean closeSecondary;

	protected MainSlider(MainMenu superMenu) {
		this.superMenu = superMenu;

		this.menuStart = new MenuStart(superMenu, this);
		this.menuPause = new MenuPause(superMenu, this);

		addComponent(menuStart, 0.0f, MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);
		addComponent(menuPause, 0.0f, MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);

		this.menuActive = menuStart;
		this.menuActive.show(true);

		this.mainDriver = new ConstantDriver(SLIDE_SCALAR);
		this.secondaryDriver = new ConstantDriver(SLIDE_SCALAR);

		this.secondaryDepth = 0;
		this.displayed = false;
		this.slidingForwards = true;
		this.closeSecondary = false;
	}

	@Override
	public void show(boolean visible) {
		displayed = visible;
		mainDriver = new SlideDriver(getRelativeX(), visible ? 0.0f : SLIDE_SCALAR, MainMenu.SLIDE_TIME);
	}

	public boolean isDisplayed() {
		return displayed;
	}

	@Override
	protected void updateSelf() {
		float mainValue = mainDriver.update(FlounderEngine.getDelta());
		float value = secondaryDriver.update(FlounderEngine.getDelta());

		menuActive.setRelativeX(value);
		menuActive.show(Math.abs(value) <= SLIDE_SCALAR);

		if (newSecondaryScreen != null) {
			newSecondaryScreen.setRelativeX(value - secondaryDepth);

			if (value == secondaryDepth) {
				removeSecondaryScreen();
				secondaryScreen = newSecondaryScreen;
				newSecondaryScreen = null;
			}
		}

		if (secondaryScreen != null) {
			secondaryScreen.setRelativeX(value - secondaryDepth + (newSecondaryScreen != null ? (slidingForwards ? SLIDE_SCALAR : -SLIDE_SCALAR) : 0.0f));
		}

		super.setRelativeX(mainValue);

		if (mainValue == SLIDE_SCALAR) {
			menuActive.show(false);

			if (!displayed) {
				mainDriver = new ConstantDriver(0.0f);
				secondaryDriver = new ConstantDriver(0.0f);

				if (newSecondaryScreen != null) {
					newSecondaryScreen.show(false);
					removeComponent(newSecondaryScreen, false);
					newSecondaryScreen = null;
				}

				removeSecondaryScreen();

				secondaryDepth = 0;
				show(false);
			}
		} else {
			menuActive.show(true);
		}

		if (closeSecondary) {
			if (secondaryScreen.getRelativeX() <= -secondaryDepth) {
				removeSecondaryScreen();
				closeSecondary = false;
				secondaryDepth = 0;
			}
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}

	private void removeSecondaryScreen() {
		if (secondaryScreen != null) {
			secondaryScreen.show(false);
			removeComponent(secondaryScreen, false);
			secondaryScreen = null;
		}
	}

	public void setNewSecondaryScreen(GuiComponent secondScreen, boolean slideForwards) {
		if (newSecondaryScreen == null && secondaryDriver.update(FlounderEngine.getDelta()) == secondaryDepth) {
			secondaryDepth += slideForwards ? SLIDE_SCALAR : -SLIDE_SCALAR;
			slidingForwards = slideForwards;
			newSecondaryScreen = secondScreen;
			newSecondaryScreen.show(true);
			addComponent(secondScreen, (secondaryDepth * menuActive.getRelativeX()) - (slideForwards ? SLIDE_SCALAR : -SLIDE_SCALAR), MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);
			secondaryDriver = new SlideDriver(menuActive.getRelativeX(), secondaryDepth, MainMenu.SLIDE_TIME);
		}
	}

	public void closeSecondaryScreen() {
		if (newSecondaryScreen == null && secondaryScreen != null) {
			secondaryDriver = new SlideDriver(menuActive.getRelativeX(), 0.0f, MainMenu.SLIDE_TIME);
			closeSecondary = true;
		}
	}

	public void sliderStartMenu(boolean useStartMenu) {
		menuActive.show(false);

		if (useStartMenu) {
			menuActive = menuStart;
		} else {
			menuActive = menuPause;
		}

		menuActive.show(true);
	}

	public boolean onStartScreen() {
		return menuActive instanceof MenuStart;
	}

	public MainMenu getSuperMenu() {
		return superMenu;
	}
}