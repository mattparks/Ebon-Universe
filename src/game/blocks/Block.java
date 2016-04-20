package game.blocks;

import flounder.engine.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;

public class Block {
	public static final Vector3f ROTATION_NONE = new Vector3f(0, 0, 0);
	public static final Vector3f ROTATION_180 = new Vector3f(0, 180, 0);

	private final BlockType type;
	private final Vector3f position;
	private final AABB aabb;
	private boolean covered;
	private boolean rotated;

	public Block(final BlockType type, final Vector3f position) {
		this.type = type;
		this.position = position;
		this.aabb = new AABB();
		this.covered = false;
		this.rotated = false;
		updateAABB(this.aabb, this.position, this.type.getExtent());
	}

	public boolean renderable() {
		return !covered && FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(aabb);
	}

	public void update(final boolean covered) {
		this.covered = covered;
	}

	public BlockType getType() {
		return type;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(final Vector3f position) {
		this.position.set(position);
		updateAABB(aabb, position, type.getExtent());
	}

	private static void updateAABB(final AABB aabb, final Vector3f position, final float extent) {
		aabb.setMaxExtents(position.x + extent, position.y + extent, position.z + extent);
		aabb.setMinExtents(position.x - extent, position.y - extent, position.z - extent);
	}

	public static Matrix4f updateModelMatrix(final Block block, Matrix4f modelMatrix) {
		Matrix4f.transformationMatrix(block.getPosition(), block.isRotated() ? Block.ROTATION_180 : Block.ROTATION_NONE, block.getType().getExtent(), modelMatrix);
		return modelMatrix;
	}

	public AABB getAABB() {
		return aabb;
	}

	public boolean isRotated() {
		return rotated;
	}
}
