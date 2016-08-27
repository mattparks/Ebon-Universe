package game.uis;

import flounder.engine.*;
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
		createResumeButton(currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createOptionsButton(currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createControlsButton(currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createQuitButton(currentY += MainSlider.BUTTONS_Y_SEPARATION);

		super.show(false);
	}

	private void createResumeButton(float yPos) {
		GuiTextButton button = MainSlider.createButton("Resume Game", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> superMenu.display(false));
		button.addRightListener(null);
	}

	private void createOptionsButton(float yPos) {
		GuiTextButton button = MainSlider.createButton("Options", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createControlsButton(float yPos) {
		GuiTextButton button = MainSlider.createButton("Controls", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenControls, true));
		button.addRightListener(null);
	}

	private void createQuitButton(float yPos) {
		GuiTextButton button = MainSlider.createButton("Quit", MainSlider.BUTTONS_X_POS, yPos, MainSlider.BUTTONS_X_WIDTH, MainSlider.BUTTONS_Y_SIZE, MainSlider.FONT_SIZE, this);
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
