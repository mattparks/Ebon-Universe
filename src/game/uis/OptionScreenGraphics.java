package game.uis;

import flounder.devices.*;
import flounder.engine.options.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class OptionScreenGraphics extends GuiComponent {
	private final GameMenu gameMenu;

	protected OptionScreenGraphics(GameMenu menu) {
		gameMenu = menu;

		createFullscreenOption(OptionScreen.BUTTONS_X_LEFT_POS, 0.0f);
		createAntialiasOption(OptionScreen.BUTTONS_X_LEFT_POS, 0.2f);

		createVSyncOption(OptionScreen.BUTTONS_X_RIGHT_POS, 0.0f);
		createPostOption(OptionScreen.BUTTONS_X_RIGHT_POS, 0.2f);

		createBackOption(OptionScreen.BUTTONS_X_CENTER_POS, 0.9f);
	}

	private void createFullscreenOption(final float xPos, final float yPos) {
		final String fullscreenText = "Fullscreen: ";
		final Text text = Text.newText(fullscreenText + (ManagerDevices.getDisplay().isFullscreen() ? "On" : "Off")).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> {
			ManagerDevices.getDisplay().setFullscreen(!ManagerDevices.getDisplay().isFullscreen());
			text.setText(fullscreenText + (ManagerDevices.getDisplay().isFullscreen() ? "On" : "Off"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	private void createAntialiasOption(final float xPos, final float yPos) {
		final String msaaText = "Antialiasing: ";
		final Text text = Text.newText(msaaText + (ManagerDevices.getDisplay().isAntialiasing() ? "On" : "Off")).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> {
			ManagerDevices.getDisplay().setAntialiasing(!ManagerDevices.getDisplay().isAntialiasing());
			text.setText(msaaText + (ManagerDevices.getDisplay().isAntialiasing() ? "On" : "Off"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	private void createVSyncOption(final float xPos, final float yPos) {
		final String shadowText = "VSync: ";
		final Text text = Text.newText(shadowText + (ManagerDevices.getDisplay().isEnableVSync() ? "On" : "Off")).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> {
			ManagerDevices.getDisplay().setEnableVSync(!ManagerDevices.getDisplay().isEnableVSync());
			text.setText(shadowText + (ManagerDevices.getDisplay().isEnableVSync() ? "On" : "Off"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	private void createPostOption(final float xPos, final float yPos) {
		final String postTextPre = "Post Effect: ";
		final Text text = Text.newText(postTextPre + OptionsGraphics.POST_EFFECT).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listenerUp = () -> {
			OptionsGraphics.POST_EFFECT += 1;

			if (OptionsGraphics.POST_EFFECT > OptionsGraphics.POST_EFFECT_MAX) {
				OptionsGraphics.POST_EFFECT = OptionsGraphics.POST_EFFECT_MAX;
			}

			text.setText(postTextPre + OptionsGraphics.POST_EFFECT);
		};

		Listener listenerDown = () -> {
			OptionsGraphics.POST_EFFECT -= 1;

			if (OptionsGraphics.POST_EFFECT < 0) {
				OptionsGraphics.POST_EFFECT = 0;
			}

			text.setText(postTextPre + OptionsGraphics.POST_EFFECT);
		};

		button.addLeftListener(listenerUp);
		button.addRightListener(listenerDown);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> gameMenu.setNewSecondaryScreen(new OptionScreen(gameMenu));

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
