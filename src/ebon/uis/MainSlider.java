package ebon.uis;

import ebon.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.visual.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class MainSlider extends GuiComponent {
	public static final Colour TEXT_COLOUR = new Colour(0.85f, 0.85f, 0.85f);
	public static KeyButton BACK_KEY = new KeyButton(GLFW.GLFW_KEY_BACKSPACE);

	public static final float FONT_SIZE = 1.475f;

	public static final float BUTTONS_X_MAGIN_LEFT = 0.05f;
	public static final float BUTTONS_X_MARGIN_RIGHT = 0.05f;
	public static final float BUTTONS_X_WIDTH = 1.0f - 0.25f * 2.0f;
	public static final float BUTTONS_Y_SEPARATION = 0.19f;
	public static final float BUTTONS_Y_SIZE = 0.2f;

	public static final float MAIN_TITLE_FONT_SIZE = 3.25f;
	public static final float MAIN_MENU_Y_POS = 0.25f;
	public static final float MAIN_MENU_Y_SIZE = 0.575f;

	public static final Colour FADE_COLOUR_STARTUP = new Colour(0.2f, 0.2f, 0.2f);

	public static final int SLIDE_SCALAR = 5;

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

		if (FlounderEngine.isRunningFromJar()) {
			this.menuActive = menuStart;
		} else {
			this.menuActive = menuPause;
			Ebon.instance.generateWorlds();
			Ebon.instance.generatePlayer();
		}

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
		FlounderMouse.setCursorHidden(!visible);

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

	public static Text createTitleText(String title, GuiAlign guiAlign, GuiComponent component) {
		Text titleText = Text.newText(title).setFontSize(MainSlider.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(0.15f, 0.15f, 0.15f);
		titleText.setBorderColour(MainSlider.TEXT_COLOUR);
		titleText.setBorder(new ConstantDriver(0.04f));
		component.addText(titleText, MainSlider.BUTTONS_X_MAGIN_LEFT, -0.30f, 1.0f);
		return titleText;
	}

	public static GuiTextButton createButton(String textString, GuiAlign guiAlign, float yPos, GuiComponent component) {
		float xPosition;
		float xMargin;

		switch (guiAlign) {
			case LEFT:
				xPosition = 0.0f;
				xMargin = MainSlider.BUTTONS_X_MAGIN_LEFT;
				break;
			case CENTRE:
				xPosition = 0.0f;
				xMargin = MainSlider.BUTTONS_X_MAGIN_LEFT;
				break;
			case RIGHT:
				xPosition = 0.5f;
				xMargin = MainSlider.BUTTONS_X_MARGIN_RIGHT;
				break;
			default:
				xPosition = 0.0f;
				xMargin = 0.0f;
				break;
		}

		Text text = Text.newText(textString).setFontSize(MainSlider.FONT_SIZE).create();
		text.setColour(MainSlider.TEXT_COLOUR);
		text.setBorderColour(0.15f, 0.15f, 0.15f);
		text.setBorder(new ConstantDriver(0.04f));
		GuiTextButton button = new GuiTextButton(text, guiAlign, xMargin);
		component.addComponent(button, xPosition, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE);
		return button;
	}

	public boolean onStartScreen() {
		return menuActive instanceof MenuStart;
	}

	public MainMenu getSuperMenu() {
		return superMenu;
	}
}