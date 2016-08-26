package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.visual.*;
import game.uis.screens.*;

import java.util.*;

public class MenuStart extends GuiComponent {
	private MainMenu superMenu;
	private MainSlider mainSlider;

	private Text titleText;
	private SinWaveDriver titleColourX;
	private SinWaveDriver titleColourY;

	private ScreenPlay screenPlay;
	private ScreenOptions screenOptions;
	private ScreenAbout screenAbout;

	protected MenuStart(MainMenu superMenu, MainSlider mainSlider) {
		this.superMenu = superMenu;
		this.mainSlider = mainSlider;

		this.screenPlay = new ScreenPlay(mainSlider);
		this.screenOptions = new ScreenOptions(mainSlider);
		this.screenAbout = new ScreenAbout(mainSlider);

		titleText = Text.newText("4Space").setFontSize(MainSlider.MAIN_TITLE_FONT_SIZE * 1.25f).create();
		titleText.setColour(MainSlider.TEXT_COLOUR);
		titleText.setBorderColour(MainSlider.TEXT_COLOUR.r, MainSlider.TEXT_COLOUR.g, MainSlider.TEXT_COLOUR.b);
		titleText.setGlowing(new SinWaveDriver(0.075f, 0.100f, 2.320f));
		addText(titleText, MainSlider.BUTTONS_X_POS, -0.30f, 1.0f);
		titleColourX = new SinWaveDriver(0.0f, 1.0f, 40.0f);
		titleColourY = new SinWaveDriver(0.0f, 1.0f, 20.0f);

		float currentY = 1.0f + MainSlider.BUTTONS_Y_SEPARATION;
		createQuitButton(currentY -= MainSlider.BUTTONS_Y_SEPARATION);
		currentY -= MainSlider.BUTTONS_Y_SEPARATION * MainSlider.BUTTONS_Y_SEPARATION;

		createModsButton(currentY -= MainSlider.BUTTONS_Y_SEPARATION);
		createAboutButton(currentY -= MainSlider.BUTTONS_Y_SEPARATION);
		createOptionsButton(currentY -= MainSlider.BUTTONS_Y_SEPARATION);
		createPlayButton(currentY -= MainSlider.BUTTONS_Y_SEPARATION);

		super.show(false);
	}

	private void createPlayButton(float yPos) {
		GuiTextButton button = createButton("Play", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenPlay, true));
		button.addRightListener(null);
	}

	private void createOptionsButton(float yPos) {
		GuiTextButton button = createButton("Options", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createAboutButton(float yPos) {
		GuiTextButton button = createButton("About", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenAbout, true));
		button.addRightListener(null);
	}

	private void createModsButton(float yPos) {
		GuiTextButton button = createButton("Mods", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		//	button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenMods, true));
		button.addRightListener(null);
	}

	private void createQuitButton(float yPos) {
		GuiTextButton button = createButton("Quit", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(FlounderEngine::requestClose);
		button.addRightListener(null);
	}

	public static GuiTextButton createButton(String textString, float xPos, float yPos, float xBut, float yBut, float fontSize, GuiComponent component) {
		Text text = Text.newText(textString).setFontSize(fontSize).create();
		text.setColour(MainSlider.TEXT_COLOUR);
		text.setBorderColour(0.15f, 0.15f, 0.15f);
		text.setBorder(new ConstantDriver(0.04f));
		GuiTextButton button = new GuiTextButton(text);
		button.setSounds(MainSlider.SOUND_MOUSE_HOVER, MainSlider.SOUND_MOUSE_LEFT, MainSlider.SOUND_MOUSE_RIGHT);
		component.addComponent(button, xPos, yPos, xBut, yBut);
		return button;
	}

	public static Text createTitleText(String title, GuiComponent component) {
		Text titleText = Text.newText(title).setFontSize(MainSlider.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(0.15f, 0.15f, 0.15f);
		titleText.setBorderColour(MainSlider.TEXT_COLOUR);
		titleText.setBorder(new ConstantDriver(0.04f));
		component.addText(titleText, MainSlider.BUTTONS_X_POS, -0.30f, 1.0f);
		return titleText;
	}

	public MainMenu getSuperMenu() {
		return superMenu;
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
