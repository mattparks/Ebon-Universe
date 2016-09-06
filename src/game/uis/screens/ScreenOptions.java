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

		createTitleText(TextAlign.LEFT, "Options");

		float currentY = -0.15f;
		createAudioOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createDevelopersOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createGraphicsOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createEffectsOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createInputsOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(TextAlign.LEFT, 1.0f);

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

	private void createTitleText(TextAlign textAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, textAlign, this);
	}

	private void createAudioOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Audio", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsAudio, true));
	}

	private void createDevelopersOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Developer", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsDeveloper, true));
	}

	private void createGraphicsOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Graphics", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsGraphics, true));
	}

	private void createEffectsOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Effects", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsEffects, true));
	}

	private void createInputsOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Inputs", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsInputs, true));
	}

	private void createBackOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", textAlign, yPos, this);
		button.addLeftListener(mainSlider::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
