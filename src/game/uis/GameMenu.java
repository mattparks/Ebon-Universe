package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.visual.*;

import java.util.*;

public class GameMenu extends GuiComponent {
	public static final Colour TEXT_COLOUR = new Colour(0.85f, 0.85f, 0.85f);
	private static final float TITLE_FONT_SIZE = 4.0f;
	private static final float MAIN_MENU_Y_POS = 0.25f;
	private static final float MAIN_MENU_Y_SIZE = 0.6f;
	private static final float SLIDE_TIME = 0.7f;

	private final MainMenu mainMenu;
	private final Text titleText;

	private final SinWaveDriver titleColourX;
	private final SinWaveDriver titleColourY;

	private boolean displayed = true;
	private boolean closeSecondary = false;
	private GuiComponent secondaryScreen;

	public GameMenu(final GameMenuBackground superMenu) {
		mainMenu = new MainMenu(superMenu, this);

		titleText = Text.newText("Flounder Demo").center().setFontSize(TITLE_FONT_SIZE).create();
		titleText.setColour(TEXT_COLOUR);
		titleText.setBorderColour(TEXT_COLOUR.r, TEXT_COLOUR.g, TEXT_COLOUR.b);
		titleText.setGlowing(new SinWaveDriver(0.075f, 0.150f, 2.320f));
		super.addText(titleText, 0, 0, 1);
		addComponent(mainMenu, 0, MAIN_MENU_Y_POS, 1, MAIN_MENU_Y_SIZE);

		titleColourX = new SinWaveDriver(0.0f, 1.0f, 40.0f);
		titleColourY = new SinWaveDriver(0.0f, 1.0f, 20.0f);
	}

	@Override
	public void show(final boolean visible) {
		super.show(visible);

		if (!visible) {
			removeSecondaryScreen();
		}

		this.displayed = visible;
	}

	@Override
	protected void updateSelf() {
		mainMenu.setRelativeX(0.0f);

		titleText.setColour(titleColourX.update(FlounderEngine.getDelta()), titleColourY.update(FlounderEngine.getDelta()), 0.3f);
		titleText.setBorderColour(titleText.getColour().r, titleText.getColour().g, titleText.getColour().b);

		if (secondaryScreen != null) {
			secondaryScreen.setRelativeX(0.0f);
			mainMenu.show(false);
		} else {
			mainMenu.show(true);
		}

		super.setRelativeX(0.0f);
		super.show(displayed);

		if (closeSecondary) {
			removeSecondaryScreen();
		}
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}

	private void removeSecondaryScreen() {
		if (secondaryScreen != null) {
			secondaryScreen.show(false);
			secondaryScreen = null;
			closeSecondary = false;
		}
	}

	protected boolean isDisplayed() {
		return displayed;
	}

	protected void setNewSecondaryScreen(final GuiComponent secondScreen) {
		removeSecondaryScreen();
		secondaryScreen = secondScreen;
		secondaryScreen.show(true);
		addComponent(secondScreen, mainMenu.getRelativeX() - 1.0f, MAIN_MENU_Y_POS, 1.0f, MAIN_MENU_Y_SIZE);
	}

	protected void closeSecondaryScreen() {
		closeSecondary = true;
	}
}
