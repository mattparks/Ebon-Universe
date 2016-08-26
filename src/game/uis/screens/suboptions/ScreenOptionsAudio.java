package game.uis.screens.suboptions;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.sounds.*;
import game.uis.*;
import game.uis.screens.*;

import java.util.*;

public class ScreenOptionsAudio extends GuiComponent {
	private MainMenuSlider mainMenuSlider;
	private ScreenOptions screenOptions;
	private float lastSoundVolume;

	public ScreenOptionsAudio(ScreenOptions screenOptions, MainMenuSlider mainMenuSlider) {
		this.mainMenuSlider = mainMenuSlider;
		this.screenOptions = screenOptions;

		createTitleText("Audios");

		float currentY = -0.15f;
		createMusicOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);
		createSoundOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);
		createVolumeOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);

		createBackOption(MainMenuContent.BUTTONS_X_POS, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsAudio.super.isShown() && MainMenuSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainMenuSlider.setNewSecondaryScreen(screenOptions, false);
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = MainMenuContent.createTitleText(title, this);
	}

	private void createMusicOption(float xPos, float yPos) {
		MusicPlayer mPlayer = FlounderEngine.getDevices().getSound().getMusicPlayer();
		GuiTextButton button = MainMenuContent.createButton("Music: " + (!mPlayer.isPaused() ? "On" : "Off"), xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> {
			if (mPlayer.isPaused()) {
				mPlayer.unpauseTrack();
			} else {
				mPlayer.pauseTrack();
			}
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean paused = mPlayer.isPaused();
			private boolean wasPaused = false;

			@Override
			public boolean eventTriggered() {
				boolean newPaused = mPlayer.isPaused();
				boolean occurred = newPaused != paused;

				float volume = MusicPlayer.SOUND_VOLUME;

				if (volume == 0.0f && !newPaused) {
					mPlayer.pauseTrack();
					occurred = true;
					newPaused = true;
					wasPaused = true;
				} else if (volume != 0.0f && newPaused && wasPaused) {
					mPlayer.unpauseTrack();
					occurred = true;
					newPaused = false;
					wasPaused = false;
				}

				paused = newPaused;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Music: " + (!paused ? "On" : "Off"));
			}
		});
	}

	private void createSoundOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Sound: " + (MusicPlayer.SOUND_VOLUME == 0.0f ? "Off" : "On"), xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> {
			if (MusicPlayer.SOUND_VOLUME != 0) {
				lastSoundVolume = MusicPlayer.SOUND_VOLUME;
				MusicPlayer.SOUND_VOLUME = 0.0f;
			} else {
				MusicPlayer.SOUND_VOLUME = lastSoundVolume;
				lastSoundVolume = 0.0f;
			}
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private float volume = MusicPlayer.SOUND_VOLUME;

			@Override
			public boolean eventTriggered() {
				float newVolume = MusicPlayer.SOUND_VOLUME;
				boolean occurred = newVolume != volume;
				volume = newVolume;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Sound: " + (MusicPlayer.SOUND_VOLUME == 0.0f ? "Off" : "On"));
			}
		});
	}

	private void createVolumeOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Volume: " + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> {
			MusicPlayer.SOUND_VOLUME += 0.05f;

			if (MusicPlayer.SOUND_VOLUME > 1.0f) {
				MusicPlayer.SOUND_VOLUME = 1.0f;
			}
		});

		button.addRightListener(() -> {
			MusicPlayer.SOUND_VOLUME -= 0.05f;

			if (MusicPlayer.SOUND_VOLUME < 0.0f) {
				MusicPlayer.SOUND_VOLUME = 0.0f;
			}
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private float volume = MusicPlayer.SOUND_VOLUME;

			@Override
			public boolean eventTriggered() {
				float newVolume = MusicPlayer.SOUND_VOLUME;
				boolean occurred = newVolume != volume;
				volume = newVolume;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Volume: " + ((int) (volume * 100.0f)) + "%");
			}
		});
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Back", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
