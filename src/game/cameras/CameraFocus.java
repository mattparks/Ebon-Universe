package game.cameras;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.space.*;
import game.options.*;

import static org.lwjgl.glfw.GLFW.*;

public class CameraFocus implements ICamera {
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 3200.0f;
	private static final float FIELD_OF_VIEW = 70.0f;

	private static final float ZOOM_AGILITY = 8.0f;
	private static final float ROTATE_AGILITY = 6.0f;
	private static final float PITCH_AGILITY = 8.0f;

	private final static float CAMERA_AIM_OFFSET = 0.0f;
	private final static float MAX_ANGLE_OF_ELEVATION = 1.5f;
	private final static float PITCH_OFFSET = 3.0f;
	private final static float MINIMUM_ZOOM = -300.0f;
	private final static float MAXIMUM_ZOOM = 300.0f;
	private static final float NORMAL_ZOOM = 32.0f;

	private final static float MAX_HORIZONTAL_CHANGE = 500.0f;
	private final static float MAX_VERTICAL_CHANGE = 5.0f;

	private final static float INFLUENCE_OF_MOUSEDY = -1000.0f;
	private final static float INFLUENCE_OF_MOUSEDX = INFLUENCE_OF_MOUSEDY * 92.0f;
	private final static float INFLUENCE_OF_JOYSTICKDY = -1.0f;
	private final static float INFLUENCE_OF_JOYSTICKDX = 100.0f * INFLUENCE_OF_JOYSTICKDY;
	private final static float INFLUENCE_OF_MOUSE_WHEEL = 12.5f;
	private static int toggleMouseMoveKey;
	private Vector3f position;
	private Frustum viewFrustum;
	private Matrix4f viewMatrix;
	private float pitch, yaw;
	private JoystickAxis joystickRotateX;
	private JoystickAxis joystickRotateY;
	private JoystickButton joystickZoomIn;
	private JoystickButton joystickZoomOut;

	private float angleOfElevation;
	private float angleAroundPlayer;

	private Vector3f targetPosition;
	private Vector3f targetRotation;
	private float targetZoom;
	private float targetElevation;
	private float targetRotationAngle;

	private float actualDistanceFromPoint;
	private float horizontalDistanceFromFocus;
	private float verticalDistanceFromFocus;

