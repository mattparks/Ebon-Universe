package tester.uis.screens.suboptions;

import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import tester.uis.*;
import tester.uis.screens.*;

import java.util.*;

public class ScreenOptionsDeveloper extends GuiComponent {
	private MainSlider mainSlider;
	private ScreenOptions screenOptions;

	public ScreenOptionsDeveloper(ScreenOptions screenOptions, MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		this.screenOptions = screenOptions;

		createTitleText(GuiAlign.LEFT, "Developers");

		float currentY = -0.15f;
		createProfilerToggleOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createAABBToggleOption(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(MainSlider.BUTTONS_X_MAGIN_LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsDeveloper.super.isShown() && MainSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainSlider.setNewSecondaryScreen(screenOptions, false);
			}
		});
	}

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, guiAlign, this);
	}

	private void createProfilerToggleOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Profiler: " + (FlounderProfiler.isOpen() ? "Opened" : "Closed"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			FlounderProfiler.toggle(!FlounderProfiler.isOpen());
		});

		FlounderEvents.addEvent(new IEvent() {
			private boolean isOpen = FlounderProfiler.isOpen();

			@Override
			public boolean eventTriggered() {
				boolean newIsOpen = FlounderProfiler.isOpen();
				boolean occurred = newIsOpen != isOpen;
				isOpen = newIsOpen;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("Profiler: " + (isOpen ? "Opened" : "Closed"));
			}
		});
	}

	private void createAABBToggleOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("AABBs: " + (FlounderBounding.renders() ? "Enabled" : "Disabled"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			FlounderBounding.setRenders(!FlounderBounding.renders());
		});

		FlounderEvents.addEvent(new IEvent() {
			private boolean renders = FlounderBounding.renders();

			@Override
			public boolean eventTriggered() {
				boolean newRenders = FlounderBounding.renders();
				boolean occurred = newRenders != renders;
				renders = newRenders;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("AABBs: " + (FlounderBounding.renders() ? "Enabled" : "Disabled"));
			}
		});
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", GuiAlign.LEFT, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
