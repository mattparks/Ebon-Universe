package ebon.uis.screens.suboptions;

import ebon.uis.*;
import ebon.uis.screens.*;
import flounder.devices.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.sounds.*;

import java.util.*;

public class ScreenOptionsAudio extends GuiComponent {
	private MasterSlider masterSlider;
	private ScreenOptions screenOptions;
	private float lastSoundVolume;

	public ScreenOptionsAudio(ScreenOptions screenOptions, MasterSlider masterSlider) {
		this.masterSlider = masterSlider;
		this.screenOptions = screenOptions;

		createTitleText(GuiAlign.LEFT, "Audios");

		float currentY = -0.15f;
		createMusicOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createSoundOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createVolumeOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsAudio.super.isShown() && MasterSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				masterSlider.setNewSecondaryScreen(screenOptions, false);
			}
		});
	}

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MasterSlider.createTitleText(title, guiAlign, this);
	}

	private void createMusicOption(GuiAlign guiAlign, float yPos) {
		MusicPlayer mPlayer = FlounderSound.getMusicPlayer();
		GuiTextButton button = MasterSlider.createButton("Music: " + (!mPlayer.isPaused() ? "On" : "Off"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			if (mPlayer.isPaused()) {
				mPlayer.unpauseTrack();
			} else {
				mPlayer.pauseTrack();
			}
		});

		FlounderEvents.addEvent(new IEvent() {
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

	private void createSoundOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Sound: " + (MusicPlayer.SOUND_VOLUME == 0.0f ? "Off" : "On"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			if (MusicPlayer.SOUND_VOLUME != 0) {
				lastSoundVolume = MusicPlayer.SOUND_VOLUME;
				MusicPlayer.SOUND_VOLUME = 0.0f;
			} else {
				MusicPlayer.SOUND_VOLUME = lastSoundVolume;
				lastSoundVolume = 0.0f;
			}
		});

		FlounderEvents.addEvent(new IEvent() {
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

	private void createVolumeOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Volume: " + ((int) (MusicPlayer.SOUND_VOLUME * 100.0f)) + "%", guiAlign, yPos, this);
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

		FlounderEvents.addEvent(new IEvent() {
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

	private void createBackOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Back", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
