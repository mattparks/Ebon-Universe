package ebon.uis;

import ebon.*;
import ebon.uis.screens.*;
import flounder.guis.*;

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
		createResumeButton(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createOptionsButton(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createControlsButton(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);
		createQuitButton(GuiAlign.LEFT, currentY += MainSlider.BUTTONS_Y_SEPARATION);

		super.show(false);
	}

	private void createResumeButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Resume Game", guiAlign, yPos, this);
		button.addLeftListener(() -> superMenu.display(false));
		button.addRightListener(null);
	}

	private void createOptionsButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Options", guiAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createControlsButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Controls", guiAlign, yPos, this);
		button.addLeftListener(() -> mainSlider.setNewSecondaryScreen(screenControls, true));
		button.addRightListener(null);
	}

	private void createQuitButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MainSlider.createButton("Main Menu", guiAlign, yPos, this);
		button.addLeftListener(() -> {
			mainSlider.closeSecondaryScreen();
			mainSlider.getSuperMenu().display(true);
			mainSlider.sliderStartMenu(true);
			Ebon.instance.destroyWorld();
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
