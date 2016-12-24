package ebon.uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.visual.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class MasterSlider extends GuiComponent {
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

	public static final int SLIDE_SCALAR = 5;

	private MasterMenu superMenu;

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

	protected MasterSlider(MasterMenu superMenu) {
		this.superMenu = superMenu;

		this.menuStart = new MenuStart(superMenu, this);
		this.menuPause = new MenuPause(superMenu, this);

		addComponent(menuStart, 0.0f, MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);
		addComponent(menuPause, 0.0f, MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);

		//	if (FlounderFramework.isRunningFromJar()) {
		this.menuActive = menuStart;
		//	} else {
		//		this.menuActive = menuPause;
		//	Ebon.instance.generateWorlds();
		//	Ebon.instance.generatePlayer();
		//	}

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
		mainDriver = new SlideDriver(getRelativeX(), visible ? 0.0f : SLIDE_SCALAR, MasterMenu.SLIDE_TIME);
	}

	public boolean isDisplayed() {
		return displayed;
	}

	@Override
	protected void updateSelf() {
		float mainValue = mainDriver.update(FlounderFramework.getDelta());
		float value = secondaryDriver.update(FlounderFramework.getDelta());

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
		if (isShown()) {

		}
	}

	private void removeSecondaryScreen() {
		if (secondaryScreen != null) {
			secondaryScreen.show(false);
			removeComponent(secondaryScreen, false);
			secondaryScreen = null;
		}
	}

	public void setNewSecondaryScreen(GuiComponent secondScreen, boolean slideForwards) {
		if (newSecondaryScreen == null && secondaryDriver.update(FlounderFramework.getDelta()) == secondaryDepth) {
			secondaryDepth += slideForwards ? SLIDE_SCALAR : -SLIDE_SCALAR;
			slidingForwards = slideForwards;
			newSecondaryScreen = secondScreen;
			newSecondaryScreen.show(true);
			addComponent(secondScreen, (secondaryDepth * menuActive.getRelativeX()) - (slideForwards ? SLIDE_SCALAR : -SLIDE_SCALAR), MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);
			secondaryDriver = new SlideDriver(menuActive.getRelativeX(), secondaryDepth, MasterMenu.SLIDE_TIME);
		}
	}

	public void closeSecondaryScreen() {
		if (newSecondaryScreen == null && secondaryScreen != null) {
			secondaryDriver = new SlideDriver(menuActive.getRelativeX(), 0.0f, MasterMenu.SLIDE_TIME);
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
		Text titleText = Text.newText(title).setFontSize(MasterSlider.MAIN_TITLE_FONT_SIZE).textAlign(guiAlign).create();
		titleText.setColour(0.15f, 0.15f, 0.15f);
		titleText.setBorderColour(MasterSlider.TEXT_COLOUR);
		titleText.setBorder(new ConstantDriver(0.04f));
		component.addText(titleText, MasterSlider.BUTTONS_X_MAGIN_LEFT, -0.30f, 1.0f);
		return titleText;
	}

	public static GuiTextButton createButton(String textString, GuiAlign guiAlign, float yPos, GuiComponent component) {
		float xPosition;
		float xMargin;

		switch (guiAlign) {
			case LEFT:
				xPosition = 0.0f;
				xMargin = MasterSlider.BUTTONS_X_MAGIN_LEFT;
				break;
			case CENTRE:
				xPosition = 0.0f;
				xMargin = MasterSlider.BUTTONS_X_MAGIN_LEFT;
				break;
			case RIGHT:
				xPosition = 0.5f;
				xMargin = MasterSlider.BUTTONS_X_MARGIN_RIGHT;
				break;
			default:
				xPosition = 0.0f;
				xMargin = 0.0f;
				break;
		}

		Text text = Text.newText(textString).setFontSize(MasterSlider.FONT_SIZE).create();
		text.setColour(MasterSlider.TEXT_COLOUR);
		text.setBorderColour(0.15f, 0.15f, 0.15f);
		text.setBorder(new ConstantDriver(0.04f));
		GuiTextButton button = new GuiTextButton(text, guiAlign, xMargin);
		component.addComponent(button, xPosition, yPos, MasterSlider.BUTTONS_X_WIDTH, MasterSlider.BUTTONS_Y_SIZE);
		return button;
	}

	public boolean onStartScreen() {
		return menuActive instanceof MenuStart;
	}

	public MasterMenu getSuperMenu() {
		return superMenu;
	}
}