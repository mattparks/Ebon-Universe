package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import game.*;
import game.uis.screens.*;

import java.util.*;

public class MenuPause extends GuiComponent {
	private MainMenu superMenu;
	private MainSlider mainSlider;

	private ScreenOptions screenOptions;
	private ScreenControls screenControls;

	protected MenuPause(MainMenu superMenu, MainSlider mainSlider) {
		this.superMenu = superMenu;
		this.mainSlider = mainSlider;

		this.screenOptions = new ScreenOptions(mainSlider);
		this.screenControls = new ScreenControls(mainSlider);

		float currentY = -MainSlider.BUTTONS_Y_SEPARATION * 2.0f;
		createResumeButton(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createOptionsButton(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createControlsButton(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createQuitButton(TextAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		super.show(false);
	}

	private void createResumeButton(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Resume Game", textAlign, yPos, this);
		button.addLeftListener(() -> superMenu.display(false));
		button.addRightListener(null);
	}

	private void createOptionsButton(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Options", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createControlsButton(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Controls", textAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenControls, true));
		button.addRightListener(null);
	}

	private void createQuitButton(TextAlign textAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Main Menu", textAlign, yPos, this);
		button.addLeftListener(() -> {
			mainSlider.closeSecondaryScreen();
			mainSlider.getSuperMenu().display(true);
			mainSlider.sliderStartMenu(true);
			((MainGame) FlounderEngine.getGame()).destroyWorld();
		});
		button.addRightListener(null);
	}

	public MainMenu getSuperMenu() {
		return superMenu;
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
