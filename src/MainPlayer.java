import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.vectors.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainPlayer {
	private static final float RUN_SPEED = 40;
	private static final float TURN_SPEED = 160;

	private IAxis inputForward;
	private IAxis inputTurn;

	private float currentSpeed;
	private float currentTurnSpeed;

	private Vector3f position;
	private Vector3f rotation;

	public MainPlayer() {
		IButton leftKeyButtons = new KeyButton(GLFW_KEY_A, GLFW_KEY_LEFT);
		IButton rightKeyButtons = new KeyButton(GLFW_KEY_D, GLFW_KEY_RIGHT);
		IButton upKeyButtons = new KeyButton(GLFW_KEY_W, GLFW_KEY_UP);
		IButton downKeyButtons = new KeyButton(GLFW_KEY_S, GLFW_KEY_DOWN);

		this.inputForward = new CompoundAxis(new ButtonAxis(upKeyButtons, downKeyButtons), new JoystickAxis(0, 1));
		this.inputTurn = new CompoundAxis(new ButtonAxis(leftKeyButtons, rightKeyButtons), new JoystickAxis(0, 3));

		this.position = new Vector3f();
		this.rotation = new Vector3f();
	}

	public void update(final boolean paused) {
		if (!paused) {
			currentSpeed = -RUN_SPEED * Maths.deadband(0.05f, inputForward.getAmount());
			currentTurnSpeed = 0.0f; // Add back in once a player model is added. // (float) (-TURN_SPEED * Maths.deadband(0.05f, inputTurn.getAmount()));
			float distance = currentSpeed * FlounderEngine.getDelta();
			float dx = (float) (-distance * Math.sin(Math.toRadians(Maths.normalizeAngle(rotation.getY() + FlounderEngine.getCamera().getYaw()))));
			float dz = (float) (-distance * Math.cos(Math.toRadians(Maths.normalizeAngle(rotation.getY() - FlounderEngine.getCamera().getYaw()))));
			float ry = currentTurnSpeed * FlounderEngine.getDelta();
			position.set(position.x + dx, position.y, position.z + dz);
			rotation.set(rotation.x, rotation.y + ry, rotation.z);
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
}
