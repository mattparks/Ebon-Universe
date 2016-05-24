package game.uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenQuit extends GuiComponent {
	private final MenuGame menuGame;

	protected ScreenQuit(final MenuGame menu) {
		menuGame = menu;
		createQuitOption(ScreenOption.BUTTONS_X_CENTER_POS, 0.3f);
		createBackOption(ScreenOption.BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createQuitOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Quit Game").center().setFontSize(ScreenOption.FONT_SIZE * 1.25f).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = FlounderDevices.getDisplay()::requestClose;

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = menuGame::closeSecondaryScreen;

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
