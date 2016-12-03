package ebon.uis.screens;

import ebon.uis.*;
import ebon.world.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenPlay extends GuiComponent {
	private MainSlider mainSlider;

	public ScreenPlay(MainSlider mainSlider) {
		this.mainSlider = mainSlider;

		createTitleText(GuiAlign.LEFT, "Play");

		float currentY = -0.15f;
		createSingleplayerOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createMultiplayerOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
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

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, guiAlign, this);
	}

	private void createSingleplayerOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Singleplayer", guiAlign, yPos, this);
		button.addLeftListener(() -> {
			EbonWorld.generateWorlds();
			mainSlider.getSuperMenu().display(false);
			mainSlider.sliderStartMenu(false);
			mainSlider.closeSecondaryScreen();
		});
	}

	private void createMultiplayerOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Multiplayer", guiAlign, yPos, this);
		//button.addLeftListener(() -> mainSlider.getMenuStart().getSuperMenu().display(false));
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