	@Override
	public void init() {
		this.position = new Vector3f();
		this.viewFrustum = new Frustum();
		this.viewMatrix = new Matrix4f();
		this.pitch = 0.0f;
		this.yaw = 0.0f;

		this.toggleMouseMoveKey = GLFW_MOUSE_BUTTON_LEFT;
		this.joystickRotateX = new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_X);
		this.joystickRotateY = new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_Y);
		this.joystickZoomIn = new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_ZOOM_IN);
		this.joystickZoomOut = new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_ZOOM_OUT);

		this.angleOfElevation = 0.0f;
		this.angleAroundPlayer = 0.0f;

		this.targetPosition = new Vector3f();
		this.targetRotation = new Vector3f();
		this.targetZoom = 5.0f;
		this.targetElevation = 0.0f;
		this.targetRotationAngle = 0.0f;

		this.actualDistanceFromPoint = 5.0f;
		this.horizontalDistanceFromFocus = 0.0f;
		this.verticalDistanceFromFocus = 0.0f;

		calculateDistances();
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
	public void update(final Vector3f focusPosition, final Vector3f focusRotation, final boolean gamePaused) {
		calculateHorizontalAngle(gamePaused);
		calculateVerticalAngle(gamePaused);
		calculateZoom(gamePaused);

		this.targetPosition.set(focusPosition);
		this.targetRotation.set(focusRotation);

		updateActualZoom();
		updateHorizontalAngle();
		updatePitchAngle();
		calculateDistances();
		calculatePosition();
		updateViewMatrix(position, pitch, yaw);

		if (FlounderEngine.getProfiler().isOpen()) {
			FlounderEngine.getProfiler().add("MainCamera", "Angle Of Elevation", angleOfElevation);
			FlounderEngine.getProfiler().add("MainCamera", "Yaw", yaw);
			FlounderEngine.getProfiler().add("MainCamera", "Pitch", pitch);
			FlounderEngine.getProfiler().add("MainCamera", "Angle Around MainPlayer", angleAroundPlayer);
			FlounderEngine.getProfiler().add("MainCamera", "Actual Distance From Point", actualDistanceFromPoint);
			FlounderEngine.getProfiler().add("MainCamera", "Target Zoom", targetZoom);
			FlounderEngine.getProfiler().add("MainCamera", "Target Elevation", targetElevation);
			FlounderEngine.getProfiler().add("MainCamera", "Target Rotation Angle", targetRotationAngle);
		}
	}

	private void calculateHorizontalAngle(final boolean gamePaused) {
		float delta = FlounderEngine.getDelta();
		float angleChange = 0.0f;

		if (!gamePaused && FlounderEngine.getDevices().getMouse().getMouse(toggleMouseMoveKey)) {
			angleChange = FlounderEngine.getDevices().getMouse().getDeltaX() * INFLUENCE_OF_MOUSEDX;
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

		if (!gamePaused && FlounderEngine.getDevices().getMouse().getMouse(toggleMouseMoveKey)) {
			angleChange = -FlounderEngine.getDevices().getMouse().getDeltaY() * INFLUENCE_OF_MOUSEDY;
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

	private void calculateZoom(final boolean gamePaused) {
		float zoomLevel = 0.0f;

		if (!gamePaused && Math.abs(Maths.deadband(0.1f, FlounderEngine.getDevices().getMouse().getDeltaWheel())) > 0.0f) {
			zoomLevel = FlounderEngine.getDevices().getMouse().getDeltaWheel();
		} else if (!gamePaused && joystickZoomIn.isDown()) {
			zoomLevel = 1.0f;
		} else if (!gamePaused && joystickZoomOut.isDown()) {
			zoomLevel = -1.0f;
		}

		zoomLevel = zoomLevel / INFLUENCE_OF_MOUSE_WHEEL;

		// if (zoomLevel != 0) {
		targetZoom -= zoomLevel;

		if (targetZoom < MINIMUM_ZOOM) {
			targetZoom = MINIMUM_ZOOM;
		} else if (targetZoom > MAXIMUM_ZOOM) {
			targetZoom = MAXIMUM_ZOOM;
		}
		// }
	}

	private void updateActualZoom() {
		float offset = targetZoom - actualDistanceFromPoint;
		float change = offset * FlounderEngine.getDelta() * ZOOM_AGILITY;
		actualDistanceFromPoint += change;
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
		float theta = targetRotation.y + angleAroundPlayer;
		position.x = targetPosition.x - (float) (horizontalDistanceFromFocus * Math.sin(Math.toRadians(theta)));
		position.z = targetPosition.z - (float) (horizontalDistanceFromFocus * Math.cos(Math.toRadians(theta)));
		position.y = targetPosition.y + (verticalDistanceFromFocus + CAMERA_AIM_OFFSET);

		yaw = targetRotation.y + Maths.DEGREES_IN_HALF_CIRCLE + angleAroundPlayer;
		pitch = (float) Math.toDegrees(angleOfElevation) - PITCH_OFFSET;
	}

	private void updateViewMatrix(final Vector3f position, final float pitch, final float yaw) {
		viewMatrix.setIdentity();
		position.negate();
		Matrix4f.rotate(viewMatrix, new Vector3f(1, 0, 0), (float) Math.toRadians(pitch), viewMatrix);
		Matrix4f.rotate(viewMatrix, new Vector3f(0, 1, 0), (float) Math.toRadians(-yaw), viewMatrix);
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
		return pitch;
	}

	@Override
	public float getYaw() {
		return yaw;
	}

	@Override
	public float getRoll() {
		return 0;
	}

	@Override
	public float getAimDistance() {
		return 0.0f;
	}

	private void calculateDistances() {
		horizontalDistanceFromFocus = (float) (actualDistanceFromPoint * Math.cos(angleOfElevation));
		verticalDistanceFromFocus = (float) (actualDistanceFromPoint * Math.sin(angleOfElevation));
	}
}
