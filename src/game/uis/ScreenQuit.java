package game.uis;

import flounder.devices.*;
import flounder.guis.*;

import java.util.*;

public class ScreenQuit extends GuiComponent {
	private final MenuGame menuGame;

	protected ScreenQuit(final MenuGame menuGame) {
		this.menuGame = menuGame;

		createQuitOption(MenuMain.BUTTONS_CENTER_X_POS, 0.3f);
		createBackOption(MenuMain.BUTTONS_CENTER_X_POS, 1.0f);

		super.addActionListener(new GuiListenerAdvanced() {
			@Override
			public boolean hasOccurred() {
				return MenuGame.BACK_KEY.wasDown();
			}

			@Override
			public void run() {
				menuGame.closeSecondaryScreen();
			}
		});
	}

	private void createQuitOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Quit Game", xPos, yPos, MenuMain.BUTTONS_CENTER_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE * 1.25f, this);
		button.addLeftListener(FlounderDevices.getDisplay()::requestClose);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final GuiTextButton button = MenuMain.createButton("Back", xPos, yPos, MenuMain.BUTTONS_CENTER_X_WIDTH, MenuMain.BUTTONS_Y_SIZE, MenuMain.FONT_SIZE, this);
		button.addLeftListener(menuGame::closeSecondaryScreen);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
