package ebon.uis;

import ebon.*;
import flounder.engine.*;
import flounder.guis.*;
import flounder.visual.*;

import java.util.*;

public class MainMenu extends GuiComponent {
	protected static final float SLIDE_TIME = 0.85f;

	private MainSlider mainSlider;

	private ValueDriver slideDriver;
	private float backgroundAlpha;
	private boolean displayed;

	public MainMenu() {
		mainSlider = new MainSlider(this);

		slideDriver = new ConstantDriver(0.0f);
		backgroundAlpha = 0.0f;
		displayed = false;

		addComponent(mainSlider, 0.0f, 0.0f, 1.0f, 1.0f);
	}

	public void display(boolean display) {
		if (displayed == display) {
			return;
		}
		mainSlider.show(display);
		displayed = display;

		if (display) {
			if (!isShown()) {
				show(true);
			}

			slideDriver = new SlideDriver(backgroundAlpha, 1.0f, SLIDE_TIME);
			((EbonGuis) FlounderEngine.getManagerGUI()).getOverlayStatus().show(false);
		} else {
			slideDriver = new SlideDriver(backgroundAlpha, 0.0f, SLIDE_TIME);
			((EbonGuis) FlounderEngine.getManagerGUI()).getOverlayStatus().show(true);
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

	public MainSlider getMainSlider() {
		return mainSlider;
	}

	@Override
	protected void setScreenSpacePosition(float x, float y, float width, float height) {
		super.setScreenSpacePosition(x, y, width, height);
	}

	@Override
	protected void updateSelf() {
		backgroundAlpha = slideDriver.update(FlounderEngine.getDelta());

		if (!displayed && !mainSlider.isShown() && backgroundAlpha == 0.0f) {
			show(false);
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
