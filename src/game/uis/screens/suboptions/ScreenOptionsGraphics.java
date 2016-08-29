package game.uis.screens.suboptions;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.uis.*;
import game.uis.screens.*;

import java.util.*;

public class ScreenOptionsGraphics extends GuiComponent {
	private MainSlider mainSlider;
	private ScreenOptions screenOptions;

	public ScreenOptionsGraphics(ScreenOptions screenOptions, MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		this.screenOptions = screenOptions;

		createTitleText("Graphics");

		float currentY = -0.15f;
		createFullscreenOption(MainSlider.BUTTONS_X_POS_LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createAntialiasOption(MainSlider.BUTTONS_X_POS_LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createVSyncOption(MainSlider.BUTTONS_X_POS_LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		currentY = -0.15f;
		createSamplesOption(MainSlider.BUTTONS_X_POS_LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(MainSlider.BUTTONS_X_POS_LEFT, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsGraphics.super.isShown() && MainSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainSlider.setNewSecondaryScreen(screenOptions, false);
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = MainSlider.createTitleText(title, TextAlign.LEFT, this);
	}

	private void createFullscreenOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Fullscreen: " + (FlounderEngine.getDevices().getDisplay().isFullscreen() ? "On" : "Off"), TextAlign.LEFT, yPos, this);
		button.addLeftListener(() -> {
			FlounderEngine.getDevices().getDisplay().setFullscreen(!FlounderEngine.getDevices().getDisplay().isFullscreen());
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean isFullscreen = FlounderEngine.getDevices().getDisplay().isFullscreen();

			@Override
			public boolean eventTriggered() {
				boolean newIsFullscreen = FlounderEngine.getDevices().getDisplay().isFullscreen();
				boolean occurred = newIsFullscreen != isFullscreen;
				isFullscreen = newIsFullscreen;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Fullscreen: " + (isFullscreen ? "On" : "Off"));
			}
		});
	}

	private void createAntialiasOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Antialiasing: " + (FlounderEngine.getDevices().getDisplay().isAntialiasing() ? "On" : "Off"), TextAlign.LEFT, yPos, this);
		button.addLeftListener(() -> {
			FlounderEngine.getDevices().getDisplay().setAntialiasing(!FlounderEngine.getDevices().getDisplay().isAntialiasing());
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean isAntialiasing = FlounderEngine.getDevices().getDisplay().isAntialiasing();

			@Override
			public boolean eventTriggered() {
				boolean newIsAntialiasing = FlounderEngine.getDevices().getDisplay().isAntialiasing();
				boolean occurred = newIsAntialiasing != isAntialiasing;
				isAntialiasing = newIsAntialiasing;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Antialiasing: " + (isAntialiasing ? "On" : "Off"));
			}
		});
	}

	private void createVSyncOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("VSync: " + (FlounderEngine.getDevices().getDisplay().isVSync() ? "On" : "Off"), TextAlign.LEFT, yPos, this);
		button.addLeftListener(() -> {
			FlounderEngine.getDevices().getDisplay().setVSync(!FlounderEngine.getDevices().getDisplay().isVSync());
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean isVSync = FlounderEngine.getDevices().getDisplay().isVSync();

			@Override
			public boolean eventTriggered() {
				boolean newIsVSync = FlounderEngine.getDevices().getDisplay().isVSync();
				boolean occurred = newIsVSync != isVSync;
				isVSync = newIsVSync;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("VSync: " + (isVSync ? "On" : "Off"));
			}
		});
	}

	private void createSamplesOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Samples: " + FlounderEngine.getDevices().getDisplay().getSamples(), TextAlign.RIGHT, yPos, this);
		button.addLeftListener(() -> {
			int newSamples = FlounderEngine.getDevices().getDisplay().getSamples() + 1;
			FlounderEngine.getDevices().getDisplay().setSamples(Math.min(32, newSamples));
		});
		button.addRightListener(() -> {
			int newSamples = FlounderEngine.getDevices().getDisplay().getSamples() - 1;
			FlounderEngine.getDevices().getDisplay().setSamples(Math.max(0, newSamples));
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private int samples = FlounderEngine.getDevices().getDisplay().getSamples();

			@Override
			public boolean eventTriggered() {
				int newSamples = FlounderEngine.getDevices().getDisplay().getSamples();
				boolean occurred = newSamples != samples;
				samples = newSamples;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Samples: " + FlounderEngine.getDevices().getDisplay().getSamples());
			}
		});
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", TextAlign.LEFT, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
