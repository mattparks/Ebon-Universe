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
	private static final float ZOOM_AGILITY = 8f;
	private static final float ROTATE_AGILITY = 6f;
	private static final float PITCH_AGILITY = 8f;
	private static final float FIELD_OF_VIEW = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 3200f;
	private final static float CAMERA_AIM_OFFSET = 0.0f; // 10.0f;
	private final static float INFLUENCE_OF_MOUSEDY = -1000.0f;
	private final static float INFLUENCE_OF_MOUSEDX = INFLUENCE_OF_MOUSEDY * 92.0f;
	private final static float INFLUENCE_OF_JOYSTICKDY = -1.0f;
	private final static float INFLUENCE_OF_JOYSTICKDX = 100.0f * INFLUENCE_OF_JOYSTICKDY;
	private final static float INFLUENCE_OF_MOUSE_WHEEL = 12.5f;
	private final static float MAX_ANGLE_OF_ELEVATION = 1.5f;
	private final static float PITCH_OFFSET = 3f;
	private final static float MINIMUM_ZOOM = 8;
	private final static float MAXIMUM_ZOOM = 300;
	private final static float MAX_HORIZONTAL_CHANGE = 500;
	private final static float MAX_VERTICAL_CHANGE = 5;
	private static final float NORMAL_ZOOM = 32f;

	public static int toggleMouseMoveKey;
	private JoystickAxis joystickRotateX;
	private JoystickAxis joystickRotateY;
	private JoystickButton joystickZoomIn;
	private JoystickButton joystickZoomOut;

	private Matrix4f viewMatrix;
	private Vector3f position;

	private float horizontalDistanceFromFocus;
	private float verticalDistanceFromFocus;
	private float pitch;
	private float yaw;

	private Frustum viewFrustum;

	private Vector3f targetPosition;
	private Vector3f targetRotation;
	private float actualDistanceFromPoint;
	private float targetZoom = actualDistanceFromPoint;
	private float targetElevation;
	private float angleOfElevation;
	private float targetRotationAngle;
	private float angleAroundPlayer;

	public MainCamera() {
		toggleMouseMoveKey = GLFW_MOUSE_BUTTON_LEFT;
		joystickRotateX = new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_X);
		joystickRotateY = new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_Y);
		joystickZoomIn = new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_ZOOM_IN);
		joystickZoomOut = new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_ZOOM_OUT);

		viewMatrix = new Matrix4f();
		position = new Vector3f();
		viewFrustum = new Frustum();
		targetPosition = new Vector3f(0, 0, 0);
		targetRotation = new Vector3f(0, 0, 0);
		actualDistanceFromPoint = NORMAL_ZOOM;
		targetZoom = actualDistanceFromPoint;
		targetElevation = 0.37f;
		angleOfElevation = targetElevation;
		targetRotationAngle = 0;
		angleAroundPlayer = targetRotationAngle;
		FlounderProfiler.addTab("Camera");

		calculateDistances();
	}

	private void calculateDistances() {
		horizontalDistanceFromFocus = (float) (actualDistanceFromPoint * Math.cos(angleOfElevation));
		verticalDistanceFromFocus = (float) (actualDistanceFromPoint * Math.sin(angleOfElevation));
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
	public void moveCamera(Vector3f focusPosition, Vector3f focusRotation, boolean gamePaused) {
		calculateHorizontalAngle(gamePaused);
		calculateVerticalAngle(gamePaused);
		calculateZoom(gamePaused);

		targetPosition.set(focusPosition);
		targetRotation.set(focusRotation);

		updateActualZoom();
		updateHorizontalAngle();
		updatePitchAngle();
		calculateDistances();
		calculatePosition();
		updateViewMatrix(position, pitch, yaw);

		if (FlounderProfiler.isOpen()) {
			FlounderProfiler.add("Camera", "Angle Of Elevation", angleOfElevation);
			FlounderProfiler.add("Camera", "Yaw", yaw);
			FlounderProfiler.add("Camera", "Pitch", pitch);
			FlounderProfiler.add("Camera", "Angle Around Player", angleAroundPlayer);
			FlounderProfiler.add("Camera", "Actual Distance From Point", actualDistanceFromPoint);
			FlounderProfiler.add("Camera", "Target Zoom", targetZoom);
			FlounderProfiler.add("Camera", "Target Elevation", targetElevation);
			FlounderProfiler.add("Camera", "Target Rotation Angle", targetRotationAngle);
		}
	}

	private void updateViewMatrix(Vector3f position, float pitch, float yaw) {
		viewMatrix.setIdentity();
		Vector3f cameraPos = new Vector3f(-position.x, -position.y, -position.z);
		Matrix4f.rotate(viewMatrix, new Vector3f(1, 0, 0), (float) Math.toRadians(pitch), viewMatrix);
		Matrix4f.rotate(viewMatrix, new Vector3f(0, 1, 0), (float) Math.toRadians(-yaw), viewMatrix);
		Matrix4f.translate(viewMatrix, cameraPos, viewMatrix);
		viewFrustum.recalculateFrustum(FlounderEngine.getProjectionMatrix(), getViewMatrix());
	}

	private void calculateHorizontalAngle(boolean gamePaused) {
		float delta = FlounderEngine.getDelta();
		float angleChange = 0; // Maths.normalizeAngle(yaw) - Maths.normalizeAngle(entity.getRotation().getY())

		if (!gamePaused && ManagerDevices.getMouse().getMouse(toggleMouseMoveKey)) { // && !DeviceInput.isActiveInGUI()
			angleChange = ManagerDevices.getMouse().getDeltaX() * INFLUENCE_OF_MOUSEDX;
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

	private void calculateVerticalAngle(boolean gamePaused) {
		float delta = FlounderEngine.getDelta();
		float angleChange = 0;

		if (!gamePaused && ManagerDevices.getMouse().getMouse(toggleMouseMoveKey)) { // && !DeviceMouse.isActiveInGUI()
			angleChange = -ManagerDevices.getMouse().getDeltaY() * INFLUENCE_OF_MOUSEDY;
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

	private void calculateZoom(boolean gamePaused) {
		float zoomLevel = 0.0f;

		if (!gamePaused && Math.abs(Maths.deadband(0.1f, ManagerDevices.getMouse().getDeltaWheel())) > 0.0f) {
			zoomLevel = ManagerDevices.getMouse().getDeltaWheel();
		} else if (!gamePaused && joystickZoomIn.isDown()) {
			zoomLevel = 1.0f;
		} else if (!gamePaused && joystickZoomOut.isDown()) {
			zoomLevel = -1.0f;
		}

		zoomLevel = zoomLevel / INFLUENCE_OF_MOUSE_WHEEL;

		if (zoomLevel != 0) {
			targetZoom -= zoomLevel;

			if (targetZoom < MINIMUM_ZOOM) {
				targetZoom = MINIMUM_ZOOM;
			} else if (targetZoom > MAXIMUM_ZOOM) {
				targetZoom = MAXIMUM_ZOOM;
			}
		}
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

	@Override
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	@Override
	public Frustum getViewFrustum() {
		return viewFrustum;
	}

	@Override
	public Matrix4f getReflectionViewMatrix(float planeHeight) {
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
	public float getAimDistance() {
		return 0.0f;
	}
}
