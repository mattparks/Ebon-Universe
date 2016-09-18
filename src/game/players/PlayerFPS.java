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
	private static final float DECELERATION = 0.10f;
	private static final float AUTOPILOT_OFFSET = 0.10f;

	private static final float X_SPEED = 50.0f;
	private static final float Y_SPEED = 12.5f;
	private static final float Z_SPEED = 50.0f;
	private static final float W_SPEED = 50.0f;
	private static final float SPEED_BOOST_SCALE = 3.125f;

	private IAxis inputX;
	private IAxis inputY;
	private IAxis inputZ;
	private IAxis inputW;
	private CompoundButton inputSlow;
	private CompoundButton inputAutopilot;

	private float speedMagnatude;
	private Vector3f velocity;

	private Vector3f position;
	private Vector3f rotation;

	private boolean autopilotEnabled;
	private boolean autopilotForceStop;
	private float autopilotStopSpeed;
	private Vector3f autopilotWaypoint;

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

		this.inputX = new CompoundAxis(new ButtonAxis(leftKeyButtons, rightKeyButtons), new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_X));
		this.inputY = new CompoundAxis(new ButtonAxis(downKeyButtons, upKeyButtons), new ButtonAxis(new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_UP)));
		this.inputZ = new CompoundAxis(new ButtonAxis(backwardsKeyButtons, forwardsKeyButtons), new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_Y));
		this.inputW = new CompoundAxis(new ButtonAxis(rollLeftButtons, rollRightButtons));
		this.inputSlow = new CompoundButton(slowDownKeyButtons, new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_GUI_LEFT));
		this.inputAutopilot = new CompoundButton(autopilotButtons);

		this.velocity = new Vector3f();

		this.position = new Vector3f();
		this.rotation = new Vector3f();

		this.autopilotEnabled = false;
		this.autopilotForceStop = false;
		this.autopilotStopSpeed = 0.0f;
		this.autopilotWaypoint = new Vector3f();
	}

	@Override
	public void update(boolean paused) {
		if (!paused) {
			// Update multiplier and waypoint.
			float multiplier = Environment.getGalaxyManager().getInSystemStar() != null ? (float) (Environment.getGalaxyManager().getInSystemStar().getSolarRadius() * 0.05f) : 1.0f;

			// Update autopilot inputs.
			if (inputAutopilot.wasDown() && Environment.getGalaxyManager().getWaypoint().getPosition() != null) {
				if (!autopilotEnabled) {
					autopilot(true, Environment.getGalaxyManager().getWaypoint().getPosition());
				} else {
					autopilotForceStop = true;
					autopilotStopSpeed = speedMagnatude;
				}
			}

			// Update roll rotations.
			rotation.set(FlounderEngine.getCamera().getRotation());
			rotation.z += multiplier * W_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputW.getAmount());

			// Update normal flying camera.
			if (!autopilotEnabled) {
				if (inputSlow.isDown()) {
					speedMagnatude -= multiplier * DECELERATION * FlounderEngine.getDelta();
					speedMagnatude = Math.max(0.0f, speedMagnatude);
				} else {
					speedMagnatude += multiplier * ACCELERATION * FlounderEngine.getDelta() * (float) Math.abs(Math.log(speedMagnatude + 0.5f)) * Maths.deadband(0.05f, inputZ.getAmount());
					speedMagnatude = Math.min(1.0f, Math.abs(speedMagnatude)) * (speedMagnatude < 0.0f ? -1.0f : 1.0f);
				}

				velocity.x = multiplier * X_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputX.getAmount());
				velocity.y = multiplier * Y_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputY.getAmount());
				velocity.z = multiplier * Z_SPEED * FlounderEngine.getDelta() * -Maths.deadband(0.05f, inputZ.getAmount());

				Vector3f.rotate(velocity, rotation, velocity);
				Vector3f.add(position, velocity, position);
			} else { // Update autopilot.
				float arrivalTime = ((Vector3f.getDistance(position, autopilotWaypoint) - AUTOPILOT_OFFSET) / speedMagnatude) / 60.0f;
				float stopTime = speedMagnatude / DECELERATION;

				if (arrivalTime <= stopTime || autopilotForceStop) {
					speedMagnatude -= multiplier * DECELERATION * FlounderEngine.getDelta();

					if (speedMagnatude < 0.0f && autopilotStopSpeed >= 0.0f) {
						autopilot(false, position);
					}
				} else {
					speedMagnatude += multiplier * ACCELERATION * FlounderEngine.getDelta() * (float) Math.abs(Math.log(speedMagnatude + 0.5f));
				}

				Vector3f.subtract(position, autopilotWaypoint, velocity);

				if (velocity.lengthSquared() > 0.02f) {
					velocity.normalize();
				} else {
					autopilot(false, position);
				}

				velocity.scale(speedMagnatude);
				velocity.negate();
				Vector3f.add(position, velocity, position);
			}
		}
	}

	private void autopilot(boolean enable, Vector3f waypoint) {
		speedMagnatude = 0.0f;
		autopilotEnabled = enable;
		autopilotForceStop = false;
		autopilotStopSpeed = 0.0f;
		autopilotWaypoint.set(waypoint);
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
