package game.uis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class QuitScreen extends GuiComponent {
	private final GameMenu gameMenu;

	protected QuitScreen(final GameMenu menu) {
		gameMenu = menu;
		createQuitOption(OptionScreen.BUTTONS_X_CENTER_POS, 0.3f);
		createBackOption(OptionScreen.BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createQuitOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Quit Game").center().setFontSize(OptionScreen.FONT_SIZE * 1.25f).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = ManagerDevices.getDisplay()::requestClose;

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = gameMenu::closeSecondaryScreen;

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
