package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.visual.*;
import game.*;
import game.uis.screens.*;

import java.util.*;

public class MenuPause extends GuiComponent {
	private MainMenu superMenu;
	private MainSlider mainSlider;

	private ScreenOptions screenOptions;
	private ScreenControls screenControls;

	protected MenuPause(MainMenu superMenu, MainSlider mainSlider) {
		this.superMenu = superMenu;
		this.mainSlider = mainSlider;

		this.screenOptions = new ScreenOptions(mainSlider);
		this.screenControls = new ScreenControls(mainSlider);

		float currentY = -MainSlider.BUTTONS_Y_SEPARATION * 2.0f;
		createResumeButton(currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createOptionsButton(currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createControlsButton(currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createQuitButton(currentY += MainSlider.BUTTONS_Y_SEPARATION);

		super.show(false);
	}

	private void createResumeButton(float yPos) {
		GuiTextButton button = createButton("Resume Game", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> superMenu.display(false));
		button.addRightListener(null);
	}

	private void createOptionsButton(float yPos) {
		GuiTextButton button = createButton("Options", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createControlsButton(float yPos) {
		GuiTextButton button = createButton("Controls", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenControls, true));
		button.addRightListener(null);
	}

	private void createQuitButton(float yPos) {
		GuiTextButton button = createButton("Quit", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> {
			mainSlider.closeSecondaryScreen();
			mainSlider.getSuperMenu().display(true);
			mainSlider.sliderStartMenu(true);
			((MainGame) FlounderEngine.getGame()).destroyWorld();
		});
		button.addRightListener(null);
	}

	public static GuiTextButton createButton(String textString, float xPos, float yPos, float xBut, float yBut, float fontSize, GuiComponent component) {
		Text text = Text.newText(textString).setFontSize(fontSize).create();
		text.setColour(MainSlider.TEXT_COLOUR);
		text.setBorderColour(0.15f, 0.15f, 0.15f);
		text.setBorder(new ConstantDriver(0.04f));
		GuiTextButton button = new GuiTextButton(text);
		button.setSounds(MainSlider.SOUND_MOUSE_HOVER, MainSlider.SOUND_MOUSE_LEFT, MainSlider.SOUND_MOUSE_RIGHT);
		component.addComponent(button, xPos, yPos, xBut, yBut);
		return button;
	}

	public static Text createTitleText(String title, GuiComponent component) {
		Text titleText = Text.newText(title).setFontSize(MainSlider.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(0.15f, 0.15f, 0.15f);
		titleText.setBorderColour(MainSlider.TEXT_COLOUR);
		titleText.setBorder(new ConstantDriver(0.04f));
		component.addText(titleText, MainSlider.BUTTONS_X_POS, -0.30f, 1.0f);
		return titleText;
	}

	public MainMenu getSuperMenu() {
		return superMenu;
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
