package uis;

import flounder.fonts.*;
import flounder.guis.*;

import java.util.*;

public class OptionScreenInputs extends GuiComponent {
	private GameMenu gameMenu;

	protected OptionScreenInputs(final GameMenu menu) {
		gameMenu = menu;

		createMouseMoveOption(OptionScreen.BUTTONS_X_CENTER_POS, 0.0f);

		createBackOption(OptionScreen.BUTTONS_X_CENTER_POS, 0.9f);
	}

	private void createMouseMoveOption(final float xPos, final float yPos) {
	/*	final String mouseText = "Mouse Move: ";
		final Text text = Text.newText(mouseText + (Camera3D.toggleMouseMoveKey == DeviceMouse.MOUSE_BUTTON_LEFT ? "Left Key" : Camera3D.toggleMouseMoveKey == DeviceMouse.MOUSE_BUTTON_RIGHT ? "Right Key" : "Center Key")).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		GuiTextButton button = new GuiTextButton(text);

		Listener listener = () -> {
			Camera3D.toggleMouseMoveKey++;

			if (Camera3D.toggleMouseMoveKey > DeviceMouse.MOUSE_BUTTON_MIDDLE) {
				Camera3D.toggleMouseMoveKey = DeviceMouse.MOUSE_BUTTON_LEFT;
			}

			text.setText(mouseText + (Camera3D.toggleMouseMoveKey == DeviceMouse.MOUSE_BUTTON_LEFT ? "Left Key" : Camera3D.toggleMouseMoveKey == DeviceMouse.MOUSE_BUTTON_RIGHT ? "Right Key" : "Center Key"));
		};

		button.addLeftListener(listener);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);*/
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
