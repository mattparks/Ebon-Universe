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
	private MainMenuSlider mainMenuSlider;

	public ScreenOptionsInputs(ScreenOptions screenOptions, MainMenuSlider mainMenuSlider) {
		this.mainMenuSlider = mainMenuSlider;
		this.screenOptions = screenOptions;

		createTitleText("Inputs");

		float currentY = -0.15f;

		createBackOption(MainMenuContent.BUTTONS_X_POS, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsInputs.super.isShown() && MainMenuSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainMenuSlider.setNewSecondaryScreen(screenOptions, false);
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = MainMenuContent.createTitleText(title, this);
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Back", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
