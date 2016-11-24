package ebon.cameras;

import flounder.camera.*;
import flounder.devices.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.space.*;

public class CameraCubeMap implements ICamera {
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = (float) (2560) * 4.0f;
	private static final float FIELD_OF_VIEW = 90.0f;

	private Vector3f reusableViewVector;

	private Vector3f position;
	private Vector3f rotation;

	private Frustum viewFrustum;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;

	@Override
	public void init() {
		this.reusableViewVector = new Vector3f();

		this.position = new Vector3f();
		this.rotation = new Vector3f();

		this.viewFrustum = new Frustum();
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
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
	public void update(Vector3f focusPosition, Vector3f focusRotation) {
		// x=pitch, y=yaw, z=roll.
	}

	public void setCentre(Vector3f centre) {
		this.position.set(centre);
	}

	public void switchToFace(int faceIndex) {
		switch (faceIndex) {
			case 0:
				rotation.set(0.0f, 90.0f, 180.0f);
				break;
			case 1:
				rotation.set(0.0f, -90.0f, 180.0f);
				break;
			case 2:
				rotation.set(-90.0f, 180.0f, 180.0f);
				break;
			case 3:
				rotation.set(90.0f, 180.0f, 180.0f);
				break;
			case 4:
				rotation.set(0.0f, 180.0f, 180.0f);
				break;
			case 5:
				rotation.set(0.0f, 0.0f, 180.0f);
				break;
		}
	}

	private void updateViewMatrix() {
		viewMatrix.setIdentity();
		position.negate();
		Matrix4f.rotate(viewMatrix, reusableViewVector.set(1.0f, 0.0f, 0.0f), (float) Math.toRadians(rotation.x), viewMatrix);
		Matrix4f.rotate(viewMatrix, reusableViewVector.set(0.0f, 1.0f, 0.0f), (float) Math.toRadians(rotation.y), viewMatrix);
		Matrix4f.rotate(viewMatrix, reusableViewVector.set(0.0f, 0.0f, 1.0f), (float) Math.toRadians(rotation.z), viewMatrix);
		position.negate();
		Matrix4f.translate(viewMatrix, position, viewMatrix);
	}

	private void updateProjectionMatrix() {
		Matrix4f.perspectiveMatrix(FIELD_OF_VIEW, FlounderDisplay.getAspectRatio(), NEAR_PLANE, FAR_PLANE, projectionMatrix);
	}

	@Override
	public Frustum getViewFrustum() {
		return viewFrustum;
	}

	@Override
	public Matrix4f getViewMatrix() {
		updateViewMatrix();
		return viewMatrix;
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		updateProjectionMatrix();
		return projectionMatrix;
	}

	@Override
	public void reflect(float waterHeight) {
		position.y -= 2.0f * (position.y - waterHeight);
		rotation.x = -rotation.x;
		updateViewMatrix();
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
}
