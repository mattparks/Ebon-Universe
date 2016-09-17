package game.uis.screens.suboptions;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.uis.*;
import game.uis.screens.*;

import java.util.*;

public class ScreenOptionsDeveloper extends GuiComponent {
	private MainSlider mainSlider;
	private ScreenOptions screenOptions;

	public ScreenOptionsDeveloper(ScreenOptions screenOptions, MainSlider mainSlider) {
		this.mainSlider = mainSlider;
		this.screenOptions = screenOptions;

		createTitleText(TextAlign.LEFT, "Developers");

		float currentY = -0.15f;
		createProfilerToggleOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createAABBToggleOption(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		createBackOption(MainSlider.BUTTONS_X_MAGIN_LEFT, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
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

	private void createTitleText(TextAlign textAlign, String title) {
		Text titleText = MainSlider.createTitleText(title, textAlign, this);
	}

	private void createProfilerToggleOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Profiler: " + (FlounderEngine.getProfiler().isOpen() ? "Opened" : "Closed"), textAlign, yPos, this);
		button.addLeftListener(() -> {
			FlounderEngine.getProfiler().toggle(!FlounderEngine.getProfiler().isOpen());
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean isOpen = FlounderEngine.getProfiler().isOpen();

			@Override
			public boolean eventTriggered() {
				boolean newIsOpen = FlounderEngine.getProfiler().isOpen();
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

	private void createAABBToggleOption(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("AABBs: " + (FlounderEngine.getBounding().renders() ? "Enabled" : "Disabled"), textAlign, yPos, this);
		button.addLeftListener(() -> {
			FlounderEngine.getBounding().setRenders(!FlounderEngine.getBounding().renders());
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean renders = FlounderEngine.getBounding().renders();

			@Override
			public boolean eventTriggered() {
				boolean newRenders = FlounderEngine.getBounding().renders();
				boolean occurred = newRenders != renders;
				renders = newRenders;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("AABBs: " + (FlounderEngine.getBounding().renders() ? "Enabled" : "Disabled"));
			}
		});
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainSlider.createButton("Back", TextAlign.LEFT, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
