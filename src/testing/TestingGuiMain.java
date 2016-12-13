package testing;

import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.visual.*;

import java.util.*;
import java.util.Timer;

public class TestingGuiMain extends GuiComponent {
	//  private GuiTexture helloGuis1;
	private Text helloTest;

	private Text info;

	private Text sample;

	private boolean updateText;

	//  private GuiTexture texture;

	public TestingGuiMain() {
		//	helloGuis1 = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "guis", "button.png")).clampEdges().create());
		helloTest = Text.newText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.").setFont(FlounderFonts.BRUSH_SCRIPT).textAlign(TextAlign.RIGHT).setFontSize(2.0f).create();
		helloTest.setColour(0.15f, 0.15f, 0.15f);
		//	helloTest.setBorder(new SinWaveDriver(0.075f, 0.1f, 3.75f));
		//	helloTest.setBorderColour(1.0f, 1.0f, 0.0f);
		addText(helloTest, 0.5f, 0.6f, 1.38f);

		info = Text.newText("FPS: 500.0, UPS: 60.0").setFont(FlounderFonts.FORTE).create();
		info.setColour(0.10f, 0.10f, 0.75f);
		addText(info, 0.1f, 0.02f, 1.0f);

		sample = Text.newText("Flounder Framework").setFont(FlounderFonts.BRUSH_SCRIPT).setFontSize(2.0f).textAlign(TextAlign.RIGHT).create();
		sample.setColour(0.75f, 0.10f, 0.10f);
		sample.setBorder(new SinWaveDriver(0.075f, 0.1f, 3.75f));
		sample.setBorderColour(new Colour(0.0f, 0.0f, 0.0f, 0.5f));
		sample.setRotation(9.11f);
		addText(sample, 0.75f, 0.09f, 0.5f);

		//  texture = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "testing.png")).create());

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateText = true;
			}
		}, 0, 100);
	}

	@Override
	protected void updateSelf() {
		if (isShown()) {
			if (updateText) {
				info.setText("FPS: " + Maths.roundToPlace(1.0f / FlounderFramework.getDeltaRender(), 1) + ", UPS: " + Maths.roundToPlace(1.0f / FlounderFramework.getDelta(), 1));
				updateText = false;
			}

			//if (!FlounderKeyboard.getKey(GLFW.GLFW_KEY_S)) {
			//	helloTest.setRotation(helloTest.getRotation() + (FlounderFramework.getDelta() * 25.0f));
			//}

			//	helloGuis1.setPosition(helloTest.getCurrentX(), helloTest.getCurrentY(), helloTest.getCurrentWidth(), helloTest.getCurrentHeight());
			//	helloGuis1.setRotation(helloTest.getRotation());
			//	helloGuis1.update();

			//	texture.setPosition(texture.getScale().x / 2.0f, 0.5f, 0.5f, 0.5f); // Left align.

			//	texture.setPosition((texture.getScale().x + FlounderDisplay.getAspectRatio()) * ((texture.getScale().x * 0.75f) / 2.0f), 0.5f, 0.5f, 0.5f); // Left-Centre align.

			//	texture.setPosition(FlounderDisplay.getAspectRatio() / 2.0f, 0.5f, 0.5f, 0.5f); // Centre align.

			//	texture.setPosition((FlounderDisplay.getAspectRatio() + (texture.getScale().x * 1.25f)) / 2.0f, 0.5f, 0.5f, 0.5f); // Centre-Right align.

			//  texture.setPosition(FlounderDisplay.getAspectRatio() - (texture.getScale().x / 2.0f), 0.5f, 0.5f, 0.5f); // Right align.

			//  texture.update();
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		//	guiTextures.add(helloGuis1);
		//  guiTextures.add(texture);
	}
}
