package game.uis;

import flounder.engine.*;
import flounder.guis.*;
import flounder.visual.*;
import game.*;
import game.uis.screens.*;

import java.util.*;

public class MainMenu extends GuiComponent {
	protected static final float SLIDE_TIME = 0.85f;

	private MainMenuSlider menuSlider;
	private ScreenStartup screenStartup;

	private ValueDriver slideDriver;
	private float backgroundAlpha;
	private boolean displayed;

	public MainMenu() {
		menuSlider = new MainMenuSlider(this);
		screenStartup = new ScreenStartup();

		slideDriver = new ConstantDriver(0.0f);
		backgroundAlpha = 0.0f;
		displayed = false;

		super.addComponent(menuSlider, 0.0f, 0.0f, 1.0f, 1.0f);
		super.addComponent(screenStartup, 0.0f, 0.0f, 1.0f, 1.0f);
	}

	public void display(boolean display) {
		if (screenStartup != null) {
			screenStartup.show(false);
			removeComponent(screenStartup, true);
			screenStartup = null;
		}

		menuSlider.show(display);
		displayed = display;

		if (display) {
			if (!isShown()) {
				show(true);
			}

			slideDriver = new SlideDriver(backgroundAlpha, 1.0f, SLIDE_TIME);
			((MainGuis) FlounderEngine.getManagerGUI()).getOverlayStatus().show(false);
		} else {
			slideDriver = new SlideDriver(backgroundAlpha, 0.0f, SLIDE_TIME);
			((MainGuis) FlounderEngine.getManagerGUI()).getOverlayStatus().show(true);
		}
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public float getBlurFactor() {
		return backgroundAlpha;
	}

	public ValueDriver getSlideDriver() {
		return slideDriver;
	}

	public boolean startingGame() {
		if (screenStartup != null) {
			return screenStartup.isShown();
		}

		return false;
	}

	@Override
	protected void setScreenSpacePosition(float x, float y, float width, float height) {
		super.setScreenSpacePosition(x, y, width, height);
	}

	@Override
	protected void updateSelf() {
		backgroundAlpha = slideDriver.update(FlounderEngine.getDelta());

		if (!displayed && !menuSlider.isShown() && backgroundAlpha == 0.0f) {
			show(false);
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
