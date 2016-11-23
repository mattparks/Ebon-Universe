package tester.uis.screens;

import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import tester.uis.*;

import java.util.*;

public class ScreenControls extends GuiComponent {
	private MainSlider mainSlider;

	public ScreenControls(MainSlider mainSlider) {
		this.mainSlider = mainSlider;

		createTitleText(GuiAlign.LEFT, "Controls");

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenControls.super.isShown() && MainSlider.BACK_KEY.wasDown();
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
