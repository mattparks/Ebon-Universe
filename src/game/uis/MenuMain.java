package game.uis;

import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class MenuMain extends GuiComponent {
	private static final float FONT_SIZE = 2.0f;
	private static final float BUTTONS_X_POS = 0.2f;
	private static final float BUTTONS_Y_SIZE = 0.2f;
	private static final float BUTTONS_X_WIDTH = 1.0f - BUTTONS_X_POS * 2.0f;

	private final MenuGame menuGame;
	private final MenuGameBackground superMenu;

	private final ScreenOption screenOption;
	private final ScreenQuit screenQuit;

	protected MenuMain(final MenuGameBackground superMenu, final MenuGame menuGame) {
		this.menuGame = menuGame;
		this.superMenu = superMenu;

		this.screenOption = new ScreenOption(menuGame);
		this.screenQuit = new ScreenQuit(menuGame);

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
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);
		button.addLeftListener(listener);
		addComponent(button, BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createOptionsButton(final float yPos) {
		final Listener listener = () -> menuGame.setNewSecondaryScreen(screenOption, true);
		createButton("Options", listener, yPos);
	}

	private void createQuitButton(final float yPos) {
		final Listener listener = () -> menuGame.setNewSecondaryScreen(screenQuit, true);
		createButton("Quit", listener, yPos);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
