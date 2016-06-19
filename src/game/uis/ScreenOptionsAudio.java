package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.sounds.*;

import java.util.*;

public class ScreenOptionsAudio extends GuiComponent {
	private MenuGame menuGame;
	private ScreenOption screenOption;
	private float lastSoundVolume;

	protected ScreenOptionsAudio(ScreenOption screenOption, MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOption = screenOption;

		createTitleText("Audio Options");

		createMusicOption(MenuMain.BUTTONS_X_LEFT_POS, 0.2f);
		createAmbientOption(MenuMain.BUTTONS_X_LEFT_POS, 0.5f);

		createSoundOption(MenuMain.BUTTONS_X_RIGHT_POS, 0.2f);
		createVolumeOption(MenuMain.BUTTONS_X_RIGHT_POS, 0.5f);

		createBackOption(MenuMain.BUTTONS_X_CENTER_POS, 1.0f);

		super.addActionListener(new GuiListenerAdvanced() {
			@Override
			public boolean hasOccurred() {
				return MenuGame.BACK_KEY.wasDown();
			}

			@Override
			public void run() {
				menuGame.setNewSecondaryScreen(screenOption, false);
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = Text.newText(title).center().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createMusicOption(float xPos, float yPos) {
		MusicPlayer mPlayer = FlounderEngine.getDevices().getSound().getMusicPlayer();
		GuiTextButton button = MenuMain.createButton("Music: " + (mPlayer.getVolume() == 1.0f ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			mPlayer.setVolume(mPlayer.getVolume() != 1.0f ? 1 : 0);
			button.getText().setText("Music: " + (mPlayer.getVolume() == 1.0f ? "On" : "Off"));
		});
	}

	private void createAmbientOption(float xPos, float yPos) {
		MusicPlayer mPlayer = FlounderEngine.getDevices().getSound().getMusicPlayer();
		GuiTextButton button = MenuMain.createButton("Ambient: " + (mPlayer.getVolume() == 1.0f ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			mPlayer.setVolume(mPlayer.getVolume() != 1.0f ? 1 : 0);
			button.getText().setText("Ambient: " + (mPlayer.getVolume() == 1.0f ? "On" : "Off"));
		});
	}

	private void createSoundOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Sound: " + (MusicPlayer.SOUND_VOLUME == 0.0f ? "Off" : "On"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			if (MusicPlayer.SOUND_VOLUME != 0) {
				lastSoundVolume = MusicPlayer.SOUND_VOLUME;
				MusicPlayer.SOUND_VOLUME = 0.0f;
			} else {
				MusicPlayer.SOUND_VOLUME = lastSoundVolume;
				lastSoundVolume = 0.0f;
			}

			button.getText().setText("Sound: " + (MusicPlayer.SOUND_VOLUME == 0.0f ? "Off" : "On"));
		});
	}

	private void createVolumeOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Volume: " + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			MusicPlayer.SOUND_VOLUME += 0.05f;

			if (MusicPlayer.SOUND_VOLUME > 1.0f) {
				MusicPlayer.SOUND_VOLUME = 1.0f;
			}

			button.getText().setText("Volume: " + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%");
		});

		button.addRightListener(() -> {
			MusicPlayer.SOUND_VOLUME -= 0.05f;

			if (MusicPlayer.SOUND_VOLUME < 0.0f) {
				MusicPlayer.SOUND_VOLUME = 0.0f;
			}

			button.getText().setText("Volume: " + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%");
		});
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOption, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
