package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.visual.*;

import java.util.*;

public class ScreenOptionsDeveloper extends GuiComponent {
	private MenuGame menuGame;
	private ScreenOption screenOption;

	protected ScreenOptionsDeveloper(ScreenOption screenOption, MenuGame menuGame) {
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

	private void createTitleText(String title) {
		Text titleText = Text.newText(title).center().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		titleText.setBorderColour(0.15f, 0.15f, 0.15f);
		titleText.setBorder(new ConstantDriver(0.04f));
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createProfilerToggleOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Profiler: " + (FlounderEngine.getProfiler().isOpen() ? "Opened" : "Closed"), xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(() -> {
			FlounderEngine.getProfiler().toggle(!FlounderEngine.getProfiler().isOpen());
		});

		button.addActionListener(new GuiListenerAdvanced() {
			private boolean isOpen = FlounderEngine.getProfiler().isOpen();

			@Override
			public boolean hasOccurred() {
				boolean newIsOpen = FlounderEngine.getProfiler().isOpen();
				boolean occurred = newIsOpen != isOpen;
				isOpen = newIsOpen;
				return occurred;
			}

			@Override
			public void run() {
				button.getText().setText("Profiler: " + (isOpen ? "Opened" : "Closed"));
			}
		});
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
