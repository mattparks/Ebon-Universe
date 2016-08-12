package game.uis;

import flounder.engine.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.visual.*;

import java.util.*;

public class ScreenAbout extends GuiComponent {
	private MenuGame menuGame;

	protected ScreenAbout(MenuGame menuGame) {
		this.menuGame = menuGame;

		createTitleText("About");

		createBackOption(MenuMain.BUTTONS_X_CENTRE_POS, 1.0f);

		FlounderEngine.getEvents().addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return MenuGame.BACK_KEY.wasDown();
			}

			@Override
			public void onEvent() {
				menuGame.closeSecondaryScreen();
			}
		});
	}

	private void createTitleText(String title) {
		Text titleText = Text.newText(title).centre().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		titleText.setBorderColour(0.15f, 0.15f, 0.15f);
		titleText.setBorder(new ConstantDriver(0.04f));
		addText(titleText, -0.5f, MenuMain.TEXT_TITLE_Y_POS, 2.0f);
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(menuGame::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
