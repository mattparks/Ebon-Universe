package game.uis;

import flounder.fonts.*;
import flounder.guis.*;
import flounder.profiling.*;

import java.util.*;

public class ScreenOptionsDeveloper extends GuiComponent {
	private final MenuGame menuGame;
	private final ScreenOption screenOption;

	protected ScreenOptionsDeveloper(final ScreenOption screenOption, final MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOption = screenOption;

		createTitleText("Dev Options");

		createProfilerToggleOption(MenuMain.BUTTONS_X_CENTER_POS, 0.2f);

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

	private void createProfilerToggleOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Profiler: " + (FlounderProfiler.isOpen() ? "Opened" : "Closed"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderProfiler.toggle(!FlounderProfiler.isOpen());
			button.getText().setText("Profiler: " + (FlounderProfiler.isOpen() ? "Opened" : "Closed"));
		});
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
