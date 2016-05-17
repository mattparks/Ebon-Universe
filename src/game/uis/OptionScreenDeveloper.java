package game.uis;

import flounder.engine.profiling.*;
import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class OptionScreenDeveloper extends GuiComponent {
	private final GameMenu gameMenu;

	protected OptionScreenDeveloper(final GameMenu menu) {
		gameMenu = menu;

		createProfilerToggleOption(OptionScreen.BUTTONS_X_CENTER_POS, 0.0f);

		createBackOption(OptionScreen.BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createProfilerToggleOption(final float xPos, final float yPos) {
		final String profilerText = "Engine Profiler: ";
		final Text text = Text.newText(profilerText + (FlounderProfiler.isOpen() ? "Enabled" : "Disabled")).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> {
			FlounderProfiler.toggle(!FlounderProfiler.isOpen());
			text.setText(profilerText + (FlounderProfiler.isOpen() ? "Enabled" : "Disabled"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> gameMenu.setNewSecondaryScreen(new OptionScreen(gameMenu));

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
