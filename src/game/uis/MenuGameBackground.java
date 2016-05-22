package game.uis;

import flounder.engine.*;
import flounder.guis.*;
import flounder.visual.*;

import java.util.*;

public class MenuGameBackground extends GuiComponent {
	public static final float SLIDE_TIME = 0.7f;

	private final MenuGame menu;

	private ValueDriver slideDriver;
	private float backgroundAlpha;
	private boolean displayed;

	public MenuGameBackground() {
		menu = new MenuGame(this);

		slideDriver = new ConstantDriver(1.0f);
		backgroundAlpha = 0.0f;
		displayed = true;

		super.addComponent(menu, 0.0f, 0.0f, 1.0f, 1.0f);
	}

	public void display(final boolean display) {
		menu.show(display);
		displayed = display;

		if (display) {
			if (!isShown()) {
				show(true);
			}

			slideDriver = new SlideDriver(backgroundAlpha, 1.0f, SLIDE_TIME);
		} else {
			slideDriver = new SlideDriver(backgroundAlpha, 0.0f, SLIDE_TIME);
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

		if (!displayed && !menu.isShown() && backgroundAlpha == 0.0f) {
			show(false);
		}
	}

	@Override
	protected void getGuiTextures(final List<GuiTexture> guiTextures) {
	}
}
