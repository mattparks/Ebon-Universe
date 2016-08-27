package game.uis.screens;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.uis.*;
import game.uis.screens.suboptions.*;

import java.util.*;

public class ScreenOptions extends GuiComponent {
	private MainSlider mainSlider;
	private ScreenOptionsAudio screenOptionsAudio;
	private ScreenOptionsDeveloper screenOptionsDeveloper;
	private ScreenOptionsGraphics screenOptionsGraphics;
	private ScreenOptionsEffects screenOptionsEffects;
	private ScreenOptionsInputs screenOptionsInputs;

	public ScreenOptions(MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		screenOptionsAudio = new ScreenOptionsAudio(this, mainSlider);
		screenOptionsDeveloper = new ScreenOptionsDeveloper(this, mainSlider);
		screenOptionsGraphics = new ScreenOptionsGraphics(this, mainSlider);
		screenOptionsEffects = new ScreenOptionsEffects(this, mainSlider);
		screenOptionsInputs = new ScreenOptionsInputs(this, mainSlider);

		createTitleText("Options");

		float currentY = -0.15f;
		createAudioOption(MainSlider.BUTTONS_X_POS, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createDevelopersOption(MainSlider.BUTTONS_X_POS, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createGraphicsOption(MainSlider.BUTTONS_X_POS, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createEffectsOption(MainSlider.BUTTONS_X_POS, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createInputsOption(MainSlider.BUTTONS_X_POS, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(MainSlider.BUTTONS_X_POS, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptions.super.isShown() && MainSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainSlider.closeSecondaryScreen();
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = MainSlider.createTitleText(title, this);
	}

	private void createAudioOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Audio", xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsAudio, true));
	}

	private void createDevelopersOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Developer", xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsDeveloper, true));
	}

	private void createGraphicsOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Graphics", xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsGraphics, true));
	}

	private void createEffectsOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Effects", xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsEffects, true));
	}

	private void createInputsOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Inputs", xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsInputs, true));
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", xPos, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(mainSlider::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
