package game.waters;

import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;
import game.*;

/**
 * Represents a water tile in the world.
 */
public class Water implements ISpatialObject {
	public static final float[] VERTICES = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
	public static final int VAO = FlounderEngine.getLoader().createInterleavedVAO(VERTICES, 2);

	public Vector3f colourAdditive = new Vector3f(0.0f, 0.3f, 0.5f);
	public float colourMix = 0.30f;
	public float textureTiling = 4.0f;
	public float waveStrength = 0.02f;
	public float normalDampener = 3.0f;
	public float dropOffDepth = 7.0f;
	public float reflectivity = 0.50f;
	public float shineDamper = 20.0f;

	private Vector3f position;
	private Vector3f rotation;
	private AABB aabb;

	/**
	 * Creates a new water quad in the world.
	 *
	 * @param x The x position in the world where this water is.
	 * @param z The z position in the world where this water is.
	 */
	public Water(float x, float z) {
		position = new Vector3f(x, Environment.WATER_Y_POS, z);
		rotation = new Vector3f(0, 0, 0); // TODO: Rotation!
		aabb = new AABB(
				new Vector3f(getPosition().getX() - Environment.WATER_SIZE, getPosition().getY() - Environment.WATER_SIZE, getPosition().getZ() - Environment.WATER_SIZE),
				new Vector3f(getPosition().getX() + Environment.WATER_SIZE, getPosition().getY() + Environment.WATER_SIZE, getPosition().getZ() + Environment.WATER_SIZE)
		);
	}

	/**
	 * Gets the height of the water from a world coordinate.
	 *
	 * @param worldX World coordinate in the X.
	 * @param worldZ World coordinate in the Z.
	 *
	 * @return Returns the height at that spot.
	 */
	public float getHeightWorld(float worldX, float worldZ) {
		if (worldX > position.getX() || worldX < position.getX() || worldZ > position.getZ() || worldX < position.getZ()) {
			return 0;
		}

		return position.getY();
	}

	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public AABB getAABB() {
		return aabb;
	}

	public Vector3f getPosition() {
		return position;
	}
}
