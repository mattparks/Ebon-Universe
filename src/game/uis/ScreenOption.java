package game.uis;

import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenOption extends GuiComponent {
	private final MenuGame menuGame;
	private final ScreenOptionsDeveloper screenOptionsDeveloper;
	private final ScreenOptionsGraphics screenOptionsGraphics;
	private final ScreenOptionsInputs screenOptionsInputs;
	private final ScreenOptionsAudio screenOptionsAudio;

	protected ScreenOption(final MenuGame menuGame) {
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

		createBackOption(MenuMain.BUTTONS_X_CENTER_POS, 1.0f);

		super.addActionListener(new GuiListenerAdvanced() {
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

	private void createTitleText(final String title) {
		final Text titleText = Text.newText(title).center().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createGraphicsOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Graphics", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsGraphics, true));
	}

	private void createAudioOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Audio", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsAudio, true));
	}

	private void createDevelopersOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Developer", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsDeveloper, true));
	}

	private void createInputsOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Inputs", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsInputs, true));
	}

	private void createBackOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(menuGame::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
