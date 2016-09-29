package ebon.uis;

import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.textures.*;
import flounder.visual.*;

import java.util.*;

public class GuiNotification extends GuiComponent {
	private static final float BACKGROUND_PADDING = 0.0125f;
	private static final float FADE_TIME = 5.0f;

	private Text text;
	private GuiTexture background;

	private ValueDriver fadeDriver;

	public GuiNotification(Text text) {
		fadeDriver = new SlideDriver(1.0f, 0.0f, FADE_TIME);

		this.text = text;
		this.background = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "guis", "message.png")).clampEdges().create());
		background.setAlphaDriver(fadeDriver);
		text.setAlphaDriver(fadeDriver);
		addText(text, (1.0f / 3.0f), 0.0f, 1.0f);
	}

	public Text getText() {
		return text;
	}

	public void resetFade() {
		fadeDriver = new SlideDriver(0.0f, 1.0f, FADE_TIME * 0.7f);
		background.setAlphaDriver(fadeDriver);
		text.setAlphaDriver(fadeDriver);
	}

	public boolean isAlive() {
		return fadeDriver.update(FlounderEngine.getDelta()) > 0.0f;
	}

	@Override
	protected void updateSelf() {
		float fade = fadeDriver.update(FlounderEngine.getDelta());

		if (fade >= 1.0f) {
			fadeDriver = new SlideDriver(1.0f, 0.0f, FADE_TIME);
			background.setAlphaDriver(fadeDriver);
			text.setAlphaDriver(fadeDriver);
		}

		float width = (text.getMaxLineSize() / FlounderDisplay.getAspectRatio()) * text.getScale();
		float height = text.getCurrentHeight();
		background.getPosition().x = super.getPosition().x;
		background.getPosition().y = super.getPosition().y - (BACKGROUND_PADDING * text.getScale());
		background.getScale().set(width, height + ((BACKGROUND_PADDING * 2.0f) / text.getScale()));
		background.getColourOffset().set(GuiTextButton.DEFAULT_COLOUR);
		background.update();
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		guiTextures.add(background);
	}
}

