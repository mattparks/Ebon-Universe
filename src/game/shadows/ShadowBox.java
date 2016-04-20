package game.shadows;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;

/**
 * Represents the 3D area of the world in which engine.shadows will be cast (basically represents the orthographic projection area for the shadow renderObjects pass). It can be updated each frame to optimise the area, making it as small as possible (to allow for optimal shadow map resolution) while not being too small to avoid objects not having engine.shadows when they should. This class also provides functionality to test whether an object is inside this shadow box. Everything inside the box will be rendered to the shadow map in the shadow renderObjects pass.
 */
public class ShadowBox {
	private static final float OFFSET = 2.0f;
	private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
	private static final Vector4f FORWARD = new Vector4f(0, 0, -1, 0);

	private Matrix4f lightViewMatrix;

	private float minX, maxX;
	private float minY, maxY;
	private float minZ, maxZ;

	private float shadowDistance;

	private float farHeight, farWidth, nearHeight, nearWidth;

	/**
	 * Creates a new shadow box and calculates some initial values relating to the camera's view frustum.
	 *
	 * @param lightViewMatrix Basically the "view matrix" of the light. Can be used to transform a point from world space into "light" space.
	 */
	protected ShadowBox(Matrix4f lightViewMatrix) {
		this.lightViewMatrix = lightViewMatrix;
	}

	/**
	 * Test if a bounding sphere intersects the shadow box. Can be used to decide which engine.entities should be rendered in the shadow renderObjects pass.
	 *
	 * @param position The center of the bounding sphere in world space.
	 * @param radius The radius of the bounding sphere.
	 *
	 * @return {@code true} if the sphere intersects the box.
	 */
	public boolean isInBox(Vector3f position, float radius) { // Unused
		Vector4f entityPos = Matrix4f.transform(lightViewMatrix, new Vector4f(position.getX(), position.getY(), position.getZ(), 1f), null);
		float closestX = (float) Maths.clamp(entityPos.x, minX, maxX);
		float closestY = (float) Maths.clamp(entityPos.y, minY, maxY);
		float closestZ = (float) Maths.clamp(entityPos.z, minZ, maxZ);
		Vector3f closestPoint = new Vector3f(closestX, closestY, closestZ);
		Vector3f center = new Vector3f(entityPos.x, entityPos.y, entityPos.z);
		float disSquared = Vector3f.subtract(center, closestPoint, null).lengthSquared();
		return disSquared < radius * radius;
	}

	/**
	 * Updates the bounds of the shadow box based on the light direction and the camera's view frustum, to make sure that the box covers the smallest area possible while still ensuring that everything inside the camera's view (and in range) will be shadowed.
	 *
	 * @param camera The camera object to be used when calculating the shadow boxes size.
	 */
	protected void update(final ICamera camera) {
		updateShadowDistance(camera);
		updateWidthsAndHeights(camera);

		Matrix4f rotation = calculateCameraRotationMatrix(camera);
		Vector3f forwardVector = new Vector3f(Matrix4f.transform(rotation, FORWARD, null));
		Vector3f toFar = new Vector3f(forwardVector);
		toFar.scale(shadowDistance);
		Vector3f toNear = new Vector3f(forwardVector);
		toNear.scale(camera.getNearPlane());
		Vector3f centerNear = Vector3f.add(toNear, camera.getPosition(), null);
		Vector3f centerFar = Vector3f.add(toFar, camera.getPosition(), null);

		Vector4f[] points = calculateFrustumVertices(rotation, forwardVector, centerNear, centerFar);

		boolean first = true;

		for (Vector4f point : points) {
			if (first) {
				minX = point.x;
				maxX = point.x;
				minY = point.y;
				maxY = point.y;
				minZ = point.z;
				maxZ = point.z;
				first = false;
				continue;
			}

			if (point.x > maxX) {
				maxX = point.x;
			} else if (point.x < minX) {
				minX = point.x;
			}

			if (point.y > maxY) {
				maxY = point.y;
			} else if (point.y < minY) {
				minY = point.y;
			}

			if (point.z > maxZ) {
				maxZ = point.z;
			} else if (point.z < minZ) {
				minZ = point.z;
			}
		}

		maxZ += OFFSET;
	}

	private void updateShadowDistance(ICamera camera) {
		shadowDistance = camera.getAimDistance() * 2.0f;
	}

	private void updateWidthsAndHeights(ICamera camera) {
		farWidth = (float) (shadowDistance * Math.tan(Math.toRadians(camera.getFOV())));
		nearWidth = (float) (camera.getNearPlane() * Math.tan(Math.toRadians(camera.getFOV())));
		farHeight = farWidth / ManagerDevices.getDisplay().getAspectRatio();
		nearHeight = nearWidth / ManagerDevices.getDisplay().getAspectRatio();
	}

