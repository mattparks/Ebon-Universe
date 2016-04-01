import flounder.engine.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.space.*;

public class MainCamera implements ICamera {
	private static final float ZOOM_AGILITY = 8f;
	private static final float ROTATE_AGILITY = 6f;
	private static final float PITCH_AGILITY = 8f;
	private static final float FIELD_OF_VIEW = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 3200f;
	private final static float CAMERA_AIM_OFFSET = 10.0f;
	private final static float INFLUENCE_OF_MOUSEDY = -7200.0f;
	private final static float INFLUENCE_OF_MOUSEDX = INFLUENCE_OF_MOUSEDY * 92.0f;
	private final static float INFLUENCE_OF_MOUSE_WHEEL = 12.5f;
	private final static float MAX_ANGLE_OF_ELEVATION = 1.5f;
	private final static float PITCH_OFFSET = 3f;
	private final static float MINIMUM_ZOOM = 8;
	private final static float MAXIMUM_ZOOM = 300;
	private final static float MAX_HORIZONTAL_CHANGE = 500;
	private final static float MAX_VERTICAL_CHANGE = 5;
	private static final float NORMAL_ZOOM = 32f;

	private Vector3f m_position;
	private Vector3f m_rotation;
	private Frustum m_frustum;
	private Matrix4f m_viewMatrix;
	private boolean m_gamePaused;

	private float m_pitch;
	private float m_yaw;

	private float m_actualDistanceFromPoint;
	private float m_targetZoom = m_actualDistanceFromPoint;

	public MainCamera() {
		m_position = new Vector3f(0, 0, 0);
		m_rotation = new Vector3f(0, 0, 0);
		m_frustum = new Frustum();
		m_viewMatrix = new Matrix4f();
		m_gamePaused = true;
		m_pitch = 0.0f;
		m_yaw = 0.0f;

		m_actualDistanceFromPoint = NORMAL_ZOOM;
		m_targetZoom = m_actualDistanceFromPoint;

		calculateDistances();
	}

	private void calculateDistances() {

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
		m_position.set(focusPosition);
		m_rotation.set(focusRotation);
		m_gamePaused = gamePaused;
		updateViewMatrix(m_viewMatrix, m_position, m_pitch, m_yaw);
	}

	private void updateViewMatrix(Matrix4f viewMatrix, Vector3f position, float pitch, float yaw) {
		viewMatrix.setIdentity();
		Vector3f cameraPos = new Vector3f(-position.x, -position.y, -position.z);
		Matrix4f.rotate(viewMatrix, new Vector3f(1, 0, 0), (float) Math.toRadians(pitch), viewMatrix);
		Matrix4f.rotate(viewMatrix, new Vector3f(0, 1, 0), (float) Math.toRadians(-yaw), viewMatrix);
		Matrix4f.translate(viewMatrix, cameraPos, viewMatrix);

		m_frustum.recalculateFrustum(FlounderEngine.getProjectionMatrix(), viewMatrix);
	}

	@Override
	public Matrix4f getViewMatrix() {
		return m_viewMatrix;
	}

	@Override
	public Frustum getViewFrustum() {
		return m_frustum;
	}

	@Override
	public Matrix4f getReflectionViewMatrix(float planeHeight) {
		Matrix4f reflectionMatrix = new Matrix4f();
		Vector3f camPosition = new Vector3f(m_position);
		camPosition.y -= 2 * (m_position.y - planeHeight);
		float reflectedPitch = -m_pitch;
		updateViewMatrix(reflectionMatrix, camPosition, reflectedPitch, m_yaw);
		return reflectionMatrix;
	}

	@Override
	public void reflect(float waterHeight) {
		m_position.y -= 2 * (m_position.y - waterHeight);
		m_pitch = -m_pitch;
		updateViewMatrix(m_viewMatrix, m_position, m_pitch, m_yaw);
	}

	@Override
	public Vector3f getPosition() {
		return m_position;
	}

	@Override
	public float getPitch() {
		return m_pitch;
	}

	@Override
	public float getYaw() {
		return m_yaw;
	}

	@Override
	public float getAimDistance() {
		return 0.0f;
	}
}
