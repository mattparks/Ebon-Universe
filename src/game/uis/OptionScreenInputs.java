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

		createCursorInactiveOption(OptionScreen.BUTTONS_X_LEFT_POS, 0.0f);
		createCursorActiveOption(OptionScreen.BUTTONS_X_LEFT_POS, 0.2f);

		createMouseMoveOption(OptionScreen.BUTTONS_X_RIGHT_POS, 0.0f);

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

	private void createCursorInactiveOption(final float xPos, final float yPos) {
		final String mouseText = "Cursor Inactive: ";
		final Text text = Text.newText(mouseText + (MainGuis.getOverlayCursor().getInactiveRow() == 0 ? "Blue" : MainGuis.getOverlayCursor().getInactiveRow() == 1 ? "Green" : MainGuis.getOverlayCursor().getInactiveRow() == 2 ? "Red" : "Purple")).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listenerLeft = () -> {
			int activeRow = MainGuis.getOverlayCursor().getInactiveRow() + 1;

			if (activeRow >= MainGuis.getOverlayCursor().getTotalRows() * 2) {
				activeRow = 0;
			}

			MainGuis.getOverlayCursor().setInactiveRow(activeRow);
			text.setText(mouseText + (MainGuis.getOverlayCursor().getInactiveRow() == 0 ? "Blue" : MainGuis.getOverlayCursor().getInactiveRow() == 1 ? "Green" : MainGuis.getOverlayCursor().getInactiveRow() == 2 ? "Red" : "Purple"));
		};

		button.addLeftListener(listenerLeft);
		addComponent(button, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	private void createCursorActiveOption(final float xPos, final float yPos) {
		final String mouseText = "Cursor Active: ";
		final Text text = Text.newText(mouseText + (MainGuis.getOverlayCursor().getActiveRow() == 0 ? "Blue" : MainGuis.getOverlayCursor().getActiveRow() == 1 ? "Green" : MainGuis.getOverlayCursor().getActiveRow() == 2 ? "Red" : "Purple")).center().setFontSize(OptionScreen.FONT_SIZE).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiTextButton button = new GuiTextButton(text);

		final Listener listenerLeft = () -> {
			int activeRow = MainGuis.getOverlayCursor().getActiveRow() + 1;

			if (activeRow >= MainGuis.getOverlayCursor().getTotalRows() * 2) {
				activeRow = 0;
			}

			MainGuis.getOverlayCursor().setActiveRow(activeRow);
			text.setText(mouseText + (MainGuis.getOverlayCursor().getActiveRow() == 0 ? "Blue" : MainGuis.getOverlayCursor().getActiveRow() == 1 ? "Green" : MainGuis.getOverlayCursor().getActiveRow() == 2 ? "Red" : "Purple"));
		};

		button.addLeftListener(listenerLeft);
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
