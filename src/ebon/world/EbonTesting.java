package ebon.world;

import flounder.framework.*;
import flounder.inputs.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.physics.bounding.*;
import org.lwjgl.glfw.*;

public class EbonTesting {
	private ButtonAxis xAxis;
	private ButtonAxis yAxis;
	private ButtonAxis zAxis;
	private ButtonAxis wAxis;

	private TestAABB testAABB0;
	private TestAABB testAABB1;

	private Vector3f positionDelta;
	private Vector3f rotationDelta;

	public EbonTesting() {
		xAxis = new ButtonAxis(new KeyButton(GLFW.GLFW_KEY_D), new KeyButton(GLFW.GLFW_KEY_A));
		yAxis = new ButtonAxis(new KeyButton(GLFW.GLFW_KEY_LEFT_CONTROL), new KeyButton(GLFW.GLFW_KEY_LEFT_SHIFT));
		zAxis = new ButtonAxis(new KeyButton(GLFW.GLFW_KEY_S), new KeyButton(GLFW.GLFW_KEY_W));
		wAxis = new ButtonAxis(new KeyButton(GLFW.GLFW_KEY_Q), new KeyButton(GLFW.GLFW_KEY_E));
		testAABB0 = new TestAABB(new Vector3f(0.0f, 0.0f, 5.0f));
		testAABB1 = new TestAABB(new Vector3f(5.0f, 0.0f, 5.0f));

		positionDelta = new Vector3f();
		rotationDelta = new Vector3f();
	}

	public void update() {
		testAABB0.update();

		// Change = to += for a cool acceleration effect.
		positionDelta.x = FlounderFramework.getDelta() * xAxis.getAmount();
		positionDelta.y = FlounderFramework.getDelta() * yAxis.getAmount();
		positionDelta.z = FlounderFramework.getDelta() * zAxis.getAmount();
		Vector3f.add(testAABB1.position, positionDelta, testAABB1.position);

		rotationDelta.y = FlounderFramework.getDelta() * 180.0f * wAxis.getAmount();
		Vector3f.add(testAABB1.rotation, rotationDelta, testAABB1.rotation);

		testAABB1.update();
		IntersectData intersect = testAABB1.bounding.intersects(testAABB0.bounding);

		if (intersect.isIntersection()) {
			Vector3f.subtract(testAABB1.position, positionDelta, testAABB1.position);
			AABB.resolveCollision(testAABB1.bounding, testAABB0.bounding, positionDelta, positionDelta);
			Vector3f.add(testAABB1.position, positionDelta, testAABB1.position);
			testAABB1.update();
		}

		// TODO: Resolve collisions between: AABB-AABB, AABB-Sphere, Sphere-Sphere, Hull-AABB, Hull-Sphere.
	}

	private static class TestAABB {
		AABB original;
		AABB bounding;

		Vector3f position;
		Vector3f rotation;
		float scale;

		TestAABB(Vector3f position) {
			//	this.original = new Sphere(1.0f);
			//	this.bounding = new Sphere(1.0f);
			this.original = new AABB(new Vector3f(-1.0f, -1.0f, -1.0f), new Vector3f(1.0f, 1.0f, 1.0f));
			this.bounding = new AABB();

			this.position = position;
			this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
			this.scale = 1.0f;
		}

		void update() {
			//	Sphere.recalculate(this.original, this.position, this.scale, this.bounding);
			AABB.recalculate(this.original, this.position, this.rotation, this.scale, this.bounding);
			FlounderBounding.addShapeRender(this.bounding);
		}
	}
}