	/**
	 * @return The rotation of the camera represented as a matrix.
	 */
	private Matrix4f calculateCameraRotationMatrix(ICamera camera) {
		Matrix4f rotation = new Matrix4f();
		Matrix4f.rotate(rotation, new Vector3f(0, 1, 0), (float) Math.toRadians(camera.getYaw()), rotation);
		Matrix4f.rotate(rotation, new Vector3f(1, 0, 0), (float) Math.toRadians(-camera.getPitch()), rotation);
		return rotation;
	}

	/**
	 * Calculates the vertex of each corner of the view frustum in light space.
	 *
	 * @param rotation - camera's rotation.
	 * @param forwardVector - the direction that the camera is aiming, and thus the direction of the frustum.
	 * @param centerNear - the center point of the frustum's near plane.
	 * @param centerFar - the center point of the frustum's far plane.
	 *
	 * @return The vertices of the frustum in light space.
	 */
	private Vector4f[] calculateFrustumVertices(Matrix4f rotation, Vector3f forwardVector, Vector3f centerNear, Vector3f centerFar) {
		Vector3f upVector = new Vector3f(Matrix4f.transform(rotation, UP, null));
		Vector3f rightVector = Vector3f.cross(forwardVector, upVector, null);
		Vector3f downVector = new Vector3f(-upVector.x, -upVector.y, -upVector.z);
		Vector3f leftVector = new Vector3f(-rightVector.x, -rightVector.y, -rightVector.z);
		Vector3f farTop = Vector3f.add(centerFar, new Vector3f(upVector.x * farHeight, upVector.y * farHeight, upVector.z * farHeight), null);
		Vector3f farBottom = Vector3f.add(centerFar, new Vector3f(downVector.x * farHeight, downVector.y * farHeight, downVector.z * farHeight), null);
		Vector3f nearTop = Vector3f.add(centerNear, new Vector3f(upVector.x * nearHeight, upVector.y * nearHeight, upVector.z * nearHeight), null);
		Vector3f nearBottom = Vector3f.add(centerNear, new Vector3f(downVector.x * nearHeight, downVector.y * nearHeight, downVector.z * nearHeight), null);
		Vector4f[] points = new Vector4f[8];
		points[0] = calculateLightSpaceFrustumCorner(farTop, rightVector, farWidth);
		points[1] = calculateLightSpaceFrustumCorner(farTop, leftVector, farWidth);
		points[2] = calculateLightSpaceFrustumCorner(farBottom, rightVector, farWidth);
		points[3] = calculateLightSpaceFrustumCorner(farBottom, leftVector, farWidth);
		points[4] = calculateLightSpaceFrustumCorner(nearTop, rightVector, nearWidth);
		points[5] = calculateLightSpaceFrustumCorner(nearTop, leftVector, nearWidth);
		points[6] = calculateLightSpaceFrustumCorner(nearBottom, rightVector, nearWidth);
		points[7] = calculateLightSpaceFrustumCorner(nearBottom, leftVector, nearWidth);
		return points;
	}

	/**
	 * Calculates one of the corner vertices of the view frustum in world space and converts it to light space.
	 *
	 * @param startPoint The starting center point on the view frustum.
	 * @param direction The direction of the corner from the start point.
	 * @param width The distance of the corner from the start point.
	 *
	 * @return The relevant corner vertex of the view frustum in light space.
	 */
	private Vector4f calculateLightSpaceFrustumCorner(Vector3f startPoint, Vector3f direction, float width) {
		Vector3f point = Vector3f.add(startPoint, new Vector3f(direction.x * width, direction.y * width, direction.z * width), null);
		Vector4f point4f = new Vector4f(point.x, point.y, point.z, 1f);
		Matrix4f.transform(lightViewMatrix, point4f, point4f);
		return point4f;
	}

	/**
	 * @return The center of the shadow box (orthographic projection area).
	 */
	protected Vector3f getCenter() {
		float x = (minX + maxX) / 2f;
		float y = (minY + maxY) / 2f;
		float z = (minZ + maxZ) / 2f;
		Vector4f cen = new Vector4f(x, y, z, 1);
		Matrix4f invertedLight = new Matrix4f();
		Matrix4f.invert(lightViewMatrix, invertedLight);
		return new Vector3f(Matrix4f.transform(invertedLight, cen, null));
	}

	/**
	 * @return The width of the shadow box (orthographic projection area).
	 */
	protected float getWidth() {
		return maxX - minX;
	}

	/**
	 * @return The height of the shadow box (orthographic projection area).
	 */
	protected float getHeight() {
		return maxY - minY;
	}

	/**
	 * @return The length of the shadow box (orthographic projection area).
	 */
	protected float getLength() {
		return maxZ - minZ;
	}

	protected float getShadowDistance() {
		return shadowDistance;
	}
}
