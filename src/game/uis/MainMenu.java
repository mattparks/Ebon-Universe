package game.uis;

import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class MainMenu extends GuiComponent {
	private static final float FONT_SIZE = 2;
	//	private static final float SLIDERS_X_POS = 0.5f;
	private static final float BUTTONS_X_POS = 0.3f;
	private static final float BUTTONS_Y_SIZE = 0.2f;
	private static final float BUTTONS_X_WIDTH = 1f - BUTTONS_X_POS * 2f;

	private final GameMenu gameMenu;
	private final GameMenuBackground superMenu;

	protected MainMenu(final GameMenuBackground superMenu, final GameMenu menu) {
		gameMenu = menu;
		this.superMenu = superMenu;
		//createSliderTest(0.1f);
		createPlayButton(0.3f);
		createOptionsButton(0.5f);
		createQuitButton(0.7f);
	}

/*	private void createSliderTest(final float yPos) {
		GuiSlider slider = new GuiSlider(new Colour(0.1f, 0.8f, 0.1f));
		Text text = Text.newText("Hello Slider!").center().setFontSize(FONT_SIZE / 2.0f).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		slider.addText(text);

		final Listener leftListener = () -> {
			float value = slider.getValue() + 0.049f;
			value = (value > 1.0f) ? 0.0f : value;
			slider.setValue(value);
		};

		slider.addLeftListener(leftListener);
		addComponent(slider, SLIDERS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE);
	}*/

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
