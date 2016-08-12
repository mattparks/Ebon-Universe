package game.uis;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.visual.*;
import game.options.*;

import java.util.*;

public class ScreenOptionsPost extends GuiComponent {
	private MenuGame menuGame;
	private ScreenOptionsGraphics screenOptionsGraphics;

	protected ScreenOptionsPost(ScreenOptionsGraphics screenOptionsGraphics, MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOptionsGraphics = screenOptionsGraphics;

		createTitleText("Post Graphics");

		createPostOption(MenuMain.BUTTONS_X_LEFT_POS, 0.2f);
		createPostEnabledOption(MenuMain.BUTTONS_X_LEFT_POS, 0.5f);

		createFilterFXAAOption(MenuMain.BUTTONS_X_RIGHT_POS, 0.2f);

		createBackOption(MenuMain.BUTTONS_X_CENTRE_POS, 1.0f);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return MenuGame.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				menuGame.setNewSecondaryScreen(screenOptionsGraphics, false);
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = Text.newText(title).centre().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		titleText.setBorderColour(0.15f, 0.15f, 0.15f);
		titleText.setBorder(new ConstantDriver(0.04f));
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createPostOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Post Effect: " + OptionsPost.POST_EFFECT, xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			OptionsPost.POST_EFFECT += 1;

			if (OptionsPost.POST_EFFECT > OptionsPost.POST_EFFECT_MAX) {
				OptionsPost.POST_EFFECT = OptionsPost.POST_EFFECT_MAX;
			}
		});

		button.addRightListener(() -> {
			OptionsPost.POST_EFFECT -= 1;

			if (OptionsPost.POST_EFFECT < 0) {
				OptionsPost.POST_EFFECT = 0;
			}
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private int currentEffect = OptionsPost.POST_EFFECT;

			@Override
			public boolean eventTriggered() {
				int newCurrentEffect = OptionsPost.POST_EFFECT;
				boolean occurred = newCurrentEffect != currentEffect;
				currentEffect = newCurrentEffect;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Post Effect: " + currentEffect);
			}
		});
	}

	private void createPostEnabledOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Post Enabled: " + (OptionsPost.POST_ENABLED ? "On" : "Off"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			OptionsPost.POST_ENABLED = !OptionsPost.POST_ENABLED;
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean postEnabled = OptionsPost.POST_ENABLED;

			@Override
			public boolean eventTriggered() {
				boolean newPostEnabled = OptionsPost.POST_ENABLED;
				boolean occurred = newPostEnabled != postEnabled;
				postEnabled = newPostEnabled;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Post Enabled: " + (postEnabled ? "On" : "Off"));
			}
		});
	}

	private void createFilterFXAAOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("FXAA: " + (OptionsPost.FILTER_FXAA ? "Enabled" : "Disabled"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			OptionsPost.FILTER_FXAA = !OptionsPost.FILTER_FXAA;
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean fxaaEnabled = OptionsPost.FILTER_FXAA;

			@Override
			public boolean eventTriggered() {
				boolean newFxaaEnabled = OptionsPost.FILTER_FXAA;
				boolean occurred = newFxaaEnabled != fxaaEnabled;
				fxaaEnabled = newFxaaEnabled;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("FXAA: " + (fxaaEnabled ? "Enabled" : "Disabled"));
			}
		});
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOptionsGraphics, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
