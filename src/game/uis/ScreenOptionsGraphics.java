package game.uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.options.*;

import java.util.*;

public class ScreenOptionsGraphics extends GuiComponent {
	private final MenuGame menuGame;
	private final ScreenOption screenOption;

	protected ScreenOptionsGraphics(final ScreenOption screenOption, MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOption = screenOption;

		createFullscreenOption(ScreenOption.BUTTONS_X_LEFT_POS, 0.0f);
		createAntialiasOption(ScreenOption.BUTTONS_X_LEFT_POS, 0.2f);

		createVSyncOption(ScreenOption.BUTTONS_X_RIGHT_POS, 0.0f);
		createPostOption(ScreenOption.BUTTONS_X_RIGHT_POS, 0.2f);

		createBackOption(ScreenOption.BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createFullscreenOption(final float xPos, final float yPos) {
		final String fullscreenText = "Fullscreen: ";
		final Text text = Text.newText(fullscreenText + (ManagerDevices.getDisplay().isFullscreen() ? "On" : "Off")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> {
			ManagerDevices.getDisplay().setFullscreen(!ManagerDevices.getDisplay().isFullscreen());
			text.setText(fullscreenText + (ManagerDevices.getDisplay().isFullscreen() ? "On" : "Off"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createAntialiasOption(final float xPos, final float yPos) {
		final String msaaText = "Antialiasing: ";
		final Text text = Text.newText(msaaText + (ManagerDevices.getDisplay().isAntialiasing() ? "On" : "Off")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> {
			ManagerDevices.getDisplay().setAntialiasing(!ManagerDevices.getDisplay().isAntialiasing());
			text.setText(msaaText + (ManagerDevices.getDisplay().isAntialiasing() ? "On" : "Off"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createVSyncOption(final float xPos, final float yPos) {
		final String shadowText = "VSync: ";
		final Text text = Text.newText(shadowText + (ManagerDevices.getDisplay().isEnableVSync() ? "On" : "Off")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> {
			ManagerDevices.getDisplay().setEnableVSync(!ManagerDevices.getDisplay().isEnableVSync());
			text.setText(shadowText + (ManagerDevices.getDisplay().isEnableVSync() ? "On" : "Off"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createPostOption(final float xPos, final float yPos) {
		final String postTextPre = "Post Effect: ";
		final Text text = Text.newText(postTextPre + OptionsPost.POST_EFFECT).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listenerUp = () -> {
			OptionsPost.POST_EFFECT += 1;

			if (OptionsPost.POST_EFFECT > OptionsPost.POST_EFFECT_MAX) {
				OptionsPost.POST_EFFECT = OptionsPost.POST_EFFECT_MAX;
			}

			text.setText(postTextPre + OptionsPost.POST_EFFECT);
		};

		Listener listenerDown = () -> {
			OptionsPost.POST_EFFECT -= 1;

			if (OptionsPost.POST_EFFECT < 0) {
				OptionsPost.POST_EFFECT = 0;
			}

			text.setText(postTextPre + OptionsPost.POST_EFFECT);
		};

		button.addLeftListener(listenerUp);
		button.addRightListener(listenerDown);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> menuGame.setNewSecondaryScreen(screenOption, false);

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
