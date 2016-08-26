package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.visual.*;

import java.util.*;
import java.util.Timer;

public class OverlayStatus extends GuiComponent {
	private ValueDriver mainDriver;

	private Text fpsText;
	private boolean updateText;

	public OverlayStatus() {
		mainDriver = new ConstantDriver(-MainSlider.SLIDE_SCALAR);

		fpsText = Text.newText("FPS: " + Maths.roundToPlace(1.0f / FlounderEngine.getDelta(), 1)).setFontSize(1.0f).create();
		fpsText.setColour(MainSlider.TEXT_COLOUR);
		fpsText.setBorderColour(0.15f, 0.15f, 0.15f);
		fpsText.setBorder(new ConstantDriver(0.04f));
		super.addText(fpsText, 0.02f, 0.02f, 1.0f);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateText = true;
			}
		}, 0, 1000);

		super.show(true);
	}

	@Override
	public void show(boolean visible) {
		mainDriver = new SlideDriver(getRelativeX(), visible ? 0.0f : -MainSlider.SLIDE_SCALAR, MainMenu.SLIDE_TIME);
	}

	@Override
	protected void updateSelf() {
		float mainValue = mainDriver.update(FlounderEngine.getDelta());

		if (updateText) {
			fpsText.setText("FPS: " + Maths.roundToPlace(1.0f / FlounderEngine.getDelta(), 1));
			updateText = false;
		}

		if (mainValue == -MainSlider.SLIDE_SCALAR) {
			super.show(true);
		} else {
			super.show(true);
		}

		super.setRelativeX(mainValue);
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {

	}
}
