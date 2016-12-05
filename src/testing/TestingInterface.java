package testing;

import flounder.devices.*;
import flounder.events.*;
import flounder.framework.*;
import flounder.helpers.*;
import flounder.inputs.*;
import flounder.logger.*;
import flounder.standard.*;
import org.lwjgl.glfw.*;

public class TestingInterface extends IExtension implements IStandard {
	public TestingInterface() {
		super(FlounderStandard.class, FlounderDisplay.class, FlounderKeyboard.class);
	}

	@Override
	public void init() {
		FlounderEvents.addEvent(new IEvent() {
			private KeyButton button = new KeyButton(GLFW.GLFW_KEY_W);

			@Override
			public boolean eventTriggered() {
				return button.wasDown();
			}

			@Override
			public void onEvent() {
				OpenGlUtils.goWireframe(!OpenGlUtils.isInWireframe());
			}
		});
	}

	@Override
	public void update() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isActive() {
		return true;
	}
}
