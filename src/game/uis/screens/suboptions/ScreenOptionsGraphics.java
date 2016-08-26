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
		createFullscreenOption(MainSlider.BUTTONS_X_POS, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createAntialiasOption(MainSlider.BUTTONS_X_POS, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createVSyncOption(MainSlider.BUTTONS_X_POS, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(MainSlider.BUTTONS_X_POS, 1.0f);

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
		Text titleText = MenuStart.createTitleText(title, this);
	}

	private void createFullscreenOption(float xPos, float yPos) {
		GuiTextButton button = MenuStart.createButton("Fullscreen: " + (FlounderEngine.getDevices().getDisplay().isFullscreen() ? "On" : "Off"), xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
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
		GuiTextButton button = MenuStart.createButton("Antialiasing: " + (FlounderEngine.getDevices().getDisplay().isAntialiasing() ? "On" : "Off"), xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
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
		GuiTextButton button = MenuStart.createButton("VSync: " + (FlounderEngine.getDevices().getDisplay().isVSync() ? "On" : "Off"), xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
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

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MenuStart.createButton("Back", xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
