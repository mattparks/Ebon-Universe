package game.uis.screens;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.uis.*;

import java.util.*;

public class ScreenAbout extends GuiComponent {
	private MainMenuSlider mainMenuSlider;

	public ScreenAbout(MainMenuSlider mainMenuSlider) {
		this.mainMenuSlider = mainMenuSlider;

		createTitleText("About");

		createBackOption(MainMenuContent.BUTTONS_X_POS, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenAbout.super.isShown() && MainMenuSlider.BACK_KEY.wasDown();
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
