package ebon.uis.screens.suboptions;

import ebon.uis.*;
import ebon.uis.screens.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;

import java.util.*;

public class ScreenOptionsDeveloper extends GuiComponent {
	private MasterSlider masterSlider;
	private ScreenOptions screenOptions;

	public ScreenOptionsDeveloper(ScreenOptions screenOptions, MasterSlider masterSlider) {
		this.masterSlider = masterSlider;
		this.screenOptions = screenOptions;

		createTitleText(GuiAlign.LEFT, "Developers");

		float currentY = -0.15f;
		createProfilerToggleOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createAABBToggleOption(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);

		createBackOption(GuiAlign.LEFT, 1.0f);

		super.show(false);

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsDeveloper.super.isShown() && MasterSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				masterSlider.setNewSecondaryScreen(screenOptions, false);
			}
		});
	}

	private void createTitleText(GuiAlign guiAlign, String title) {
		Text titleText = MasterSlider.createTitleText(title, guiAlign, this);
	}

	private void createProfilerToggleOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Profiler: " + (FlounderProfiler.isOpen() ? "Opened" : "Closed"), guiAlign, yPos, this);
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
		GuiTextButton button = MasterSlider.createButton("AABBs: " + (FlounderBounding.renders() ? "Enabled" : "Disabled"), guiAlign, yPos, this);
		button.addLeftListener(() -> {
			FlounderBounding.toggle(!FlounderBounding.renders());
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

	private void createBackOption(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Back", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
