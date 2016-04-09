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

		createCursorInactiveOption(OptionScreen.BUTTONS_X_LEFT_POS + 0.3f, 0.0f);
		createCursorActiveOption(OptionScreen.BUTTONS_X_LEFT_POS + 0.3f, 0.2f);

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
		final Text text = Text.newText(mouseText).center().setFontSize(OptionScreen.FONT_SIZE * 0.5f).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiSlider slider = new GuiSlider();
		slider.addText(text);
		slider.setValue((MainGuis.getOverlayCursor().getInactiveColour().r / 3) + (MainGuis.getOverlayCursor().getInactiveColour().g / 3) + (MainGuis.getOverlayCursor().getInactiveColour().b / 3));

		final Listener listenerLeft = () -> {
			float r = MainGuis.getOverlayCursor().getInactiveColour().r;
			float g = MainGuis.getOverlayCursor().getInactiveColour().g;
			float b = MainGuis.getOverlayCursor().getInactiveColour().b;

			if (r == 1.0f) {
				r = 0;
				g = 1;
				b = 0;
			} else if (g == 1.0f) {
				r = 0;
				g = 0;
				b = 1;
			} else if (b == 1.0f) {
				r = 1;
				g = 0;
				b = 0;
			}

			MainGuis.getOverlayCursor().setInactiveColour(r, g, b);
			slider.setFullColour(MainGuis.getOverlayCursor().getInactiveColour());
			slider.setValue((r / 3) + (g / 3) + (b / 3));
		};

		slider.addLeftListener(listenerLeft);
		addComponent(slider, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
	}

	private void createCursorActiveOption(final float xPos, final float yPos) {
		final String mouseText = "Cursor Active: ";
		final Text text = Text.newText(mouseText).center().setFontSize(OptionScreen.FONT_SIZE * 0.5f).create();
		text.setColour(GameMenu.TEXT_COLOUR);
		final GuiSlider slider = new GuiSlider();
		slider.addText(text);
		slider.setValue((MainGuis.getOverlayCursor().getActiveColour().r / 3) + (MainGuis.getOverlayCursor().getActiveColour().g / 3) + (MainGuis.getOverlayCursor().getActiveColour().b / 3));

		final Listener listenerLeft = () -> {
			float r = MainGuis.getOverlayCursor().getActiveColour().r;
			float g = MainGuis.getOverlayCursor().getActiveColour().g;
			float b = MainGuis.getOverlayCursor().getActiveColour().b;

			if (r == 1.0f) {
				r = 0;
				g = 1;
				b = 0;
			} else if (g == 1.0f) {
				r = 0;
				g = 0;
				b = 1;
			} else if (b == 1.0f) {
				r = 1;
				g = 0;
				b = 0;
			}

			MainGuis.getOverlayCursor().setActiveColour(r, g, b);
			slider.setFullColour(MainGuis.getOverlayCursor().getActiveColour());
			slider.setValue((r / 3) + (g / 3) + (b / 3));
		};

		slider.addLeftListener(listenerLeft);
		addComponent(slider, xPos, yPos, OptionScreen.BUTTONS_X_WIDTH, OptionScreen.BUTTONS_Y_SIZE);
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
