package game.uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenOptionsGraphics extends GuiComponent {
	private final MenuGame menuGame;
	private final ScreenOption screenOption;

	private final ScreenOptionsPost screenOptionsPost;

	protected ScreenOptionsGraphics(final ScreenOption screenOption, MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOption = screenOption;

		this.screenOptionsPost = new ScreenOptionsPost(this, menuGame);

		createTitleText("Graphic Options");

		createFullscreenOption(MenuMain.BUTTONS_X_LEFT_POS, 0.2f);
		createAntialiasOption(MenuMain.BUTTONS_X_LEFT_POS, 0.5f);

		createVSyncOption(MenuMain.BUTTONS_X_RIGHT_POS, 0.2f);
		createPostOption(MenuMain.BUTTONS_X_RIGHT_POS, 0.5f);

		createBackOption(MenuMain.BUTTONS_X_CENTER_POS, 1.0f);

		super.addActionListener(new GuiListenerAdvanced() {
			@Override
			public boolean hasOccurred() {
				return MenuGame.BACK_KEY.wasDown();
			}

			@Override
			public void run() {
				menuGame.setNewSecondaryScreen(screenOption, false);
			}
		});
	}

	private void createTitleText(final String title) {
		final Text titleText = Text.newText(title).center().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createFullscreenOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Fullscreen: " + (FlounderDevices.getDisplay().isFullscreen() ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderDevices.getDisplay().setFullscreen(!FlounderDevices.getDisplay().isFullscreen());
			button.getText().setText("Fullscreen: " + (FlounderDevices.getDisplay().isFullscreen() ? "On" : "Off"));
		});
	}

	private void createAntialiasOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Antialiasing: " + (FlounderDevices.getDisplay().isAntialiasing() ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderDevices.getDisplay().setAntialiasing(!FlounderDevices.getDisplay().isAntialiasing());
			button.getText().setText("Antialiasing: " + (FlounderDevices.getDisplay().isAntialiasing() ? "On" : "Off"));
		});
	}

	private void createVSyncOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("VSync: " + (FlounderDevices.getDisplay().isEnableVSync() ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderDevices.getDisplay().setEnableVSync(!FlounderDevices.getDisplay().isEnableVSync());
			button.getText().setText("VSync: " + (FlounderDevices.getDisplay().isEnableVSync() ? "On" : "Off"));
		});
	}

	private void createPostOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Post Effects", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsPost, true));
	}

	private void createBackOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOption, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
