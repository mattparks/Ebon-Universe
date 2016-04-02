package uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class QuitScreen extends GuiComponent {
	private static final float FONT_SIZE = 2;
	private static final float BUTTONS_X_POS = 0.3f;
	private static final float BUTTONS_Y_SIZE = 0.2f;
	private static final float BUTTONS_X_WIDTH = 1f - BUTTONS_X_POS * 2f;

	private final GameMenu gameMenu;

	protected QuitScreen(final GameMenu menu) {
		gameMenu = menu;
		createQuitOption(0.3f);
		createBackOption(0.9f);
	}

	private void createQuitOption(final float yPos) {
		final Text text = Text.newText("Quit Game").center().setFontSize(FONT_SIZE * 1.25f).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = ManagerDevices.getDisplay()::requestClose;

		button.addLeftListener(listener);
		addComponent(button, BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = gameMenu::closeSecondaryScreen;

		button.addLeftListener(listener);
		addComponent(button, BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
