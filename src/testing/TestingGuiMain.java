package testing;

import flounder.devices.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.textures.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class TestingGuiMain extends GuiComponent {
	private GuiTexture helloGuis;
	private Text helloTest;
	private float rotation;

	public TestingGuiMain() {
		helloGuis = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "guis", "button.png")).clampEdges().create());
		helloTest = Text.newText("Hello World").setFont(FlounderFonts.FFF_FORWARD).setFontSize(3.0f).create();
		helloTest.setColour(0.15f, 0.15f, 0.15f);
		addText(helloTest, 0.5f, 0.5f, 1.0f);
		rotation = 0.0f;

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return FlounderKeyboard.getKey(GLFW.GLFW_KEY_A);
			}

			@Override
			public void onEvent() {
				rotation += 50.0f * FlounderFramework.getDelta();
			}
		});

		FlounderEvents.addEvent(new IEvent() {
			@Override
			public boolean eventTriggered() {
				return FlounderKeyboard.getKey(GLFW.GLFW_KEY_D);
			}

			@Override
			public void onEvent() {
				rotation -= 50.0f * FlounderFramework.getDelta();
			}
		});
	}

	@Override
	protected void updateSelf() {
		float x = 0.5f;
		float y = 0.5f;
		float width = 0.5f / FlounderDisplay.getAspectRatio();
		float height = 0.5f;
		helloGuis.setPosition(x, y, width, height);
		helloGuis.setRotation(rotation);
		helloGuis.update();

		helloTest.setRotation(rotation);
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		guiTextures.add(helloGuis);
	}
}
