package game.uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenQuit extends GuiComponent {
	private final MenuGame menuGame;

	protected ScreenQuit(final MenuGame menuGame) {
		this.menuGame = menuGame;

		createQuitOption(ScreenOption.BUTTONS_X_CENTER_POS, 0.3f);
		createBackOption(ScreenOption.BUTTONS_X_CENTER_POS, 1.0f);

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
		final Text text = Text.newText("Quit Game").center().setFontSize(ScreenOption.FONT_SIZE * 1.25f).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final GuiListener guiListener = FlounderDevices.getDisplay()::requestClose;

		button.addLeftListener(guiListener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final GuiListener guiListener = menuGame::closeSecondaryScreen;

		button.addLeftListener(guiListener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
