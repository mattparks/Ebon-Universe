package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.textures.*;
import flounder.visual.*;
import game.uis.screens.*;

import java.util.*;

public class MenuStart extends GuiComponent {
	private MainMenu superMenu;
	private MainSlider mainSlider;

	private GuiTexture[] slideshow;
	private GuiTexture slideoverlay;
	private SlideDriver slideshowDriver;
	private static final float SLIDESHOW_ASPECT = 1600.0f / 1200.0f;
	private static final float SLIDE_SPEED = 4.75f;

	private Text titleText;

	private ScreenPlay screenPlay;
	private ScreenOptions screenOptions;
	private ScreenAbout screenAbout;
	private ScreenMods screenMods;

	protected MenuStart(MainMenu superMenu, MainSlider mainSlider) {
		this.superMenu = superMenu;
		this.mainSlider = mainSlider;

		this.screenPlay = new ScreenPlay(mainSlider);
		this.screenOptions = new ScreenOptions(mainSlider);
		this.screenAbout = new ScreenAbout(mainSlider);
		this.screenMods = new ScreenMods(mainSlider);

		titleText = Text.newText(FlounderEngine.getDevices().getDisplay().getTitle(), TextAlign.LEFT).setFontSize(MainSlider.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MainSlider.TEXT_COLOUR);
		titleText.setBorderColour(MainSlider.TEXT_COLOUR.r, MainSlider.TEXT_COLOUR.g, MainSlider.TEXT_COLOUR.b);
		titleText.setGlowing(new SinWaveDriver(0.075f, 0.100f, 2.320f));
		addText(titleText, MainSlider.BUTTONS_X_MAGIN_LEFT, -0.23f, 1.0f);

		float currentY = 1.0f + MainSlider.BUTTONS_Y_SEPARATION;
		createQuitButton(TextAlign.LEFT, currentY -= MainSlider.BUTTONS_Y_SEPARATION);
		currentY -= MainSlider.BUTTONS_Y_SEPARATION * MainSlider.BUTTONS_Y_SEPARATION;

		createModsButton(TextAlign.LEFT, currentY -= MainSlider.BUTTONS_Y_SEPARATION);
		createAboutButton(TextAlign.LEFT, currentY -= MainSlider.BUTTONS_Y_SEPARATION);
		createOptionsButton(TextAlign.LEFT, currentY -= MainSlider.BUTTONS_Y_SEPARATION);
		createPlayButton(TextAlign.LEFT, currentY -= MainSlider.BUTTONS_Y_SEPARATION);

		super.show(false);
	}

	private void createPlayButton(TextAlign textAlign, float yPos) {
		//	GuiCheckbox checkbox = MainSlider.createCheckbox("Testing", TextAlign.LEFT, yPos - MainSlider.BUTTONS_Y_SEPARATION, false, this);
		GuiTextButton button = MainSlider.createButton("Play", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenPlay, true));
		button.addRightListener(null);
	}

	private void createOptionsButton(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Options", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createAboutButton(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("About", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenAbout, true));
		button.addRightListener(null);
	}

	private void createModsButton(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Mods", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenMods, true));
		button.addRightListener(null);
	}

	private void createQuitButton(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Quit", textAlign, yPos, this);
		button.addLeftListener(FlounderEngine::requestClose);
		button.addRightListener(null);
	}

	public MainMenu getSuperMenu() {
		return superMenu;
	}

	@Override
	protected void updateSelf() {
		titleText.setColour(GuiTextButton.HOVER_COLOUR);
		titleText.setBorderColour(GuiTextButton.HOVER_COLOUR);

		if (slideshow == null && isShown()) {
			slideshow = new GuiTexture[]{
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example1.png")).createInSecondThread()),
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example2.png")).createInSecondThread()),
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example3.png")).createInSecondThread()),
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example4.png")).createInSecondThread()),
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example5.png")).createInSecondThread())
			};
			slideoverlay = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "overlay.png")).nearestFiltering().createInSecondThread());
			slideshowDriver = new SlideDriver(slideshow.length - 1.0f, 0.0f, slideshow.length * SLIDE_SPEED);
		}

		// TODO: Use aspect ratio to create image width.
		float aspectRatio = FlounderEngine.getDevices().getDisplay().getAspectRatio();
		float progression = slideshowDriver.update(FlounderEngine.getDelta());
		float imageWidth = SLIDESHOW_ASPECT / aspectRatio;

		if (progression >= slideshow.length - 1.0f) {
			slideshowDriver = new SlideDriver(slideshow.length - 1.0f, 0.0f, slideshow.length * SLIDE_SPEED);
		} else if (progression == 0.0f) {
			slideshowDriver = new SlideDriver(0.0f, slideshow.length - 1.0f, slideshow.length * SLIDE_SPEED);
		}

		float currentTextureX = progression;

		if (isShown()) {
			for (int i = 0; i < slideshow.length; i++) {
				slideshow[i].setPosition(currentTextureX, 0.0f, imageWidth, 1.0f);
				slideshow[i].update();
				currentTextureX -= imageWidth;
			}
		}

		// slideoverlay.getColourOffset().set(titleText.getColour());
		slideoverlay.setPosition(0.0f, 0.0f, 1.0f, 1.0f);
		slideoverlay.update();
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		for (int i = 0; i < slideshow.length; i++) {
			guiTextures.add(slideshow[i]);
		}

		guiTextures.add(slideoverlay);
	}
}
