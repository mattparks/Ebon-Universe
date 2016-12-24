package ebon.uis.screens;

import ebon.uis.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenAbout extends GuiComponent {
	private MasterSlider masterSlider;

	public ScreenAbout(MasterSlider masterSlider) {
		this.masterSlider = masterSlider;

		createTitleText("About");

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenAbout.super.isShown() && MasterSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				masterSlider.closeSecondaryScreen();
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = MasterSlider.createTitleText(title, GuiAlign.LEFT, this);
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
