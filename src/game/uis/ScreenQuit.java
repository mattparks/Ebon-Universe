package game.uis;

import flounder.engine.*;
import flounder.events.*;
import flounder.guis.*;

import java.util.*;

public class ScreenQuit extends GuiComponent {
	private MenuGame menuGame;

	protected ScreenQuit(MenuGame menuGame) {
		this.menuGame = menuGame;

		createQuitOption(MenuMain.BUTTONS_CENTRE_X_POS, 0.3f);
		createBackOption(MenuMain.BUTTONS_CENTRE_X_POS, 1.0f);

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

	private void createQuitOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Quit Game", xPos, yPos, MenuMain.BUTTONS_CENTRE_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE * 1.25f, this);
		button.addLeftListener(FlounderEngine::requestClose);
	}

	private void createBackOption(float xPos, float yPos) {
		GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_CENTRE_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(menuGame::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
