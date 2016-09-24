package ebon.uis.screens;

import ebon.uis.*;
import ebon.uis.screens.suboptions.*;
import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;

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

		createTitleText(GuiAlign.LEFT, "Options");

		float currentY = -0.15f;
		createAudioOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createDevelopersOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createGraphicsOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createEffectsOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createInputsOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(GuiAlign.LEFT, 1.0f);

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

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, guiAlign, this);
	}

	private void createAudioOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Audio", guiAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsAudio, true));
	}

	private void createDevelopersOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Developer", guiAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsDeveloper, true));
	}

	private void createGraphicsOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Graphics", guiAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsGraphics, true));
	}

	private void createEffectsOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Effects", guiAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsEffects, true));
	}

	private void createInputsOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Inputs", guiAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsInputs, true));
	}

	private void createBackOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", guiAlign, yPos, this);
		button.addLeftListener(mainSlider::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
