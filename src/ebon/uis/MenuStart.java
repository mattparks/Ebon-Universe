package ebon.uis;

import ebon.uis.screens.*;
import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.textures.*;
import flounder.visual.*;

import java.util.*;

public class MenuStart extends GuiComponent {
	private MasterMenu superMenu;
	private MasterSlider masterSlider;

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

	protected MenuStart(MasterMenu superMenu, MasterSlider masterSlider) {
		this.superMenu = superMenu;
		this.masterSlider = masterSlider;

		this.screenPlay = new ScreenPlay(masterSlider);
		this.screenOptions = new ScreenOptions(masterSlider);
		this.screenAbout = new ScreenAbout(masterSlider);
		this.screenMods = new ScreenMods(masterSlider);

		titleText = Text.newText(FlounderDisplay.getTitle()).setFontSize(MasterSlider.MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(MasterSlider.TEXT_COLOUR);
		titleText.setBorderColour(MasterSlider.TEXT_COLOUR.r, MasterSlider.TEXT_COLOUR.g, MasterSlider.TEXT_COLOUR.b);
		titleText.setGlowing(new SinWaveDriver(0.075f, 0.100f, 2.320f));
		addText(titleText, MasterSlider.BUTTONS_X_MAGIN_LEFT, -0.23f, 1.0f);

		float currentY = 1.0f + MasterSlider.BUTTONS_Y_SEPARATION;
		createQuitButton(GuiAlign.LEFT, currentY -= MasterSlider.BUTTONS_Y_SEPARATION);
		currentY -= MasterSlider.BUTTONS_Y_SEPARATION * MasterSlider.BUTTONS_Y_SEPARATION;

		createModsButton(GuiAlign.LEFT, currentY -= MasterSlider.BUTTONS_Y_SEPARATION);
		createAboutButton(GuiAlign.LEFT, currentY -= MasterSlider.BUTTONS_Y_SEPARATION);
		createOptionsButton(GuiAlign.LEFT, currentY -= MasterSlider.BUTTONS_Y_SEPARATION);
		createPlayButton(GuiAlign.LEFT, currentY -= MasterSlider.BUTTONS_Y_SEPARATION);

		super.show(false);
	}

	private void createPlayButton(GuiAlign guiAlign, float yPos) {
		//	GuiCheckbox checkbox = MasterSlider.createCheckbox("Testing", GuiAlign.LEFT, yPos - MasterSlider.BUTTONS_Y_SEPARATION, false, this);
		GuiTextButton button = MasterSlider.createButton("Play", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenPlay, true));
		button.addRightListener(null);
	}

	private void createOptionsButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Options", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createAboutButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("About", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenAbout, true));
		button.addRightListener(null);
	}

	private void createModsButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Mods", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenMods, true));
		button.addRightListener(null);
	}

	private void createQuitButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Quit", guiAlign, yPos, this);
		button.addLeftListener(FlounderFramework::requestClose);
		button.addRightListener(null);
	}

	@Override
	protected void updateSelf() {
		titleText.setColour(GuiTextButton.HOVER_COLOUR);
		titleText.setBorderColour(GuiTextButton.HOVER_COLOUR);

		if (slideshow == null && isShown()) {
			slideshow = new GuiTexture[]{
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example1.png")).create()),
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example2.png")).create()),
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example3.png")).create()),
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example4.png")).create()),
					new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "example5.png")).create())
			};
			slideoverlay = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slideshow", "overlay.png")).nearestFiltering().create());
			slideshowDriver = new SlideDriver(slideshow.length - 1.0f, 0.0f, slideshow.length * SLIDE_SPEED);
		}

		float progression = slideshowDriver.update(FlounderFramework.getDelta());

		if (progression >= slideshow.length - 1.0f) {
			slideshowDriver = new SlideDriver(slideshow.length - 1.0f, 0.0f, slideshow.length * SLIDE_SPEED);
		} else if (progression == 0.0f) {
			slideshowDriver = new SlideDriver(0.0f, slideshow.length - 1.0f, slideshow.length * SLIDE_SPEED);
		}

		float currentTextureX = progression;

		if (isShown()) {
			for (GuiTexture slide : slideshow) {
				slide.setPosition(currentTextureX, 0.5f, SLIDESHOW_ASPECT, 1.0f);
				slide.update();
				currentTextureX -= SLIDESHOW_ASPECT;
			}
		}

		slideoverlay.setPosition(FlounderDisplay.getAspectRatio() / 2.0f, 0.5f, FlounderDisplay.getAspectRatio(), 1.0f);
		slideoverlay.update();
	}

	public MasterMenu getSuperMenu() {
		return superMenu;
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		if (isShown()) {
			for (int i = 0; i < slideshow.length; i++) {
				guiTextures.add(slideshow[i]);
			}

			guiTextures.add(slideoverlay);
		}
	}
}
