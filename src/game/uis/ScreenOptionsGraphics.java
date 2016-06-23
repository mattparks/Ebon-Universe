package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.visual.*;

import java.util.*;

public class ScreenOptionsGraphics extends GuiComponent {
	private MenuGame menuGame;
	private ScreenOption screenOption;

	private ScreenOptionsPost screenOptionsPost;

	protected ScreenOptionsGraphics(ScreenOption screenOption, MenuGame menuGame) {
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

	private void createTitleText(String title) {
		Text titleText = Text.newText(title).center().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		titleText.setBorderColour(0.15f, 0.15f, 0.15f);
		titleText.setBorder(new ConstantDriver(0.04f));
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createFullscreenOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Fullscreen: " + (FlounderEngine.getDevices().getDisplay().isFullscreen() ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderEngine.getDevices().getDisplay().setFullscreen(!FlounderEngine.getDevices().getDisplay().isFullscreen());
		});

		button.addActionListener(new GuiListenerAdvanced() {
			private boolean isFullscreen = FlounderEngine.getDevices().getDisplay().isFullscreen();

			@Override
			public boolean hasOccurred() {
				boolean newIsFullscreen = FlounderEngine.getDevices().getDisplay().isFullscreen();
				boolean occurred = newIsFullscreen != isFullscreen;
				isFullscreen = newIsFullscreen;
				return occurred;
			}

			@Override
			public void run() {
				button.getText().setText("Fullscreen: " + (isFullscreen ? "On" : "Off"));
			}
		});
	}

	private void createAntialiasOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Antialiasing: " + (FlounderEngine.getDevices().getDisplay().isAntialiasing() ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderEngine.getDevices().getDisplay().setAntialiasing(!FlounderEngine.getDevices().getDisplay().isAntialiasing());
		});

		button.addActionListener(new GuiListenerAdvanced() {
			private boolean isAntialiasing = FlounderEngine.getDevices().getDisplay().isAntialiasing();

			@Override
			public boolean hasOccurred() {
				boolean newIsAntialiasing = FlounderEngine.getDevices().getDisplay().isAntialiasing();
				boolean occurred = newIsAntialiasing != isAntialiasing;
				isAntialiasing = newIsAntialiasing;
				return occurred;
			}

			@Override
			public void run() {
				button.getText().setText("Antialiasing: " + (isAntialiasing ? "On" : "Off"));
			}
		});
	}

	private void createVSyncOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("VSync: " + (FlounderEngine.getDevices().getDisplay().isVSync() ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderEngine.getDevices().getDisplay().setVSync(!FlounderEngine.getDevices().getDisplay().isVSync());
		});

		button.addActionListener(new GuiListenerAdvanced() {
			private boolean isVSync = FlounderEngine.getDevices().getDisplay().isVSync();

			@Override
			public boolean hasOccurred() {
				boolean newIsVSync = FlounderEngine.getDevices().getDisplay().isVSync();
				boolean occurred = newIsVSync != isVSync;
				isVSync = newIsVSync;
				return occurred;
			}

			@Override
			public void run() {
				button.getText().setText("VSync: " + (isVSync ? "On" : "Off"));
			}
		});
	}

	private void createPostOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Post Effects", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsPost, true));
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOption, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
