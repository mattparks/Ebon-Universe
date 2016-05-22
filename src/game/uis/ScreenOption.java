package game.uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.sounds.*;

import java.util.*;

public class ScreenOption extends GuiComponent {
	public static final float FONT_SIZE = 2.0f;
	public static final float BUTTONS_Y_SIZE = 0.2f;
	public static final float BUTTONS_X_LEFT_POS = 0.049999997f;
	public static final float BUTTONS_X_CENTER_POS = 0.3f;
	public static final float BUTTONS_X_RIGHT_POS = 0.55f;
	public static final float BUTTONS_X_WIDTH = 0.4f;

	public static final Sound VALUE_UP_SOUND = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button1.wav"), 0.8f);
	public static final Sound VALUE_DOWN_SOUND = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button2.wav"), 0.8f);
	public static final Sound VALUE_INVALID_SOUND = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button3.wav"), 0.8f);

	private final MenuGame menuGame;
	private final ScreenOptionsDeveloper screenOptionsDeveloper;
	private final ScreenOptionsGraphics screenOptionsGraphics;
	private final ScreenOptionsInputs screenOptionsInputs;
	private final ScreenOptionsSounds screenOptionsSounds;

	protected ScreenOption(final MenuGame menuGame) {
		this.menuGame = menuGame;
		screenOptionsDeveloper = new ScreenOptionsDeveloper(this, menuGame);
		screenOptionsGraphics = new ScreenOptionsGraphics(this, menuGame);
		screenOptionsInputs = new ScreenOptionsInputs(this, menuGame);
		screenOptionsSounds = new ScreenOptionsSounds(this, menuGame);

		createGraphicsOption(BUTTONS_X_LEFT_POS, 0.0f);
		createInputsOption(BUTTONS_X_LEFT_POS, 0.2f);

		createSoundsOption(BUTTONS_X_RIGHT_POS, 0.0f);
		createDevelopersOption(BUTTONS_X_RIGHT_POS, 0.2f);

		createBackOption(BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createGraphicsOption(final float xPos, final float yPos) {
		final String graphicsText = "Graphics";
		final Text text = Text.newText(graphicsText).center().setFontSize(FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> menuGame.setNewSecondaryScreen(screenOptionsGraphics, true);

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createSoundsOption(final float xPos, final float yPos) {
		final String soundsText = "Sounds";
		final Text text = Text.newText(soundsText).center().setFontSize(FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> menuGame.setNewSecondaryScreen(screenOptionsSounds, true);

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createDevelopersOption(final float xPos, final float yPos) {
		final String soundsText = "Developer";
		final Text text = Text.newText(soundsText).center().setFontSize(FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> menuGame.setNewSecondaryScreen(screenOptionsDeveloper, true);

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createInputsOption(final float xPos, final float yPos) {
		final String inputsText = "Inputs";
		final Text text = Text.newText(inputsText).center().setFontSize(FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> menuGame.setNewSecondaryScreen(screenOptionsInputs, true);

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		Listener listener = menuGame::closeSecondaryScreen;

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
