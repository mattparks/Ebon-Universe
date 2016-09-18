package game.celestial.dust;

import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;

public class Dust implements Comparable<Dust>, ISpatialObject {
	private static final Vector3f VECTOR_REUSABLE_1 = new Vector3f();
	private static final Vector3f VECTOR_REUSABLE_2 = new Vector3f();

	private float tileSpanSize;
	private Vector3f position;
	private float starCount;
	private Colour averageColour;
	private AABB dustBounding;

	public Dust(Vector3f minPosition, Vector3f maxPosition, float starCount, Colour averageColour) {
		this.tileSpanSize = Vector3f.subtract(maxPosition, minPosition, maxPosition).length();
		this.position = Vector3f.divide(Vector3f.subtract(minPosition, maxPosition, this.position), new Vector3f(2.0f, 2.0f, 2.0f), this.position);
		this.starCount = starCount;
		this.averageColour = averageColour;

		this.dustBounding = new AABB(new Vector3f(minPosition), new Vector3f(maxPosition));
	}

	public float getStarDensity() {
		double width = dustBounding.getWidth();
		double height = dustBounding.getHeight();
		double depth = dustBounding.getDepth();
		return (float) (width * height * depth) / starCount;
	}

	public float getRadius() {
		return tileSpanSize;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Colour getAverageColour() {
		return averageColour;
	}

	@Override
	public AABB getBounding() {
		return dustBounding;
	}

	@Override
	public int compareTo(Dust o) {
		if (FlounderEngine.getCamera().getPosition() == null) {
			return ((Float) position.lengthSquared()).compareTo(o.position.lengthSquared());
		}

		return ((Float) Vector3f.subtract(FlounderEngine.getCamera().getPosition(), position, VECTOR_REUSABLE_1).lengthSquared()).compareTo(Vector3f.subtract(FlounderEngine.getCamera().getPosition(), o.position, VECTOR_REUSABLE_2).lengthSquared());
	}
}
