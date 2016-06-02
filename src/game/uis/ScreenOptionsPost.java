package game.uis;

import flounder.fonts.*;
import flounder.guis.*;
import game.options.*;

import java.util.*;

public class ScreenOptionsPost extends GuiComponent {
	private final MenuGame menuGame;
	private final ScreenOptionsGraphics screenOptionsGraphics;

	protected ScreenOptionsPost(final ScreenOptionsGraphics screenOptionsGraphics, MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOptionsGraphics = screenOptionsGraphics;

		createTitleText("Post Graphics");

		createPostOption(MenuMain.BUTTONS_X_CENTER_POS, 0.2f);
		createPostEnabledOption(MenuMain.BUTTONS_X_CENTER_POS, 0.5f);

		createBackOption(MenuMain.BUTTONS_X_CENTER_POS, 1.0f);

		super.addActionListener(new GuiListenerAdvanced() {
			@Override
			public boolean hasOccurred() {
				return MenuGame.BACK_KEY.wasDown();
			}

			@Override
			public void run() {
				menuGame.setNewSecondaryScreen(screenOptionsGraphics, false);
			}
		});
	}

	private void createTitleText(final String title) {
		final Text titleText = Text.newText(title).center().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createPostOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Post Effect: " + (OptionsPost.POST_ENABLED ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			OptionsPost.POST_EFFECT += 1;

			if (OptionsPost.POST_EFFECT > OptionsPost.POST_EFFECT_MAX) {
				OptionsPost.POST_EFFECT = OptionsPost.POST_EFFECT_MAX;
			}

			button.getText().setText("Post Effect: " + OptionsPost.POST_EFFECT);
		});

		button.addRightListener(() -> {
			OptionsPost.POST_EFFECT -= 1;

			if (OptionsPost.POST_EFFECT < 0) {
				OptionsPost.POST_EFFECT = 0;
			}

			button.getText().setText("Post Effect: " + OptionsPost.POST_EFFECT);
		});
	}

	private void createPostEnabledOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Post Enabled: " + (OptionsPost.POST_ENABLED ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			OptionsPost.POST_ENABLED = !OptionsPost.POST_ENABLED;
			button.getText().setText("Post Enabled: " + (OptionsPost.POST_ENABLED ? "On" : "Off"));
		});
	}

	private void createBackOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsGraphics, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
