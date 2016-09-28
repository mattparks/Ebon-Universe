package ebon.uis;

import ebon.*;
import ebon.celestial.manager.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.resources.*;
import flounder.textures.*;
import flounder.visual.*;

import java.util.*;
import java.util.Timer;

public class OverlayStatus extends GuiComponent {
	private ValueDriver mainDriver;

	private Text updatesText;
	private Text positionText;
	private Text velocityText;
	private boolean updateText;

	private GuiTexture crossHair;

	private GuiTexture starSelection;

	private List<String> quedMessages;
	private GuiNotification message;

	public OverlayStatus() {
		mainDriver = new ConstantDriver(-MainSlider.SLIDE_SCALAR);

		updatesText = createStatus("FPS: 0 | UPS: 0", 0.02f);
		positionText = createStatus("POSITION: [0, 0, 0]", 0.06f);
		velocityText = createStatus("VELOCITY: 0 ly/s", 0.10f);

		crossHair = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "crosshair.png")).create());
		crossHair.getTexture().setNumberOfRows(4);
		crossHair.setSelectedRow(Ebon.configMain.getIntWithDefault("crosshair", 1, () -> crossHair.getSelectedRow()));

		starSelection = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "crosshair.png")).create());
		starSelection.getTexture().setNumberOfRows(4);
		starSelection.setSelectedRow(11);
		starSelection.setColourOffset(new Colour(0.0f, 0.0f, 1.0f));

		quedMessages = new ArrayList<>();
		message = new GuiNotification(Text.newText("Hello World").create());
		message.getText().setColour(MainSlider.TEXT_COLOUR);
		addComponent(message, 0.3f, 0.05f, 0.5f, 0.2f);

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
		Text text = Text.newText(content).setFontSize(0.75f).create();
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
		if (!message.isAlive() && !quedMessages.isEmpty()) {
			message.getText().setText(quedMessages.get(0));
			message.resetFade();
			quedMessages.remove(0);
		}

		float mainValue = mainDriver.update(FlounderEngine.getDelta());

		if (updateText) {
			updatesText.setText("FPS: " + Maths.roundToPlace(1.0f / FlounderEngine.getDelta(), 1) + " | UPS: " + Maths.roundToPlace(1.0f / FlounderEngine.getDelta(), 1));
			positionText.setText("POSITION: [" + Maths.roundToPlace(FlounderEngine.getCamera().getPosition().x, 1) + ", " + Maths.roundToPlace(FlounderEngine.getCamera().getPosition().y, 1) + ", " + Maths.roundToPlace(FlounderEngine.getCamera().getPosition().z, 1) + "]");
			velocityText.setText("VELOCITY: " + EbonGalaxies.getPlayerVelocity());

			updateText = false;
		}

		float averageArea = (FlounderDisplay.getWidth() + FlounderDisplay.getHeight()) / 2.0f;
		float width = (33.3f / averageArea);
		float height = width * FlounderDisplay.getAspectRatio();
		crossHair.setPosition(0.5f - (width / 2.0f) + super.getPosition().x, 0.5f - (height / 2.0f), width, height);
		crossHair.update();
		crossHair.setColourOffset(GuiTextButton.HOVER_COLOUR);

		if (EbonGalaxies.getWaypoint() != null) {
			float selectionScale = 1.0f;
			float selectionX = Maths.clamp(EbonGalaxies.getWaypoint().getScreenPosition().x, 0.0f, 1.0f);
			float selectionY = Maths.clamp(EbonGalaxies.getWaypoint().getScreenPosition().y, 0.0f, 1.0f);
			starSelection.setPosition(selectionX - ((selectionScale * width) / 2.0f) + super.getPosition().x, selectionY - ((selectionScale * height) / 2.0f), (selectionScale * width), (selectionScale * height));
			starSelection.update();
		}

		if (mainValue == -MainSlider.SLIDE_SCALAR) {
			super.show(true);
		} else {
			super.show(true);
		}

		super.setRelativeX(mainValue);
	}

	public void addMessage(String newMessage) {
		if (message.isAlive() && newMessage.equals(message.getText().getTextString())) {
			return;
		}

		if (quedMessages.contains(newMessage)) {
			return;
		}

		quedMessages.add(newMessage);
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		if (EbonGalaxies.getWaypoint() != null && EbonGalaxies.getWaypoint().getScreenPosition().z >= 0) {
			guiTextures.add(starSelection);
		}

		guiTextures.add(crossHair);
	}
}
