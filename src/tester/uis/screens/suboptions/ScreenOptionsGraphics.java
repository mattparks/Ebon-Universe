package tester.uis.screens.suboptions;

import flounder.devices.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import tester.uis.*;
import tester.uis.screens.*;

import java.util.*;

public class ScreenOptionsGraphics extends GuiComponent {
	private MainSlider mainSlider;
	private ScreenOptions screenOptions;

	public ScreenOptionsGraphics(ScreenOptions screenOptions, MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		this.screenOptions = screenOptions;

		createTitleText(GuiAlign.LEFT, "Graphics");

		float currentY = -0.15f;
		createFullscreenOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createAntialiasOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createVSyncOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createSamplesOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(MainSlider.BUTTONS_X_MAGIN_LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
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

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, guiAlign, this);
	}

	private void createFullscreenOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Fullscreen: " + (FlounderDisplay.isFullscreen() ? "On" : "Off"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			FlounderDisplay.setFullscreen(!FlounderDisplay.isFullscreen());
		});

		FlounderEvents.addEvent(new IEvent() {
			private boolean isFullscreen = FlounderDisplay.isFullscreen();

			@Override
			public boolean eventTriggered() {
				boolean newIsFullscreen = FlounderDisplay.isFullscreen();
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

	private void createAntialiasOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Antialiasing: " + (FlounderDisplay.isAntialiasing() ? "On" : "Off"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			FlounderDisplay.setAntialiasing(!FlounderDisplay.isAntialiasing());
		});

		FlounderEvents.addEvent(new IEvent() {
			private boolean isAntialiasing = FlounderDisplay.isAntialiasing();

			@Override
			public boolean eventTriggered() {
				boolean newIsAntialiasing = FlounderDisplay.isAntialiasing();
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

	private void createVSyncOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("VSync: " + (FlounderDisplay.isVSync() ? "On" : "Off"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			FlounderDisplay.setVSync(!FlounderDisplay.isVSync());
		});

		FlounderEvents.addEvent(new IEvent() {
			private boolean isVSync = FlounderDisplay.isVSync();

			@Override
			public boolean eventTriggered() {
				boolean newIsVSync = FlounderDisplay.isVSync();
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

	private void createSamplesOption(GuiAlign guiAlign, float yPos) {
	/*	GuiTextButton button = MainSlider.createButton("Samples: " + ((EbonRenderer) Ebon.getMasterRenderer()).getSamples(), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			int newSamples = ((EbonRenderer) Ebon.getMasterRenderer()).getSamples() + 1;
			((EbonRenderer) Ebon.getMasterRenderer()).setSamples(Math.min(32, newSamples));
		});
		button.addRightListener(() -> {
			int newSamples = ((EbonRenderer) Ebon.getMasterRenderer()).getSamples() - 1;
			((EbonRenderer) Ebon.getMasterRenderer()).setSamples(Math.max(0, newSamples));
		});

		FlounderEvents.addEvent(new IEvent() {
			private int samples = ((EbonRenderer) Ebon.getMasterRenderer()).getSamples();

			@Override
			public boolean eventTriggered() {
				int newSamples = ((EbonRenderer) Ebon.getMasterRenderer()).getSamples();
				boolean occurred = newSamples != samples;
				samples = newSamples;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Samples: " + ((EbonRenderer) Ebon.getMasterRenderer()).getSamples());
			}
		});*/
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", GuiAlign.LEFT, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
