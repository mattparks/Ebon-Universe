package game.uis;

import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.sounds.*;
import flounder.visual.*;

import java.util.*;

public class MenuMain extends GuiComponent {
	public static final float FONT_SIZE = 2.0f;

	public static final float BUTTONS_CENTER_X_POS = 0.0f;
	public static final float BUTTONS_CENTER_X_WIDTH = 1.0f - BUTTONS_CENTER_X_POS * 2.0f;

	public static final float BUTTONS_X_LEFT_POS = -0.2f;
	public static final float BUTTONS_X_CENTER_POS = 0.2f;
	public static final float BUTTONS_X_RIGHT_POS = 0.6f;
	public static final float BUTTONS_Y_SIZE = 0.2f;
	public static final float BUTTONS_X_WIDTH = 0.6f;

	public static final float TEXT_TITLE_Y_POS = -0.3f;

	public static final Sound SOUND_MOUSE_HOVER = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button1.wav"), 0.8f);
	public static final Sound SOUND_MOUSE_LEFT = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button2.wav"), 0.8f);
	public static final Sound SOUND_MOUSE_RIGHT = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button3.wav"), 0.8f);

	private MenuGame menuGame;
	private MenuGameBackground superMenu;

	private Text titleText;
	private SinWaveDriver titleColourX;
	private SinWaveDriver titleColourY;

	private ScreenOption screenOption;
	private ScreenQuit screenQuit;

	protected MenuMain(MenuGameBackground superMenu, MenuGame menuGame) {
		this.menuGame = menuGame;
		this.superMenu = superMenu;

		this.screenOption = new ScreenOption(menuGame);
		this.screenQuit = new ScreenQuit(menuGame);

		titleText = Text.newText("Flounder Engine").center().setFontSize(MenuGame.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MenuGame.TEXT_COLOUR);
		titleText.setBorderColour(MenuGame.TEXT_COLOUR.r, MenuGame.TEXT_COLOUR.g, MenuGame.TEXT_COLOUR.b);
		titleText.setGlowing(new SinWaveDriver(0.075f, 0.150f, 2.320f));
		addText(titleText, 0.0f, -0.30f, 1.0f);
		titleColourX = new SinWaveDriver(0.0f, 1.0f, 40.0f);
		titleColourY = new SinWaveDriver(0.0f, 1.0f, 20.0f);

		createPlayButton(0.3f);
		createOptionsButton(0.6f);
		createQuitButton(0.9f);
	}

	private void createPlayButton(float yPos) {
		GuiTextButton button = createButton("Play", BUTTONS_CENTER_X_POS, yPos, BUTTONS_CENTER_X_WIDTH, BUTTONS_Y_SIZE, FONT_SIZE, this);
		button.addLeftListener(() -> superMenu.display(false));
		button.addRightListener(null);
	}

	private void createOptionsButton(float yPos) {
		GuiTextButton button = createButton("Options", BUTTONS_CENTER_X_POS, yPos, BUTTONS_CENTER_X_WIDTH, BUTTONS_Y_SIZE, FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenOption, true));
		button.addRightListener(null);
	}

	private void createQuitButton(float yPos) {
		GuiTextButton button = createButton("Quit", BUTTONS_CENTER_X_POS, yPos, BUTTONS_CENTER_X_WIDTH, BUTTONS_Y_SIZE, FONT_SIZE, this);
		button.addLeftListener(() -> menuGame.setNewSecondaryScreen(screenQuit, true));
		button.addRightListener(null);
	}

	public static GuiTextButton createButton(String textString, float xPos, float yPos, float xBut, float yBut, float fontSize, GuiComponent component) {
		Text text = Text.newText(textString).center().setFontSize(fontSize).create();
		text.setColour(MenuGame.TEXT_COLOUR);
		text.setBorderColour(0.15f, 0.15f, 0.15f);
		text.setBorder(new ConstantDriver(0.04f));
		GuiTextButton button = new GuiTextButton(text);
		button.setSounds(SOUND_MOUSE_HOVER, SOUND_MOUSE_LEFT, SOUND_MOUSE_RIGHT);
		component.addComponent(button, xPos, yPos, xBut, yBut);
		return button;
	}

	@Override
	protected void updateSelf() {
		titleText.setColour(titleColourX.update(FlounderEngine.getDelta()), titleColourY.update(FlounderEngine.getDelta()), 0.3f);
		titleText.setBorderColour(titleText.getColour().r, titleText.getColour().g, titleText.getColour().b);
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
