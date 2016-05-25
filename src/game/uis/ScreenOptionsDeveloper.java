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

		super.addActionListener(new GuiListenerAdvanced() {
			@Override
			public boolean hasOccurred() {
				return MenuGame.BACK_KEY.wasDown();
			}

			@Override
			public void run() {
				menuGame.setNewSecondaryScreen(screenOption, false);
			}
		});
	}

	private void createProfilerToggleOption(final float xPos, final float yPos) {
		final String profilerText = "Engine Profiler: ";
		final Text text = Text.newText(profilerText + (FlounderProfiler.isOpen() ? "Enabled" : "Disabled")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final GuiListener guiListener = () -> {
			FlounderProfiler.toggle(!FlounderProfiler.isOpen());
			text.setText(profilerText + (FlounderProfiler.isOpen() ? "Enabled" : "Disabled"));
		};

		button.addLeftListener(guiListener);
		addComponent(button, xPos, yPos, ScreenOption.BUTTONS_X_WIDTH, ScreenOption.BUTTONS_Y_SIZE);
	}

	private void createBackOption(final float xPos, final float yPos) {
		final Text text = Text.newText("Back").center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final GuiListener guiListener = () -> menuGame.setNewSecondaryScreen(screenOption, false);

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
