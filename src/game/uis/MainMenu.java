package game.uis;

import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class MainMenu extends GuiComponent {
	private static final float FONT_SIZE = 2;
	private static final float BUTTONS_X_POS = 0.3f;
	private static final float BUTTONS_Y_SIZE = 0.2f;
	private static final float BUTTONS_X_WIDTH = 1f - BUTTONS_X_POS * 2f;

	private final GameMenu gameMenu;
	private final GameMenuBackground superMenu;

	protected MainMenu(final GameMenuBackground superMenu, final GameMenu menu) {
		gameMenu = menu;
		this.superMenu = superMenu;
		createPlayButton(0.3f);
		createOptionsButton(0.5f);
		createQuitButton(0.7f);
	}

	private void createPlayButton(final float yPos) {
		Listener listener = () -> superMenu.display(false);
		createButton("Play", listener, yPos);
	}

	private void createButton(final String textString, final Listener listener, final float yPos) {
		Text text = Text.newText(textString).center().setFontSize(FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);
		button.addLeftListener(listener);
		addComponent(button, BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}

	private void createOptionsButton(final float yPos) {
		Listener listener = () -> gameMenu.setNewSecondaryScreen(new OptionScreen(gameMenu));
		createButton("Options", listener, yPos);
	}

	private void createQuitButton(final float yPos) {
		Listener listener = () -> gameMenu.setNewSecondaryScreen(new QuitScreen(gameMenu));
		createButton("Quit", listener, yPos);
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
