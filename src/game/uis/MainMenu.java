package game.uis;

import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class MainMenu extends GuiComponent {
	private static final float FONT_SIZE = 2.0f;
	private static final float BUTTONS_X_POS = 0.3f;
	private static final float BUTTONS_Y_SIZE = 0.2f;
	private static final float BUTTONS_X_WIDTH = 1.0f - BUTTONS_X_POS * 2.0f;

	private final GameMenu gameMenu;
	private final GameMenuBackground superMenu;

	private final OptionScreen optionScreen;
	private final QuitScreen quitScreen;

	protected MainMenu(final GameMenuBackground superMenu, final GameMenu gameMenu) {
		this.gameMenu = gameMenu;
		this.superMenu = superMenu;

		this.optionScreen = new OptionScreen(gameMenu);
		this.quitScreen = new QuitScreen(gameMenu);

		createPlayButton(0.3f);
		createOptionsButton(0.5f);
		createQuitButton(0.7f);
	}

	private void createPlayButton(final float yPos) {
		final Listener listener = () -> superMenu.display(false);
		createButton("Play", listener, yPos);
	}

	private void createButton(final String textString, final Listener listener, final float yPos) {
		final Text text = Text.newText(textString).center().setFontSize(FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);
		button.addLeftListener(listener);
		addComponent(button, BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createOptionsButton(final float yPos) {
		final Listener listener = () -> gameMenu.setNewSecondaryScreen(optionScreen);
		createButton("Options", listener, yPos);
	}

	private void createQuitButton(final float yPos) {
		final Listener listener = () -> gameMenu.setNewSecondaryScreen(quitScreen);
		createButton("Quit", listener, yPos);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
