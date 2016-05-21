package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.visual.*;

import java.util.*;

public class MenuGame extends GuiComponent {
	public static final Colour TEXT_COLOUR = new Colour(0.85f, 0.85f, 0.85f);
	private static final float TITLE_FONT_SIZE = 4.0f;
	private static final float MAIN_MENU_Y_POS = 0.25f;
	private static final float MAIN_MENU_Y_SIZE = 0.6f;
	private static final float SLIDE_TIME = 0.7f;

	private final MenuMain menuMain;

	private final Text titleText;
	private final SinWaveDriver titleColourX;
	private final SinWaveDriver titleColourY;

	private ValueDriver mainDriver;
	private GuiComponent secondaryScreen;
	private GuiComponent newSecondaryScreen;
	private ValueDriver secondaryDriver;

	private int secondaryDepth;
	private boolean displayed;
	private boolean slidingForwards;
	private boolean closeSecondary;

	public MenuGame(final MenuGameBackground superMenu) {
		menuMain = new MenuMain(superMenu, this);

		titleText = Text.newText("Flounder Demo").center().setFontSize(TITLE_FONT_SIZE).create();
		titleText.setColour(TEXT_COLOUR);
		titleText.setBorderColour(TEXT_COLOUR.r, TEXT_COLOUR.g, TEXT_COLOUR.b);
		titleText.setGlowing(new SinWaveDriver(0.075f, 0.150f, 2.320f));
		addText(titleText, 0.0f, 0.0f, 1.0f);
		titleColourX = new SinWaveDriver(0.0f, 1.0f, 40.0f);
		titleColourY = new SinWaveDriver(0.0f, 1.0f, 20.0f);

		mainDriver = new ConstantDriver(0.0f);
		secondaryDriver = new ConstantDriver(0.0f);

		addComponent(menuMain, 0.0f, MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);

		secondaryDepth = 0;
		displayed = true;
		slidingForwards = true;
		closeSecondary = false;
	}

	@Override
	public void show(final boolean visible) {
		displayed = visible;
		mainDriver = new SlideDriver(getRelativeX(), visible ? 0.0f : 1.0f, SLIDE_TIME);
	}

	@Override
	protected void updateSelf() {
		final float mainValue = mainDriver.update(FlounderEngine.getDelta());
		final float value = secondaryDriver.update(FlounderEngine.getDelta());

		menuMain.setRelativeX(value);

		if (newSecondaryScreen != null) {
			newSecondaryScreen.setRelativeX(value - secondaryDepth);

			if (value == secondaryDepth) {
				removeSecondaryScreen();
				secondaryScreen = newSecondaryScreen;
				newSecondaryScreen = null;
			}
		}

		if (secondaryScreen != null) {
			secondaryScreen.setRelativeX(value - secondaryDepth + (newSecondaryScreen != null ? (slidingForwards ? 1.0f : -1.0f) : 0.0f));
		}

		super.setRelativeX(mainValue);

		if (mainValue >= 1.0f) {
			menuMain.show(false);

			if (!displayed) {
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

		titleText.setColour(titleColourX.update(FlounderEngine.getDelta()), titleColourY.update(FlounderEngine.getDelta()), 0.3f);
		titleText.setBorderColour(titleText.getColour().r, titleText.getColour().g, titleText.getColour().b);
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}

	private void removeSecondaryScreen() {
		if (secondaryScreen != null) {
			secondaryScreen.show(false);
			removeComponent(secondaryScreen, false);
			secondaryScreen = null;
		}
	}

	protected void setNewSecondaryScreen(final GuiComponent secondScreen, final boolean slideForwards) {
		secondaryDepth += slideForwards ? 1 : -1;
		slidingForwards = slideForwards;
		newSecondaryScreen = secondScreen;
		newSecondaryScreen.show(true);
		addComponent(secondScreen, (secondaryDepth * menuMain.getRelativeX()) - (slideForwards ? 1.0f : -1.0f), MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);
		secondaryDriver = new SlideDriver(menuMain.getRelativeX(), secondaryDepth, SLIDE_TIME);
	}

	protected void closeSecondaryScreen() {
		secondaryDriver = new SlideDriver(menuMain.getRelativeX(), 0.0f, SLIDE_TIME);
		closeSecondary = true;
	}
}