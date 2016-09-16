package game.celestial.dust;

import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;

public class Dust implements Comparable<Dust>, ISpatialObject {
	private static final Vector3f VECTOR_REUSABLE_1 = new Vector3f();
	private static final Vector3f VECTOR_REUSABLE_2 = new Vector3f();

	private float tileSpanSize;
	private Vector3f position;
	private float starCount;
	private AABB dustAABB;

	public Dust(float tileSpanSize, Vector3f position, float starCount) {
		this.tileSpanSize = tileSpanSize;
		this.position = position;
		this.starCount = starCount;

		this.dustAABB = new AABB();
		float size = 0.5f * tileSpanSize;
		dustAABB.getMinExtents().set(this.position.getX() - size, this.position.getY() - size, this.position.getZ() - size);
		dustAABB.getMaxExtents().set(this.position.getX() + size, this.position.getY() + size, this.position.getZ() + size);

		//	FlounderEngine.getLogger().error(getStarDensity());
	}

	public float getStarDensity() {
		double width = dustAABB.getWidth();
		double height = dustAABB.getHeight();
		double depth = dustAABB.getDepth();
		return (float) (width * height * depth) / starCount;
	}

	@Override
	public AABB getAABB() {
		return dustAABB;
	}

	@Override
	public int compareTo(Dust o) {
		if (FlounderEngine.getCamera().getPosition() == null) {
			return ((Float) position.lengthSquared()).compareTo(o.position.lengthSquared());
		}

		return ((Float) Vector3f.subtract(FlounderEngine.getCamera().getPosition(), position, VECTOR_REUSABLE_1).lengthSquared()).compareTo(Vector3f.subtract(FlounderEngine.getCamera().getPosition(), o.position, VECTOR_REUSABLE_2).lengthSquared());
	}
}
