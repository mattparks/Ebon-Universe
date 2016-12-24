package ebon.uis.screens.suboptions;

import ebon.options.*;
import ebon.uis.*;
import ebon.uis.screens.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenOptionsEffects extends GuiComponent {
	private MasterSlider masterSlider;
	private ScreenOptions screenOptionsGraphics;

	public ScreenOptionsEffects(ScreenOptions screenOptionsGraphics, MasterSlider masterSlider) {
		this.masterSlider = masterSlider;
		this.screenOptionsGraphics = screenOptionsGraphics;

		createTitleText(GuiAlign.LEFT, "Effects");

		float currentY = -0.15f;
		createPostEnabledOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createPostEffectOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createFilterFXAAOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsEffects.super.isShown() && MasterSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				masterSlider.setNewSecondaryScreen(screenOptionsGraphics, false);
			}
		});
	}

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MasterSlider.createTitleText(title, guiAlign, this);
	}

	private void createPostEffectOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Post Effect: " + OptionsPost.POST_EFFECT, guiAlign, yPos, this);
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
		GuiTextButton button = MasterSlider.createButton("Post Enabled: " + (OptionsPost.POST_ENABLED ? "On" : "Off"), guiAlign, yPos, this);
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
		GuiTextButton button = MasterSlider.createButton("FXAA: " + (OptionsPost.FILTER_FXAA ? "Enabled" : "Disabled"), guiAlign, yPos, this);
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
		GuiTextButton button = MasterSlider.createButton("Back", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptionsGraphics, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
