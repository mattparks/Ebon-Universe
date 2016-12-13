package testing;

import flounder.framework.*;
import flounder.inputs.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import org.lwjgl.glfw.*;

public class TestingBoundings extends IModule {
	private static final TestingBoundings instance = new TestingBoundings();

	private ButtonAxis xAxis;
	private ButtonAxis yAxis;
	private ButtonAxis zAxis;
	private ButtonAxis wAxis;

	private TestAABB testAABB0;
	private TestAABB testAABB1;

	private Vector3f positionDelta;
	private Vector3f rotationDelta;

	public TestingBoundings() {
		super(ModuleUpdate.UPDATE_POST, FlounderBounding.class);
	}

	@Override
	public void init() {
		xAxis = new ButtonAxis(new KeyButton(GLFW.GLFW_KEY_D), new KeyButton(GLFW.GLFW_KEY_A));
		yAxis = new ButtonAxis(new KeyButton(GLFW.GLFW_KEY_LEFT_CONTROL), new KeyButton(GLFW.GLFW_KEY_LEFT_SHIFT));
		zAxis = new ButtonAxis(new KeyButton(GLFW.GLFW_KEY_S), new KeyButton(GLFW.GLFW_KEY_W));
		wAxis = new ButtonAxis(new KeyButton(GLFW.GLFW_KEY_Q), new KeyButton(GLFW.GLFW_KEY_E));
		testAABB0 = new TestAABB(new Vector3f(0.0f, 0.0f, 5.0f));
		testAABB1 = new TestAABB(new Vector3f(5.0f, 0.0f, 5.0f));

		positionDelta = new Vector3f();
		rotationDelta = new Vector3f();
	}

	@Override
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

	@Override
	public void profile() {
	}

	@Override
	public IModule getInstance() {
		return instance;
	}

	@Override
	public void dispose() {

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


		static double resolveCollisionX(AABB thisAABB, AABB other, float moveAmountX) {
			float newAmtX;

		//	if (moveAmountX == 0.0) {
		//		return moveAmountX;
		//	}

			if (moveAmountX > 0.0) { // This max == other min
				newAmtX = other.getMinExtents().getX() - thisAABB.getMaxExtents().getX();
			} else { // This min == other max
				newAmtX = other.getMaxExtents().getX() - thisAABB.getMinExtents().getX();
			}

			if (Math.abs(newAmtX) < Math.abs(moveAmountX)) {
				moveAmountX = newAmtX;
			}

			FlounderProfiler.add("Testing", "X-Depth", Maths.roundToPlace(newAmtX, 3));

			return moveAmountX;
		}

		static double resolveCollisionY(AABB thisAABB, AABB other, float moveAmountY) {
			float newAmtY;

		//	if (moveAmountY == 0.0) {
		//		return moveAmountY;
		//	}

			if (moveAmountY > 0.0) { // This max == other min.
				newAmtY = other.getMinExtents().getY() - thisAABB.getMaxExtents().getY();
			} else { // This min == other max.
				newAmtY = other.getMaxExtents().getY() - thisAABB.getMinExtents().getY();
			}

			if (Math.abs(newAmtY) < Math.abs(moveAmountY)) {
				moveAmountY = newAmtY;
			}

			FlounderProfiler.add("Testing", "Y-Depth", Maths.roundToPlace(newAmtY, 3));

			return moveAmountY;
		}

		static double resolveCollisionZ(AABB thisAABB, AABB other, float moveAmountZ) {
			float newAmtZ;

		//	if (moveAmountZ == 0.0) {
		//		return moveAmountZ;
		//	}

			if (moveAmountZ > 0.0) { // This max == other min.
				newAmtZ = other.getMinExtents().getZ() - thisAABB.getMaxExtents().getZ();
			} else { // This min == other max.
				newAmtZ = other.getMaxExtents().getZ() - thisAABB.getMinExtents().getZ();
			}

			if (Math.abs(newAmtZ) < Math.abs(moveAmountZ)) {
				moveAmountZ = newAmtZ;
			}

			FlounderProfiler.add("Testing", "Z-Depth", Maths.roundToPlace(newAmtZ, 3));

			return moveAmountZ;
		}
	}
}
