package ebon.uis;

import ebon.uis.screens.*;
import ebon.world.*;
import flounder.guis.*;

import java.util.*;

public class MenuPause extends GuiComponent {
	private MasterMenu superMenu;
	private MasterSlider masterSlider;

	private ScreenOptions screenOptions;
	private ScreenControls screenControls;

	protected MenuPause(MasterMenu superMenu, MasterSlider masterSlider) {
		this.superMenu = superMenu;
		this.masterSlider = masterSlider;

		this.screenOptions = new ScreenOptions(masterSlider);
		this.screenControls = new ScreenControls(masterSlider);

		float currentY = -MasterSlider.BUTTONS_Y_SEPARATION * 2.0f;
		createResumeButton(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createOptionsButton(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createControlsButton(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);
		createQuitButton(GuiAlign.LEFT, currentY += MasterSlider.BUTTONS_Y_SEPARATION);

		super.show(false);
	}

	private void createResumeButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Resume Game", guiAlign, yPos, this);
		button.addLeftListener(() -> superMenu.display(false));
		button.addRightListener(null);
	}

	private void createOptionsButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Options", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenOptions, true));
		button.addRightListener(null);
	}

	private void createControlsButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Controls", guiAlign, yPos, this);
		button.addLeftListener(() -> masterSlider.setNewSecondaryScreen(screenControls, true));
		button.addRightListener(null);
	}

	private void createQuitButton(GuiAlign guiAlign, float yPos) {
		GuiTextButton button = MasterSlider.createButton("Main Menu", guiAlign, yPos, this);
		button.addLeftListener(() -> {
			masterSlider.closeSecondaryScreen();
			masterSlider.getSuperMenu().display(true);
			masterSlider.sliderStartMenu(true);
			EbonWorld.clear();
		});
		button.addRightListener(null);
	}

	public MasterMenu getSuperMenu() {
		return superMenu;
	}

	@Override
	protected void updateSelf() {
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
