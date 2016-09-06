package game.uis.screens;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.*;
import game.uis.*;

import java.util.*;

public class ScreenPlay extends GuiComponent {
	private MainSlider mainSlider;

	public ScreenPlay(MainSlider mainSlider) {
		this.mainSlider = mainSlider;

		createTitleText(TextAlign.LEFT, "Play");

		float currentY = -0.15f;
		createSingleplayerOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createMultiplayerOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(TextAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenPlay.super.isShown() && MainSlider.BACK_KEY.wasDown();
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

	private void createSingleplayerOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Singleplayer", textAlign, yPos, this);
		button.addLeftListener(() -> {
			((MainGame) FlounderEngine.getGame()).generateWorlds();
			((MainGame) FlounderEngine.getGame()).generatePlayer();
			mainSlider.getSuperMenu().display(false);
			mainSlider.sliderStartMenu(false);
			mainSlider.closeSecondaryScreen();
		});
	}

	private void createMultiplayerOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Multiplayer", textAlign, yPos, this);
		//button.addLeftListener(() -> mainSlider.getMenuStart().getSuperMenu().display(false));
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
