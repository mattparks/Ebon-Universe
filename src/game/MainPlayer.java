package game;

import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import game.options.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainPlayer {
	private static final float FRONT_SPEED = 40;
	private static final float UP_SPEED = 30;
	private static final float SIDE_SPEED = 40;

	private final IAxis inputForward;
	private final IAxis inputUp;
	private final IAxis inputSide;

	private final Vector3f velocity;

	private final Vector3f position;
	private final Vector3f rotation;

	public MainPlayer() {
		final IButton leftKeyButtons = new KeyButton(GLFW_KEY_A, GLFW_KEY_LEFT);
		final IButton rightKeyButtons = new KeyButton(GLFW_KEY_D, GLFW_KEY_RIGHT);
		final IButton forwardsKeyButtons = new KeyButton(GLFW_KEY_W, GLFW_KEY_UP);
		final IButton backwardsKeyButtons = new KeyButton(GLFW_KEY_S, GLFW_KEY_DOWN);
		final IButton upKeyButtons = new KeyButton(GLFW_KEY_SPACE);
		final IButton downKeyButtons = new KeyButton(GLFW_KEY_LEFT_SHIFT);

		this.inputForward = new CompoundAxis(new ButtonAxis(forwardsKeyButtons, backwardsKeyButtons), new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_Y));
		this.inputUp = new CompoundAxis(new ButtonAxis(downKeyButtons, upKeyButtons));
		this.inputSide = new CompoundAxis(new ButtonAxis(leftKeyButtons, rightKeyButtons), new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_X));

		this.velocity = new Vector3f(0, 0, 0);

		this.position = new Vector3f(0, -16, -64);
		this.rotation = new Vector3f(0, 0, 0);
	}

	public void update(final boolean paused) {
		if (!paused) {
			rotation.set(0.0f, FlounderEngine.getCamera().getYaw(), 0.0f);
			velocity.x = SIDE_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputSide.getAmount());
			velocity.y = UP_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputUp.getAmount());
			velocity.z = FRONT_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputForward.getAmount());
			Vector3f.rotate(velocity, rotation, velocity);

			boolean pevInsideBlock = Environment.getBlocksManager().insideBlock(position);
			Vector3f.add(position, velocity, position);
			boolean insideBlock = Environment.getBlocksManager().insideBlock(position);

			if (insideBlock && !pevInsideBlock) {
				Vector3f.subtract(position, velocity, position);
			}
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
}
