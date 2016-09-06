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
	private MainSlider mainSlider;
	private ScreenOptions screenOptions;
	private float lastSoundVolume;

	public ScreenOptionsAudio(ScreenOptions screenOptions, MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		this.screenOptions = screenOptions;

		createTitleText(TextAlign.LEFT, "Audios");

		float currentY = -0.15f;
		createMusicOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createSoundOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createVolumeOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(TextAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsAudio.super.isShown() && MainSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainSlider.setNewSecondaryScreen(screenOptions, false);
			}
		});
	}

	private void createTitleText(TextAlign textAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, textAlign, this);
	}

	private void createMusicOption(TextAlign textAlign, float yPos) {
		MusicPlayer mPlayer = FlounderEngine.getDevices().getSound().getMusicPlayer();
		GuiTextButton button = MainSlider.createButton("Music: " + (!mPlayer.isPaused() ? "On" : "Off"), textAlign, yPos, this);
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

	private void createSoundOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Sound: " + (MusicPlayer.SOUND_VOLUME == 0.0f ? "Off" : "On"), textAlign, yPos, this);
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

	private void createVolumeOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Volume: " + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%", textAlign, yPos, this);
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

	private void createBackOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
