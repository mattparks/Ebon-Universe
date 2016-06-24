package game.uis;

import flounder.fonts.*;
import flounder.guis.*;
import flounder.visual.*;

import java.util.*;

public class ScreenOption extends GuiComponent {
	private MenuGame menuGame;
	private ScreenOptionsDeveloper screenOptionsDeveloper;
	private ScreenOptionsGraphics screenOptionsGraphics;
	private ScreenOptionsInputs screenOptionsInputs;
	private ScreenOptionsAudio screenOptionsAudio;

	protected ScreenOption(MenuGame menuGame) {
		this.menuGame = menuGame;
		screenOptionsDeveloper = new ScreenOptionsDeveloper(this, menuGame);
		screenOptionsGraphics = new ScreenOptionsGraphics(this, menuGame);
		screenOptionsInputs = new ScreenOptionsInputs(this, menuGame);
		screenOptionsAudio = new ScreenOptionsAudio(this, menuGame);

		createTitleText("Options");

		createGraphicsOption(MenuMain.BUTTONS_X_LEFT_POS, 0.2f);
		createInputsOption(MenuMain.BUTTONS_X_LEFT_POS, 0.5f);

		createAudioOption(MenuMain.BUTTONS_X_RIGHT_POS, 0.2f);
		createDevelopersOption(MenuMain.BUTTONS_X_RIGHT_POS, 0.5f);

		createBackOption(MenuMain.BUTTONS_X_CENTRE_POS, 1.0f);

		super.addActionListener(new ListenerAdvanced() {
			@Override
			public boolean hasOccurred() {
				return MenuGame.BACK_KEY.wasDown();
			}

			@Override
			public void run() {
				menuGame.closeSecondaryScreen();
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = Text.newText(title).centre().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		titleText.setBorderColour(0.15f, 0.15f, 0.15f);
		titleText.setBorder(new ConstantDriver(0.04f));
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createGraphicsOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Graphics", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsGraphics, true));
	}

	private void createAudioOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Audio", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsAudio, true));
	}

	private void createDevelopersOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Developer", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsDeveloper, true));
	}

	private void createInputsOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Inputs", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsInputs, true));
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(menuGame::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
