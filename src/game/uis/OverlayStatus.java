package game.uis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.resources.*;
import flounder.textures.*;
import flounder.visual.*;
import game.*;

import java.util.*;
import java.util.Timer;

public class OverlayStatus extends GuiComponent {
	private ValueDriver mainDriver;

	private Text fpsText;
	private Text positionText;
	private boolean updateText;

	private GuiTexture crossHair;

	private GuiTexture starSelection;

	public OverlayStatus() {
		mainDriver = new ConstantDriver(-MainSlider.SLIDE_SCALAR);

		fpsText = createStatus("FPS: 0", 0.02f);
		positionText = createStatus("POSITION: [0, 0, 0]", 0.05f);

		crossHair = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "crosshair.png")).clampEdges().create());
		crossHair.getTexture().setNumberOfRows(4);
		crossHair.setSelectedRow(MainGame.CONFIG.getIntWithDefault("crosshair", 1, () -> crossHair.getSelectedRow()));

		starSelection = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "crosshair.png")).clampEdges().create());
		starSelection.getTexture().setNumberOfRows(4);
		starSelection.setSelectedRow(0);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateText = true;
			}
		}, 0, 100);

		super.show(true);
	}

	private Text createStatus(String content, float yPos) {
		Text text = Text.newText(content, TextAlign.LEFT).setFont(FlounderEngine.getFonts().segoeUi).setFontSize(1.0f).create();
		text.setColour(MainSlider.TEXT_COLOUR);
		text.setBorderColour(0.15f, 0.15f, 0.15f);
		text.setBorder(new ConstantDriver(0.04f));
		super.addText(text, 0.02f, yPos, 1.0f);
		return text;
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
			positionText.setText("POSITION: [" + Maths.roundToPlace(FlounderEngine.getCamera().getPosition().x, 1) + ", " + Maths.roundToPlace(FlounderEngine.getCamera().getPosition().y, 1) + ", " + Maths.roundToPlace(FlounderEngine.getCamera().getPosition().z, 1) + "]");
			updateText = false;
		}

		float averageArea = (FlounderEngine.getDevices().getDisplay().getWidth() + FlounderEngine.getDevices().getDisplay().getHeight()) / 2.0f;
		float width = (33.3f / averageArea);
		float height = width * FlounderEngine.getDevices().getDisplay().getAspectRatio();
		crossHair.setPosition(0.5f - (width / 2.0f) + super.getPosition().x, 0.5f - (height / 2.0f), width, height);
		crossHair.update();
		crossHair.setColourOffset(GuiTextButton.HOVER_COLOUR);

		float selectionScale = 100.0f / Environment.starScreenPos.z;
		starSelection.setPosition(Environment.starScreenPos.x - ((selectionScale * width) / 2.0f) + super.getPosition().x, Environment.starScreenPos.y - ((selectionScale * height) / 2.0f), (selectionScale * width), (selectionScale * height));
		starSelection.update();
		starSelection.setColourOffset(GuiTextButton.HOVER_COLOUR);

		if (mainValue == -MainSlider.SLIDE_SCALAR) {
			super.show(true);
		} else {
			super.show(true);
		}

		super.setRelativeX(mainValue);
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		guiTextures.add(crossHair);
		guiTextures.add(starSelection);
	}
}
