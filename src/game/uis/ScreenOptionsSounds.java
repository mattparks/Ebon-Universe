package game.uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.sounds.*;

import java.util.*;

public class ScreenOptionsSounds extends GuiComponent {
	private final MenuGame menuGame;
	private final ScreenOption screenOption;
	private float lastSoundVolume;

	protected ScreenOptionsSounds(final ScreenOption screenOption, final MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOption = screenOption;

		createSoundOption(ScreenOption.BUTTONS_X_LEFT_POS, 0.0f);
		createMusicOption(ScreenOption.BUTTONS_X_LEFT_POS, 0.2f);
		createAmbientOption(ScreenOption.BUTTONS_X_LEFT_POS, 0.4f);

		createVolumeOption(ScreenOption.BUTTONS_X_RIGHT_POS, 0.0f);

		createBackOption(ScreenOption.BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createSoundOption(final float xPos, final float yPos) {
		final String soundText = "Sound: ";
		final Text text = Text.newText(soundText + (MusicPlayer.SOUND_VOLUME == 0.0f ? "Off" : "On")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener leftListener = () -> {
			if (MusicPlayer.SOUND_VOLUME != 0) {
				lastSoundVolume = MusicPlayer.SOUND_VOLUME;
				MusicPlayer.SOUND_VOLUME = 0.0f;
			} else {
				MusicPlayer.SOUND_VOLUME = lastSoundVolume;
				lastSoundVolume = 0.0f;
			}

			text.setText(soundText + (MusicPlayer.SOUND_VOLUME == 0.0f ? "Off" : "On"));
		};

		button.addLeftListener(leftListener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createMusicOption(final float xPos, final float yPos) {
		final MusicPlayer mPlayer = ManagerDevices.getSound().getMusicPlayer();
		final String musicText = "Music: ";
		final Text text = Text.newText(musicText + (mPlayer.getVolume() == 1.0f ? "On" : "Off")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener leftListener = () -> {
			mPlayer.setVolume(mPlayer.getVolume() != 1.0f ? 1 : 0);
			text.setText(musicText + (mPlayer.getVolume() == 1.0f ? "On" : "Off"));
		};

		button.addLeftListener(leftListener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createAmbientOption(final float xPos, final float yPos) {
		final MusicPlayer mPlayer = ManagerDevices.getSound().getMusicPlayer();
		final String ambientText = "Ambient: ";
		final Text text = Text.newText(ambientText + (mPlayer.getVolume() == 1.0f ? "On" : "Off")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener leftListener = () -> {
			mPlayer.setVolume(mPlayer.getVolume() != 1.0f ? 1 : 0);
			text.setText(ambientText + (mPlayer.getVolume() == 1.0f ? "On" : "Off"));
		};

		button.addLeftListener(leftListener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createVolumeOption(final float xPos, final float yPos) {
		final String soundText = "Volume: ";
		final Text text = Text.newText(soundText + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%").center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener leftListener = () -> {
			MusicPlayer.SOUND_VOLUME += 0.05f;

			if (MusicPlayer.SOUND_VOLUME > 1.0f) {
				MusicPlayer.SOUND_VOLUME = 1.0f;
			}

			text.setText(soundText + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%");
		};

		final Listener rightListener = () -> {
			MusicPlayer.SOUND_VOLUME -= 0.05f;

			if (MusicPlayer.SOUND_VOLUME < 0.0f) {
				MusicPlayer.SOUND_VOLUME = 0.0f;
			}

			text.setText(soundText + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%");
		};

		button.addLeftListener(leftListener);
		button.setMouseLeftClickSound(ScreenOption.VALUE_UP_SOUND);
		button.addRightListener(rightListener);
		button.setMouseRightClickSound(ScreenOption.VALUE_DOWN_SOUND);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener leftListener = () -> menuGame.setNewSecondaryScreen(screenOption, false);

		button.addLeftListener(leftListener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
