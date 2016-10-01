package editor;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.entrance.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import flounder.space.*;
import sun.reflect.generics.reflectiveObjects.*;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCamera implements ICamera {
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 3200.0f;
	private static final float FIELD_OF_VIEW = 90.0f;

	private static final float ZOOM_AGILITY = 8.0f;
	private static final float ROTATE_AGILITY = 6.0f;
	private static final float PITCH_AGILITY = 8.0f;

	private final static float CAMERA_AIM_OFFSET = 0.0f;
	private final static float MAX_ANGLE_OF_ELEVATION = 1.5f;
	private final static float PITCH_OFFSET = 3.0f;
	private final static float MINIMUM_ZOOM = -300.0f;
	private final static float MAXIMUM_ZOOM = 300.0f;
	private static final float NORMAL_ZOOM = 2.0f;

	private final static float MAX_HORIZONTAL_CHANGE = 500.0f;
	private final static float MAX_VERTICAL_CHANGE = 5.0f;

	private final static float INFLUENCE_OF_MOUSEDY = -175.0f;
	private final static float INFLUENCE_OF_MOUSEDX = INFLUENCE_OF_MOUSEDY * 92.0f;
	private final static float INFLUENCE_OF_JOYSTICKDY = -1.0f;
	private final static float INFLUENCE_OF_JOYSTICKDX = 100.0f * INFLUENCE_OF_JOYSTICKDY;
	private final static float INFLUENCE_OF_MOUSE_WHEEL = 12.5f;

	private static int toggleMouseMoveKey;

	private Frustum viewFrustum;
	private Matrix4f viewMatrix;

	private Vector3f position;
	private Vector3f rotation;

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
		this.viewFrustum = new Frustum();
		this.viewMatrix = new Matrix4f();

		this.position = new Vector3f();
		this.rotation = new Vector3f();

		this.toggleMouseMoveKey = GLFW_MOUSE_BUTTON_LEFT;

		this.angleOfElevation = 0.0f;
		this.angleAroundPlayer = 0.0f;

		this.targetPosition = new Vector3f();
		this.targetRotation = new Vector3f();
		this.targetZoom = NORMAL_ZOOM;
		this.targetElevation = 0.0f;
		this.targetRotationAngle = 0.0f;

		this.actualDistanceFromPoint = NORMAL_ZOOM;
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

		if (FlounderProfiler.isOpen()) {
			FlounderProfiler.add("MainCamera", "Angle Of Elevation", angleOfElevation);
			FlounderProfiler.add("MainCamera", "Rotation", rotation);
			FlounderProfiler.add("MainCamera", "Angle Around MainPlayer", angleAroundPlayer);
			FlounderProfiler.add("MainCamera", "Actual Distance From Point", actualDistanceFromPoint);
			FlounderProfiler.add("MainCamera", "Target Zoom", targetZoom);
			FlounderProfiler.add("MainCamera", "Target Elevation", targetElevation);
			FlounderProfiler.add("MainCamera", "Target Rotation Angle", targetRotationAngle);
		}
	}

	private void calculateHorizontalAngle(final boolean gamePaused) {
		float delta = FlounderEngine.getDelta();
		float angleChange = 0.0f;

		if (!gamePaused && FlounderMouse.getMouse(toggleMouseMoveKey)) {
			angleChange = FlounderMouse.getDeltaX() * INFLUENCE_OF_MOUSEDX;
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

		if (!gamePaused && FlounderMouse.getMouse(toggleMouseMoveKey)) {
			angleChange = -FlounderMouse.getDeltaY() * INFLUENCE_OF_MOUSEDY;
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

		if (!gamePaused && Math.abs(Maths.deadband(0.1f, FlounderMouse.getDeltaWheel())) > 0.0f) {
			zoomLevel = FlounderMouse.getDeltaWheel();
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

		rotation.y = targetRotation.y + Maths.DEGREES_IN_HALF_CIRCLE + angleAroundPlayer;
		rotation.x = (float) Math.toDegrees(angleOfElevation) - PITCH_OFFSET;
	}

	private void updateViewMatrix() {
		viewMatrix.setIdentity();
		position.negate();
		Matrix4f.rotate(viewMatrix, new Vector3f(1, 0, 0), (float) Math.toRadians(rotation.x), viewMatrix);
		Matrix4f.rotate(viewMatrix, new Vector3f(0, 1, 0), (float) Math.toRadians(-rotation.y), viewMatrix);
		Matrix4f.rotate(viewMatrix, new Vector3f(0, 0, 1), (float) Math.toRadians(rotation.z), viewMatrix);
		Matrix4f.translate(viewMatrix, position, viewMatrix);
		position.negate();
		viewFrustum.recalculateFrustum(FlounderEngine.getProjectionMatrix(), viewMatrix);
	}

	@Override
	public Matrix4f getViewMatrix() {
		updateViewMatrix();
		return viewMatrix;
	}

	@Override
	public Frustum getViewFrustum() {
		return viewFrustum;
	}

	@Override
	public Matrix4f getReflectionViewMatrix(final float planeHeight) {
		throw new NotImplementedException();
	}

	@Override
	public void reflect(float waterHeight) {
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(Vector3f rotation) {
		this.rotation.set(rotation);
	}

	@Override
	public float getAimDistance() {
		throw new NotImplementedException();
	}

	private void calculateDistances() {
		horizontalDistanceFromFocus = (float) (actualDistanceFromPoint * Math.cos(angleOfElevation));
		verticalDistanceFromFocus = (float) (actualDistanceFromPoint * Math.sin(angleOfElevation));
	}
}