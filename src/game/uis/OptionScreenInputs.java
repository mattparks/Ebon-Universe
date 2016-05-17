package game.uis;

import flounder.fonts.*;
import flounder.guis.*;
import game.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class OptionScreenInputs extends GuiComponent {
	private GameMenu gameMenu;

	protected OptionScreenInputs(final GameMenu menu) {
		gameMenu = menu;

		createMouseMoveOption(OptionScreen.BUTTONS_X_CENTER_POS, 0.0f);

		createBackOption(OptionScreen.BUTTONS_X_CENTER_POS, 1.0f);
	}

	private void createMouseMoveOption(final float xPos, final float yPos) {
		final String mouseText = "Mouse Move: ";
		final Text text = Text.newText(mouseText + (MainCamera.toggleMouseMoveKey == GLFW_MOUSE_BUTTON_LEFT ? "Left Key" : MainCamera.toggleMouseMoveKey == GLFW_MOUSE_BUTTON_RIGHT ? "Right Key" : "Center Key")).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listener = () -> {
			MainCamera.toggleMouseMoveKey++;

			if (MainCamera.toggleMouseMoveKey > GLFW_MOUSE_BUTTON_MIDDLE) {
				MainCamera.toggleMouseMoveKey = GLFW_MOUSE_BUTTON_LEFT;
			}

			text.setText(mouseText + (MainCamera.toggleMouseMoveKey == GLFW_MOUSE_BUTTON_LEFT ? "Left Key" : MainCamera.toggleMouseMoveKey == GLFW_MOUSE_BUTTON_RIGHT ? "Right Key" : "Center Key"));
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
