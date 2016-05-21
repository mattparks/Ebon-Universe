package game.uis;

import flounder.engine.profiling.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class ScreenOptionsDeveloper extends GuiComponent {
	private final MenuGame menuGame;
	private final ScreenOption screenOption;

	protected ScreenOptionsDeveloper(final ScreenOption screenOption, final MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOption = screenOption;

		createProfilerToggleOption(ScreenOption.BUTTONS_X_CENTER_POS, 0.0f);

		createBackOption(ScreenOption.BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createProfilerToggleOption(final float xPos, final float yPos) {
		final String profilerText = "Engine Profiler: ";
		final Text text = Text.newText(profilerText + (FlounderProfiler.isOpen() ? "Enabled" : "Disabled")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> {
			FlounderProfiler.toggle(!FlounderProfiler.isOpen());
			text.setText(profilerText + (FlounderProfiler.isOpen() ? "Enabled" : "Disabled"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> menuGame.setNewSecondaryScreen(screenOption);

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
