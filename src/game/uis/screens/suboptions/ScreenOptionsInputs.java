package game.uis.screens.suboptions;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.uis.*;
import game.uis.screens.*;

import java.util.*;

public class ScreenOptionsInputs extends GuiComponent {
	private ScreenOptions screenOptions;
	private MainSlider mainSlider;

	public ScreenOptionsInputs(ScreenOptions screenOptions, MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		this.screenOptions = screenOptions;

		createTitleText("Inputs");

		float currentY = -0.15f;

		createBackOption(MainSlider.BUTTONS_X_MAGIN_LEFT, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsInputs.super.isShown() && MainSlider.BACK_KEY.wasDown();
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
