package game.uis;

import flounder.engine.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.visual.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class MenuGame extends GuiComponent {
	public static final Colour TEXT_COLOUR = new Colour(0.85f, 0.85f, 0.85f);

	public static final float MAIN_TITLE_FONT_SIZE = 2.75f;
	public static final float MAIN_MENU_Y_POS = 0.25f;
	public static final float MAIN_MENU_Y_SIZE = 0.6f;

	public static final int SLIDE_SCALAR = 2;

	public static KeyButton BACK_KEY = new KeyButton(GLFW.GLFW_KEY_BACKSPACE);

	private MenuMain menuMain;

	private ValueDriver mainDriver;
	private GuiComponent secondaryScreen;
	private GuiComponent newSecondaryScreen;
	private ValueDriver secondaryDriver;

	private int secondaryDepth;
	private boolean displayed;
	private boolean slidingForwards;
	private boolean closeSecondary;

	public MenuGame(MenuGameBackground superMenu) {
		menuMain = new MenuMain(superMenu, this);

		mainDriver = new ConstantDriver(SLIDE_SCALAR);
		secondaryDriver = new ConstantDriver(SLIDE_SCALAR);

		addComponent(menuMain, 0.0f, MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);

		secondaryDepth = 0;
		displayed = false;
		slidingForwards = true;
		closeSecondary = false;
	}

	@Override
	public void show(boolean visible) {
		displayed = visible;
		mainDriver = new SlideDriver(getRelativeX(), visible ? 0.0f : SLIDE_SCALAR, MenuGameBackground.SLIDE_TIME);
	}

	public boolean isDisplayed() {
		return displayed;
	}

	@Override
	protected void updateSelf() {
		float mainValue = mainDriver.update(FlounderEngine.getDelta());
		float value = secondaryDriver.update(FlounderEngine.getDelta());

		menuMain.setRelativeX(value);
		menuMain.show(Math.abs(value) <= SLIDE_SCALAR);

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
			menuMain.show(false);

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
			menuMain.show(true);
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

	protected void setNewSecondaryScreen(GuiComponent secondScreen, boolean slideForwards) {
		if (newSecondaryScreen == null && secondaryDriver.update(FlounderEngine.getDelta()) == secondaryDepth) {
			secondaryDepth += slideForwards ? SLIDE_SCALAR : -SLIDE_SCALAR;
			slidingForwards = slideForwards;
			newSecondaryScreen = secondScreen;
			newSecondaryScreen.show(true);
			addComponent(secondScreen, (secondaryDepth * menuMain.getRelativeX()) - (slideForwards ? SLIDE_SCALAR : -SLIDE_SCALAR), MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);
			secondaryDriver = new SlideDriver(menuMain.getRelativeX(), secondaryDepth, MenuGameBackground.SLIDE_TIME);
		}
	}

	protected void closeSecondaryScreen() {
		if (newSecondaryScreen == null && secondaryScreen != null) {
			secondaryDriver = new SlideDriver(menuMain.getRelativeX(), 0.0f, MenuGameBackground.SLIDE_TIME);
			closeSecondary = true;
		}
	}
}