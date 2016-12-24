package ebon.uis.screens.suboptions;

import ebon.uis.*;
import ebon.uis.screens.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenOptionsInputs extends GuiComponent {
	private ScreenOptions screenOptions;
	private MasterSlider masterSlider;

	public ScreenOptionsInputs(ScreenOptions screenOptions, MasterSlider masterSlider) {
		this.masterSlider = masterSlider;
		this.screenOptions = screenOptions;

		createTitleText(GuiAlign.LEFT, "Inputs");

		float currentY = -0.15f;

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsInputs.super.isShown() && MasterSlider.BACK_KEY.wasDown();
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
