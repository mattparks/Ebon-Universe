package uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.sounds.*;

import java.util.*;

public class OptionScreen extends GuiComponent {
	public static final float FONT_SIZE = 2;
	public static final float BUTTONS_Y_SIZE = 0.2f;
	public static final float BUTTONS_X_LEFT_POS = 0.049999997f;
	public static final float BUTTONS_X_CENTER_POS = 0.3f;
	public static final float BUTTONS_X_RIGHT_POS = 0.55f;
	public static final float BUTTONS_X_WIDTH = 0.4f;

	public static final Sound VALUE_UP_SOUND = Sound.loadSoundNow(new MyFile(DeviceSound.SOUND_FOLDER, "button3.wav"), 0.8f);
	public static final Sound VALUE_DOWN_SOUND = Sound.loadSoundNow(new MyFile(DeviceSound.SOUND_FOLDER, "button3.wav"), 0.8f);
	public static final Sound VALUE_INVALID_SOUND = Sound.loadSoundNow(new MyFile(DeviceSound.SOUND_FOLDER, "button3.wav"), 0.8f);

	private final GameMenu gameMenu;

	protected OptionScreen(final GameMenu menu) {
		gameMenu = menu;

		createGraphicsOption(BUTTONS_X_LEFT_POS, 0.0f);
		createInputsOption(BUTTONS_X_LEFT_POS, 0.2f);

		createSoundsOption(BUTTONS_X_RIGHT_POS, 0.0f);
		createDevelopersOption(BUTTONS_X_RIGHT_POS, 0.2f);

		createBackOption(BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createGraphicsOption(final float xPos, final float yPos) {
		final String graphicsText = "Graphics Options";
		final Text text = Text.newText(graphicsText).center().setFontSize(FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> gameMenu.setNewSecondaryScreen(new OptionScreenGraphics(gameMenu));

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createSoundsOption(final float xPos, final float yPos) {
		final String soundsText = "Sounds Options";
		final Text text = Text.newText(soundsText).center().setFontSize(FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> gameMenu.setNewSecondaryScreen(new OptionScreenSounds(gameMenu));

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createDevelopersOption(final float xPos, final float yPos) {
		final String soundsText = "Developer Options";
		final Text text = Text.newText(soundsText).center().setFontSize(FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> gameMenu.setNewSecondaryScreen(new OptionScreenDeveloper(gameMenu));

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createInputsOption(final float xPos, final float yPos) {
		final String inputsText = "Inputs Options";
		final Text text = Text.newText(inputsText).center().setFontSize(FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> gameMenu.setNewSecondaryScreen(new OptionScreenInputs(gameMenu));

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> gameMenu.closeSecondaryScreen();

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
