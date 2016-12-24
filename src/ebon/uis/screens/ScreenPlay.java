package ebon.uis.screens;

import ebon.uis.*;
import ebon.world.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenPlay extends GuiComponent {
	private MasterSlider masterSlider;

	public ScreenPlay(MasterSlider masterSlider) {
		this.masterSlider = masterSlider;

		createTitleText(GuiAlign.LEFT, "Play");

		float currentY = -0.15f;
		createSingleplayerOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createMultiplayerOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenPlay.super.isShown() && MasterSlider.BACK_KEY.wasDown();
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

	private void createSingleplayerOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Singleplayer", guiAlign, yPos, this);
		button.addLeftListener(() -> {
		//	EbonWorld.generateWorlds();
			masterSlider.getSuperMenu().display(false);
			masterSlider.sliderStartMenu(false);
			masterSlider.closeSecondaryScreen();
		});
	}

	private void createMultiplayerOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Multiplayer", guiAlign, yPos, this);
		//button.addLeftListener(() -> masterSlider.getMenuStart().getSuperMenu().display(false));
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
