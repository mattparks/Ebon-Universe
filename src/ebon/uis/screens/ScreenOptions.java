package ebon.uis.screens;

import ebon.uis.*;
import ebon.uis.screens.suboptions.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenOptions extends GuiComponent {
	private MasterSlider masterSlider;
	private ScreenOptionsAudio screenOptionsAudio;
	private ScreenOptionsDeveloper screenOptionsDeveloper;
	private ScreenOptionsGraphics screenOptionsGraphics;
	private ScreenOptionsEffects screenOptionsEffects;
	private ScreenOptionsInputs screenOptionsInputs;

	public ScreenOptions(MasterSlider masterSlider) {
		this.masterSlider = masterSlider;
		screenOptionsAudio = new ScreenOptionsAudio(this, masterSlider);
		screenOptionsDeveloper = new ScreenOptionsDeveloper(this, masterSlider);
		screenOptionsGraphics = new ScreenOptionsGraphics(this, masterSlider);
		screenOptionsEffects = new ScreenOptionsEffects(this, masterSlider);
		screenOptionsInputs = new ScreenOptionsInputs(this, masterSlider);

		createTitleText(GuiAlign.LEFT, "Options");

		float currentY = -0.15f;
		createAudioOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createDevelopersOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createGraphicsOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createEffectsOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createInputsOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptions.super.isShown() && MasterSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				masterSlider.closeSecondaryScreen();
			}
		});
	}

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MasterSlider.createTitleText(title, guiAlign, this);
	}

	private void createAudioOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Audio", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptionsAudio, true));
	}

	private void createDevelopersOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Developer", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptionsDeveloper, true));
	}

	private void createGraphicsOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Graphics", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptionsGraphics, true));
	}

	private void createEffectsOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Effects", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptionsEffects, true));
	}

	private void createInputsOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Inputs", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptionsInputs, true));
	}

	private void createBackOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Back", guiAlign, yPos, this);
		button.addLeftListener(masterSlider::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
