package game.uis.screens;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.uis.*;

import java.util.*;

public class ScreenAbout extends GuiComponent {
	private MainSlider mainSlider;

	public ScreenAbout(MainSlider mainSlider) {
		this.mainSlider = mainSlider;

		createTitleText("About");

		createBackOption(TextAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenAbout.super.isShown() && MainSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainSlider.closeSecondaryScreen();
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = MainSlider.createTitleText(title, TextAlign.LEFT, this);
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
