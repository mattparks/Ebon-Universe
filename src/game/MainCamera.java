package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.profiling.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.space.*;
import game.options.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainCamera implements ICamera {
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 3200.0f;
	private static final float FIELD_OF_VIEW = 70.0f;

	private static final float ROTATE_AGILITY = 6.0f;
	private static final float PITCH_AGILITY = 8.0f;

	private final static float CAMERA_AIM_OFFSET = 32.0f;
	private final static float MAX_ANGLE_OF_ELEVATION = 1.5f;
	private final static float PITCH_OFFSET = 3.0f;

	private final static float MAX_HORIZONTAL_CHANGE = 500.0f;
	private final static float MAX_VERTICAL_CHANGE = 5.0f;

	private final static float INFLUENCE_OF_MOUSEDY = -1000.0f;
	private final static float INFLUENCE_OF_MOUSEDX = INFLUENCE_OF_MOUSEDY * 92.0f;
	private final static float INFLUENCE_OF_JOYSTICKDY = -1.0f;
	private final static float INFLUENCE_OF_JOYSTICKDX = 100.0f * INFLUENCE_OF_JOYSTICKDY;

	public static int toggleMouseMoveKey;

	private Vector3f position;
	private Vector3f rotation;
	private Frustum viewFrustum;
	private Matrix4f viewMatrix;
	private JoystickAxis joystickRotateX;
	private JoystickAxis joystickRotateY;

	private float angleOfElevation;
	private float angleAroundPlayer;

	private Vector3f targetPosition;
	private float targetElevation;
	private float targetRotationAngle;

	@Override
	public void init() {
		this.position = new Vector3f(0.0f, 0.0f, 0.0f);
		this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
		this.viewFrustum = new Frustum();
		this.viewMatrix = new Matrix4f();

		this.toggleMouseMoveKey = GLFW_MOUSE_BUTTON_LEFT;
		this.joystickRotateX = new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_X);
		this.joystickRotateY = new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_Y);

		this.angleOfElevation = 0.0f;
		this.angleAroundPlayer = 0.0f;

		this.targetPosition = new Vector3f();
		this.targetElevation = 0.0f;
		this.targetRotationAngle = 0.0f;

		FlounderProfiler.addTab("MainCamera");
	}

	@Override
	public float getNearPlane() {
		return NEAR_PLANE;
	}

	@Override
	public float getFarPlane() {
		return FAR_PLANE;
	}

	@Override
	public float getFOV() {
		return FIELD_OF_VIEW;
	}

	@Override
	public void moveCamera(final Vector3f focusPosition, final Vector3f focusRotation, final boolean gamePaused) {
		calculateHorizontalAngle(gamePaused);
		calculateVerticalAngle(gamePaused);

		this.targetPosition.set(focusPosition);

		updateHorizontalAngle();
		updatePitchAngle();
		calculatePosition();
		updateViewMatrix(position, rotation);

		if (FlounderProfiler.isOpen()) {
			FlounderProfiler.add("MainCamera", "Angle Of Elevation", angleOfElevation);
			FlounderProfiler.add("MainCamera", "Rotation", rotation);
			FlounderProfiler.add("MainCamera", "Position", position);
			FlounderProfiler.add("MainCamera", "Angle Around MainPlayer", angleAroundPlayer);
			FlounderProfiler.add("MainCamera", "Target Elevation", targetElevation);
			FlounderProfiler.add("MainCamera", "Target Rotation Angle", targetRotationAngle);
		}
	}

	private void calculateHorizontalAngle(final boolean gamePaused) {
		float delta = FlounderEngine.getDelta();
		float angleChange = 0.0f;

		if (!gamePaused && FlounderDevices.getMouse().getMouse(toggleMouseMoveKey)) {
			angleChange = FlounderDevices.getMouse().getDeltaX() * INFLUENCE_OF_MOUSEDX;
		} else if (!gamePaused && Math.abs(Maths.deadband(0.1f, joystickRotateX.getAmount())) > 0.0f) {
			angleChange = joystickRotateX.getAmount() * FlounderEngine.getDelta() * INFLUENCE_OF_JOYSTICKDX;
		}

		if (angleChange > MAX_HORIZONTAL_CHANGE * delta) {
			angleChange = MAX_HORIZONTAL_CHANGE * delta;
		} else if (angleChange < -MAX_HORIZONTAL_CHANGE * delta) {
			angleChange = -MAX_HORIZONTAL_CHANGE * delta;
		}

		targetRotationAngle -= angleChange;

		if (targetRotationAngle >= Maths.DEGREES_IN_HALF_CIRCLE) {
			targetRotationAngle -= Maths.DEGREES_IN_CIRCLE;
		} else if (targetRotationAngle <= -Maths.DEGREES_IN_HALF_CIRCLE) {
			targetRotationAngle += Maths.DEGREES_IN_CIRCLE;
		}
	}

	private void calculateVerticalAngle(final boolean gamePaused) {
		float delta = FlounderEngine.getDelta();
		float angleChange = 0.0f;

		if (!gamePaused && FlounderDevices.getMouse().getMouse(toggleMouseMoveKey)) {
			angleChange = -FlounderDevices.getMouse().getDeltaY() * INFLUENCE_OF_MOUSEDY;
		} else if (!gamePaused && Math.abs(Maths.deadband(0.1f, joystickRotateY.getAmount())) > 0.0f) {
			angleChange = joystickRotateY.getAmount() * FlounderEngine.getDelta() * INFLUENCE_OF_JOYSTICKDY;
		}

		if (angleChange > MAX_VERTICAL_CHANGE * delta) {
			angleChange = MAX_VERTICAL_CHANGE * delta;
		} else if (angleChange < -MAX_VERTICAL_CHANGE * delta) {
			angleChange = -MAX_VERTICAL_CHANGE * delta;
		}

		targetElevation -= angleChange;

		if (targetElevation >= MAX_ANGLE_OF_ELEVATION) {
			targetElevation = MAX_ANGLE_OF_ELEVATION;
		} else if (targetElevation <= 0) {
			targetElevation = 0;
		}
	}

	private void updateHorizontalAngle() {
		float offset = targetRotationAngle - angleAroundPlayer;

		if (Math.abs(offset) > Maths.DEGREES_IN_HALF_CIRCLE) {
			if (offset < 0) {
				offset = targetRotationAngle + Maths.DEGREES_IN_CIRCLE - angleAroundPlayer;
			} else {
				offset = targetRotationAngle - Maths.DEGREES_IN_CIRCLE - angleAroundPlayer;
			}
		}

		float change = offset * FlounderEngine.getDelta() * ROTATE_AGILITY;
		angleAroundPlayer += change;

		if (angleAroundPlayer >= Maths.DEGREES_IN_HALF_CIRCLE) {
			angleAroundPlayer -= Maths.DEGREES_IN_CIRCLE;
		} else if (angleAroundPlayer <= -Maths.DEGREES_IN_HALF_CIRCLE) {
			angleAroundPlayer += Maths.DEGREES_IN_CIRCLE;
		}
	}

	private void updatePitchAngle() {
		float offset = targetElevation - angleOfElevation;
		float change = offset * FlounderEngine.getDelta() * PITCH_AGILITY;
		angleOfElevation += change;
	}

	private void calculatePosition() {
		// float theta = angleAroundPlayer;
		position.x = targetPosition.x; //  - (float) (Math.sin(Math.toRadians(theta)))
		position.z = targetPosition.z; //  - (float) (Math.cos(Math.toRadians(theta)))
		position.y = targetPosition.y; //  + CAMERA_AIM_OFFSET

		rotation.x = (float) Math.toDegrees(angleOfElevation) - PITCH_OFFSET;
		rotation.y = Maths.DEGREES_IN_HALF_CIRCLE + angleAroundPlayer;
		rotation.z = 0.0f;
	}

	private void updateViewMatrix(final Vector3f position, final Vector3f rotation) {
		viewMatrix.setIdentity();
		position.negate();
		Matrix4f.rotate(viewMatrix, new Vector3f(1, 0, 0), (float) Math.toRadians(rotation.x), viewMatrix);
		Matrix4f.rotate(viewMatrix, new Vector3f(0, 1, 0), (float) Math.toRadians(-rotation.y), viewMatrix);
		Matrix4f.translate(viewMatrix, position, viewMatrix);
		position.negate();
		viewFrustum.recalculateFrustum(FlounderEngine.getProjectionMatrix(), getViewMatrix());
	}

	@Override
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	@Override
	public Frustum getViewFrustum() {
		return viewFrustum;
	}

	@Override
	public Matrix4f getReflectionViewMatrix(final float planeHeight) {
		return null;
	}

	@Override
	public void reflect(float waterHeight) {
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public float getPitch() {
		return rotation.x;
	}

	@Override
	public float getYaw() {
		return rotation.y;
	}

	@Override
	public float getRoll() {
		return rotation.z;
	}

	@Override
	public float getAimDistance() {
		return 0.0f;
	}
}
