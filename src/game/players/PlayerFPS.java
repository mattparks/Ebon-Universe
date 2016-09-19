package game.players;

import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import game.*;
import game.options.*;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerFPS implements IPlayer {
	private static final float ACCELERATION = 0.05f;
	private static final float DECELERATION = 0.08f;

	private static final float X_SPEED = 50.0f;
	private static final float Y_SPEED = 12.5f;
	private static final float Z_SPEED = 50.0f;
	private static final float W_SPEED = 75.0f;

	private IAxis inputX;
	private IAxis inputY;
	private IAxis inputZ;
	private IAxis inputW;
	private CompoundButton inputSlow;
	private CompoundButton inputAutopilot;
	private CompoundButton inputGravity;

	private float speedMagnitude;
	private Vector3f velocity;

	private Vector3f position;
	private Vector3f rotation;

	private Autopilot autopilot;
	private boolean gravityEnabled;
	private float gravityPitch;

	@Override
	public void init() {
		IButton leftKeyButtons = new KeyButton(GLFW_KEY_A, GLFW_KEY_LEFT);
		IButton rightKeyButtons = new KeyButton(GLFW_KEY_D, GLFW_KEY_RIGHT);

		IButton forwardsKeyButtons = new KeyButton(GLFW_KEY_W, GLFW_KEY_UP);
		IButton backwardsKeyButtons = new KeyButton(GLFW_KEY_S, GLFW_KEY_DOWN);

		IButton upKeyButtons = new KeyButton(GLFW_KEY_LEFT_SHIFT);
		IButton downKeyButtons = new KeyButton(GLFW_KEY_LEFT_CONTROL);

		IButton rollLeftButtons = new KeyButton(GLFW_KEY_Q);
		IButton rollRightButtons = new KeyButton(GLFW_KEY_E);

		IButton slowDownKeyButtons = new KeyButton(GLFW_KEY_SPACE);

		IButton autopilotButtons = new KeyButton(GLFW_KEY_H);
		IButton gravityButtons = new KeyButton(GLFW_KEY_G);

		this.inputX = new CompoundAxis(new ButtonAxis(leftKeyButtons, rightKeyButtons), new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_X));
		this.inputY = new CompoundAxis(new ButtonAxis(downKeyButtons, upKeyButtons), new ButtonAxis(new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_UP)));
		this.inputZ = new CompoundAxis(new ButtonAxis(backwardsKeyButtons, forwardsKeyButtons), new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_Y));
		this.inputW = new CompoundAxis(new ButtonAxis(rollLeftButtons, rollRightButtons));
		this.inputSlow = new CompoundButton(slowDownKeyButtons, new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT));
		this.inputAutopilot = new CompoundButton(autopilotButtons);
		this.inputGravity = new CompoundButton(gravityButtons);

		this.velocity = new Vector3f();

		this.position = new Vector3f();
		this.rotation = new Vector3f();

		this.autopilot = new Autopilot(ACCELERATION, DECELERATION);
		this.gravityEnabled = false;
		this.gravityPitch = 0.0f;
	}

	@Override
	public void update(boolean paused) {
		//	if (!paused) {
		// Update multiplier and waypoint.
		float multiplier = Environment.getGalaxyManager().getInSystemStar() != null ? (float) (Environment.getGalaxyManager().getInSystemStar().getSolarRadius() * 0.05f) : 1.0f;
		autopilot.setWaypoint(Environment.getGalaxyManager().getWaypoint(), true);

		// Update autopilot inputs.
		if (inputAutopilot.wasDown() && Environment.getGalaxyManager().getWaypoint().getPosition() != null) {
			autopilot.toggleAutopilot(speedMagnitude);
		}

		// Update gravity inputs.
		if (inputGravity.wasDown()) {
			gravityEnabled = !gravityEnabled;
			((MainGuis) FlounderEngine.getManagerGUI()).getOverlayStatus().addMessage("Gravity " + (gravityEnabled ? "Enabled" : "Disabled"));
		}

		// Update roll rotations.
		rotation.set(FlounderEngine.getCamera().getRotation());
		rotation.z += W_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputW.getAmount());

		// Updates gravity effects.
		if (gravityEnabled && rotation.z != gravityPitch) {
			rotation.z += (W_SPEED / 15.25f) * FlounderEngine.getDelta() * (gravityPitch - rotation.z);
			rotation.z = Maths.deadband(0.2f, rotation.z - gravityPitch) + gravityPitch;
		} else {
			rotation.z = Maths.normalizeAngle(rotation.z);
		}

		// Update normal flying camera.
		if (!autopilot.isEnabled()) {
			if (inputSlow.isDown()) {
				speedMagnitude -= multiplier * DECELERATION * FlounderEngine.getDelta();
				speedMagnitude = Math.max(0.0f, speedMagnitude);
			} else {
				speedMagnitude += multiplier * ACCELERATION * FlounderEngine.getDelta() * (float) Math.abs(Math.log(speedMagnitude + 0.5f)) * Maths.deadband(0.05f, inputZ.getAmount());
				speedMagnitude = Math.min(1.0f, Math.abs(speedMagnitude)) * (speedMagnitude < 0.0f ? -1.0f : 1.0f);
			}

			velocity.x = 0;//multiplier * X_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputX.getAmount());
			velocity.y = 0;//multiplier * Y_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputY.getAmount());
			velocity.z = -speedMagnitude;//multiplier * Z_SPEED * FlounderEngine.getDelta() * -Maths.deadband(0.05f, inputZ.getAmount());

			Vector3f.rotate(velocity, rotation, velocity);
			Vector3f.add(position, velocity, position);
		} else { // Update autopilot.
			autopilot.update(position);
			speedMagnitude = autopilot.getSpeedMagnitude();
			Vector3f.add(position, velocity.set(autopilot.getVelocity()), position);
		}
		//	}
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation.set(rotation);
	}

	@Override
	public void dispose() {
	}
}
