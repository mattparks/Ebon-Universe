package game.uis;

import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.sounds.*;
import flounder.visual.*;
import game.uis.screens.*;

import java.util.*;

public class MainMenuContent extends GuiComponent {
	public static final float FONT_SIZE = 1.625f;

	public static final float BUTTONS_X_POS = -0.18f;
	public static final float BUTTONS_X_WIDTH = 1.0f - 0.25f * 2.0f;
	public static final float BUTTONS_Y_SEPARATION = 0.18f;
	public static final float BUTTONS_Y_SIZE = 0.2f;

	public static final Sound SOUND_MOUSE_HOVER = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button1.wav"), 0.8f);
	public static final Sound SOUND_MOUSE_LEFT = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button2.wav"), 0.8f);
	public static final Sound SOUND_MOUSE_RIGHT = Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "button3.wav"), 0.8f);

	private MainMenuSlider mainMenuSlider;
	private MainMenu superMenu;

	private Text titleText;
	private SinWaveDriver titleColourX;
	private SinWaveDriver titleColourY;

	private ScreenOptions screenOptions;
	private ScreenAbout screenAbout;

	protected MainMenuContent(MainMenu superMenu, MainMenuSlider mainMenuSlider) {
		this.mainMenuSlider = mainMenuSlider;
		this.superMenu = superMenu;

		this.screenOptions = new ScreenOptions(mainMenuSlider);
		this.screenAbout = new ScreenAbout(mainMenuSlider);

		titleText = Text.newText("4Space").setFontSize(MainMenuSlider.MAIN_TITLE_FONT_SIZE * 1.25f).create();
		titleText.setColour(MainMenuSlider.TEXT_COLOUR);
		titleText.setBorderColour(MainMenuSlider.TEXT_COLOUR.r, MainMenuSlider.TEXT_COLOUR.g, MainMenuSlider.TEXT_COLOUR.b);
		titleText.setGlowing(new SinWaveDriver(0.075f, 0.100f, 2.320f));
		addText(titleText, BUTTONS_X_POS, -0.30f, 1.0f);
		titleColourX = new SinWaveDriver(0.0f, 1.0f, 40.0f);
		titleColourY = new SinWaveDriver(0.0f, 1.0f, 20.0f);

		float currentY = 1.0f + BUTTONS_Y_SEPARATION;
		createQuitButton(currentY -= BUTTONS_Y_SEPARATION);
		currentY -= BUTTONS_Y_SEPARATION * BUTTONS_Y_SEPARATION;

		createModsButton(currentY -= BUTTONS_Y_SEPARATION);
		createAboutButton(currentY -= BUTTONS_Y_SEPARATION);
		createOptionsButton(currentY -= BUTTONS_Y_SEPARATION);
		createPlayButton(currentY -= BUTTONS_Y_SEPARATION);
	}

	private void createPlayButton(float yPos) {
		GuiTextButton button = createButton("Play", BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE, FONT_SIZE, this);
		button.addLeftListener(() -> superMenu.display(false));
		button.addRightListener(null);
	}

	private void createOptionsButton(float yPos) {
		GuiTextButton button = createButton("Options", BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE, FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createAboutButton(float yPos) {
		GuiTextButton button = createButton("About", BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE, FONT_SIZE, this);
		button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenAbout, true));
		button.addRightListener(null);
	}

	private void createModsButton(float yPos) {
		GuiTextButton button = createButton("Mods", BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE, FONT_SIZE, this);
		//	button.addLeftListener(() -> mainMenuSlider.setNewSecondaryScreen(screenMods, true));
		button.addRightListener(null);
	}

	private void createQuitButton(float yPos) {
		GuiTextButton button = createButton("Quit", BUTTONS_X_POS, yPos, BUTTONS_X_WIDTH, BUTTONS_Y_SIZE, FONT_SIZE, this);
		button.addLeftListener(FlounderEngine::requestClose);
		button.addRightListener(null);
	}

	public static GuiTextButton createButton(String textString, float xPos, float yPos, float xBut, float yBut, float fontSize, GuiComponent component) {
		Text text = Text.newText(textString).setFontSize(fontSize).create();
		text.setColour(MainMenuSlider.TEXT_COLOUR);
		text.setBorderColour(0.15f, 0.15f, 0.15f);
		text.setBorder(new ConstantDriver(0.04f));
		GuiTextButton button = new GuiTextButton(text);
		button.setSounds(SOUND_MOUSE_HOVER, SOUND_MOUSE_LEFT, SOUND_MOUSE_RIGHT);
		component.addComponent(button, xPos, yPos, xBut, yBut);
		return button;
	}

	public static Text createTitleText(String title, GuiComponent component) {
		Text titleText = Text.newText(title).setFontSize(MainMenuSlider.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MainMenuSlider.TEXT_COLOUR);
		titleText.setBorderColour(0.15f, 0.15f, 0.15f);
		titleText.setBorder(new ConstantDriver(0.04f));
		component.addText(titleText, MainMenuContent.BUTTONS_X_POS, -0.30f, 1.0f);
		return titleText;
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
