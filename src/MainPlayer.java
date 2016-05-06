import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.vectors.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainPlayer {
	private static final Vector3f DIRECTION_AXIS = new Vector3f(0, 0, 1); // TODO
	private static final Vector3f DIRECTION_CENTRE = new Vector3f(0.0f, 0.0f, 0.0f);
	private static final float FRONT_SPEED = 40;
	private static final float UP_SPEED = 10;
	private static final float SIDE_SPEED = 40;

	private final IAxis inputForward;
	private final IAxis inputTurn;

	private final Vector3f direction;

	private final Vector3f position;
	private final Vector3f rotation;

	public MainPlayer() {
		final IButton leftKeyButtons = new KeyButton(GLFW_KEY_A, GLFW_KEY_LEFT);
		final IButton rightKeyButtons = new KeyButton(GLFW_KEY_D, GLFW_KEY_RIGHT);
		final IButton upKeyButtons = new KeyButton(GLFW_KEY_W, GLFW_KEY_UP);
		final IButton downKeyButtons = new KeyButton(GLFW_KEY_S, GLFW_KEY_DOWN);

		this.inputForward = new CompoundAxis(new ButtonAxis(upKeyButtons, downKeyButtons), new JoystickAxis(0, 1));
		this.inputTurn = new CompoundAxis(new ButtonAxis(leftKeyButtons, rightKeyButtons), new JoystickAxis(0, 3));

		this.direction = new Vector3f(0, 0, 0);

		this.position = new Vector3f(0, 10, 0);
		this.rotation = new Vector3f(0, 0, 0);
	}

	public void update(final boolean paused) {
		if (!paused) {
			direction.z = -FRONT_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputForward.getAmount());
			direction.y = -UP_SPEED * 0.0f;
			direction.x = -SIDE_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputTurn.getAmount());
			// Vector3f.rotate(direction, FlounderEngine.getCamera().getYaw(), DIRECTION_AXIS, DIRECTION_CENTRE, direction);

			position.set(position.x + direction.x, position.y + direction.y, position.z + direction.z);
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
}
