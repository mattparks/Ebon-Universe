package game.uis;

import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenOptionsInputs extends GuiComponent {
	private ScreenOption screenOption;
	private MenuGame menuGame;

	protected ScreenOptionsInputs(ScreenOption screenOption, MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOption = screenOption;

		createTitleText("Input Options");

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
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
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
