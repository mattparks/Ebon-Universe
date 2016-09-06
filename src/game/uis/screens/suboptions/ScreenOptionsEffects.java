package game.uis.screens.suboptions;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.options.*;
import game.uis.*;
import game.uis.screens.*;

import java.util.*;

public class ScreenOptionsEffects extends GuiComponent {
	private MainSlider mainSlider;
	private ScreenOptions screenOptionsGraphics;

	public ScreenOptionsEffects(ScreenOptions screenOptionsGraphics, MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		this.screenOptionsGraphics = screenOptionsGraphics;

		createTitleText(TextAlign.LEFT, "Effects");

		float currentY = -0.15f;
		createPostEnabledOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createPostEffectOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createFilterFXAAOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(TextAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsEffects.super.isShown() && MainSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainSlider.setNewSecondaryScreen(screenOptionsGraphics, false);
			}
		});
	}

	private void createTitleText(TextAlign textAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, textAlign, this);
	}

	private void createPostEffectOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Post Effect: " + OptionsPost.POST_EFFECT, textAlign, yPos, this);
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

	private void createPostEnabledOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Post Enabled: " + (OptionsPost.POST_ENABLED ? "On" : "Off"), textAlign, yPos, this);
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

	private void createFilterFXAAOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("FXAA: " + (OptionsPost.FILTER_FXAA ? "Enabled" : "Disabled"), textAlign, yPos, this);
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

	private void createBackOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsGraphics, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
