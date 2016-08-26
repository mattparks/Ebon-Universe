package game.uis.screens;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.uis.*;
import game.uis.screens.suboptions.*;

import java.util.*;

public class ScreenOptions extends GuiComponent {
	private MainMenuSlider mainMenuSlider;
	private ScreenOptionsAudio screenOptionsAudio;
	private ScreenOptionsDeveloper screenOptionsDeveloper;
	private ScreenOptionsGraphics screenOptionsGraphics;
	private ScreenOptionsEffects screenOptionsEffects;
	private ScreenOptionsInputs screenOptionsInputs;

	public ScreenOptions(MainMenuSlider mainMenuSlider) {
		this.mainMenuSlider = mainMenuSlider;
		screenOptionsAudio = new ScreenOptionsAudio(this, mainMenuSlider);
		screenOptionsDeveloper = new ScreenOptionsDeveloper(this, mainMenuSlider);
		screenOptionsGraphics = new ScreenOptionsGraphics(this, mainMenuSlider);
		screenOptionsEffects = new ScreenOptionsEffects(this, mainMenuSlider);
		screenOptionsInputs = new ScreenOptionsInputs(this, mainMenuSlider);

		createTitleText("Options");

		float currentY = -0.15f;
		createAudioOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);
		createDevelopersOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);
		createGraphicsOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);
		createEffectsOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);
		createInputsOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);

		createBackOption(MainMenuContent.BUTTONS_X_POS, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptions.super.isShown() && MainMenuSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainMenuSlider.closeSecondaryScreen();
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = MainMenuContent.createTitleText(title, this);
	}

	private void createAudioOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Audio", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptionsAudio, true));
	}

	private void createDevelopersOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Developer", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptionsDeveloper, true));
	}

	private void createGraphicsOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Graphics", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptionsGraphics, true));
	}

	private void createEffectsOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Effects", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptionsEffects, true));
	}

	private void createInputsOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Inputs", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptionsInputs, true));
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Back", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(mainMenuSlider::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
