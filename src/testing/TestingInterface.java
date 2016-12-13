package testing;

import flounder.devices.*;
import flounder.events.*;
import flounder.framework.*;
import flounder.helpers.*;
import flounder.inputs.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import flounder.standard.*;

import static org.lwjgl.glfw.GLFW.*;

public class TestingInterface extends IExtension implements IStandard {
	public TestingInterface() {
		super(FlounderStandard.class, FlounderDisplay.class, FlounderKeyboard.class, TestingBoundings.class);
	}

	@Override
	public void init() {
		FlounderEvents.addEvent(new IEvent() {
			private KeyButton button = new KeyButton(GLFW_KEY_F2);

			@Override
			public boolean eventTriggered() {
				return button.wasDown();
			}

			@Override
			public void onEvent() {
				FlounderDisplay.screenshot();
			}
		});

		FlounderEvents.addEvent(new IEvent() {
			private KeyButton button = new KeyButton(GLFW_KEY_F11);

			@Override
			public boolean eventTriggered() {
				return button.wasDown();
			}

			@Override
			public void onEvent() {
				FlounderDisplay.setFullscreen(!FlounderDisplay.isFullscreen());
			}
		});

		FlounderEvents.addEvent(new IEvent() {
			private KeyButton button = new KeyButton(GLFW_KEY_P);

			@Override
			public boolean eventTriggered() {
				return button.wasDown();
			}

			@Override
			public void onEvent() {
				OpenGlUtils.goWireframe(!OpenGlUtils.isInWireframe());
			}
		});

		FlounderBounding.toggle(true);
		FlounderProfiler.toggle(true);
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
