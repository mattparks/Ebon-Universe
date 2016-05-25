package game.uis;

import flounder.fonts.*;
import flounder.guis.*;
import game.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class ScreenOptionsInputs extends GuiComponent {
	private final ScreenOption screenOption;
	private MenuGame menuGame;

	protected ScreenOptionsInputs(final ScreenOption screenOption, final MenuGame menuGame) {
		this.menuGame = menuGame;
		this.screenOption = screenOption;

		createMouseMoveOption(ScreenOption.BUTTONS_X_CENTER_POS, 0.0f);

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

	private void createMouseMoveOption(final float xPos, final float yPos) {
		final String mouseText = "Mouse Move: ";
		final Text text = Text.newText(mouseText + (MainCamera.toggleMouseMoveKey == GLFW_MOUSE_BUTTON_LEFT ? "Left Key" : MainCamera.toggleMouseMoveKey == GLFW_MOUSE_BUTTON_RIGHT ? "Right Key" : "Center Key")).center().setFontSize(ScreenOption.FONT_SIZE).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final GuiListener guiListener = () -> {
			MainCamera.toggleMouseMoveKey++;

			if (MainCamera.toggleMouseMoveKey > GLFW_MOUSE_BUTTON_MIDDLE) {
				MainCamera.toggleMouseMoveKey = GLFW_MOUSE_BUTTON_LEFT;
			}

			text.setText(mouseText + (MainCamera.toggleMouseMoveKey == GLFW_MOUSE_BUTTON_LEFT ? "Left Key" : MainCamera.toggleMouseMoveKey == GLFW_MOUSE_BUTTON_RIGHT ? "Right Key" : "Center Key"));
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
