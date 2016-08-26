package game.uis.screens.suboptions;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.uis.*;
import game.uis.screens.*;

import java.util.*;

public class ScreenOptionsDeveloper extends GuiComponent {
	private MainMenuSlider mainMenuSlider;
	private ScreenOptions screenOptions;

	public ScreenOptionsDeveloper(ScreenOptions screenOptions, MainMenuSlider mainMenuSlider) {
		this.mainMenuSlider = mainMenuSlider;
		this.screenOptions = screenOptions;

		createTitleText("Developers");

		float currentY = -0.15f;
		createProfilerToggleOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);
		createAABBToggleOption(MainMenuContent.BUTTONS_X_POS, currentY += MainMenuContent.BUTTONS_Y_SEPARATION);

		createBackOption(MainMenuContent.BUTTONS_X_POS, 1.0f);

		super.show(false);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return ScreenOptionsDeveloper.super.isShown() && MainMenuSlider.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				mainMenuSlider.setNewSecondaryScreen(screenOptions, false);
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = MainMenuContent.createTitleText(title, this);
	}

	private void createProfilerToggleOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Profiler: " + (FlounderEngine.getProfiler().isOpen() ? "Opened" : "Closed"), xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
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

	private void createAABBToggleOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("AABBs: " + (FlounderEngine.getAABBs().renders() ? "Enabled" : "Disabled"), xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderEngine.getAABBs().setRenders(!FlounderEngine.getAABBs().renders());
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private boolean renders = FlounderEngine.getAABBs().renders();

			@Override
			public boolean eventTriggered() {
				boolean newRenders = FlounderEngine.getAABBs().renders();
				boolean occurred = newRenders != renders;
				renders = newRenders;
				return occurred;
			}

			@Override
			public void onEvent() {
				button.getText().setText("AABBs: " + (FlounderEngine.getAABBs().renders() ? "Enabled" : "Disabled"));
			}
		});
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MainMenuContent.createButton("Back", xPos, yPos, MainMenuContent.BUTTONS_X_WIDTH, MainMenuContent.BUTTONS_Y_SIZE, MainMenuContent.FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptions, false));
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
