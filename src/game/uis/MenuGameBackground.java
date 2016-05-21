package game.uis;

import flounder.engine.*;
import flounder.guis.*;
import flounder.visual.*;

import java.util.*;

public class MenuGameBackground extends GuiComponent {
	private static final float SLIDE_TIME = 0.7f;
	private final MenuGame menu;
	private ValueDriver slideDriver;
	private float backgroundAlpha;
	private boolean displayed = true;

	public MenuGameBackground() {
		menu = new MenuGame(this);
		slideDriver = new ConstantDriver(1);
		super.addComponent(menu, 0, 0, 1, 1);
	}

	public void display(final boolean display) {
		menu.show(display);
		displayed = display;

		if (display) {
			if (!isShown()) {
				show(true);
			}

			slideDriver = new SlideDriver(backgroundAlpha, 1, SLIDE_TIME);
		} else {
			slideDriver = new SlideDriver(backgroundAlpha, 0, SLIDE_TIME);
		}
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public float getBlurFactor() {
		return backgroundAlpha;
	}

	@Override
	protected void setScreenSpacePosition(final float x, final float y, final float width, final float height) {
		super.setScreenSpacePosition(x, y, width, height);
	}

	@Override
	protected void updateSelf() {
		backgroundAlpha = slideDriver.update(FlounderEngine.getDelta());

		if (!displayed && !menu.isShown()) {
			show(false);
		}
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
