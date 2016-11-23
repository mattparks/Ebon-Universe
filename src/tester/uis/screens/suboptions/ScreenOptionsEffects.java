package tester.uis.screens.suboptions;

import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import tester.options.*;
import tester.uis.*;
import tester.uis.screens.*;

import java.util.*;

public class ScreenOptionsEffects extends GuiComponent {
	private MainSlider mainSlider;
	private ScreenOptions screenOptionsGraphics;

	public ScreenOptionsEffects(ScreenOptions screenOptionsGraphics, MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		this.screenOptionsGraphics = screenOptionsGraphics;

		createTitleText(GuiAlign.LEFT, "Effects");

		float currentY = -0.15f;
		createPostEnabledOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createPostEffectOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createFilterFXAAOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
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

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, guiAlign, this);
	}

	private void createPostEffectOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Post Effect: " + OptionsPost.POST_EFFECT, guiAlign, yPos, this);
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

		FlounderEvents.addEvent(new IEvent() {
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

	private void createPostEnabledOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Post Enabled: " + (OptionsPost.POST_ENABLED ? "On" : "Off"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			OptionsPost.POST_ENABLED = !OptionsPost.POST_ENABLED;
		});

		FlounderEvents.addEvent(new IEvent() {
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

	private void createFilterFXAAOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("FXAA: " + (OptionsPost.FILTER_FXAA ? "Enabled" : "Disabled"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			OptionsPost.FILTER_FXAA = !OptionsPost.FILTER_FXAA;
		});

		FlounderEvents.addEvent(new IEvent() {
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

	private void createBackOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", guiAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptionsGraphics, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
