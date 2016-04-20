package game.blocks;

import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.physics.*;

public class Block {
	private final BlockType type;
	private final Vector3f position;
	private final AABB aabb;
	private boolean covered;

	public Block(final BlockType type, final Vector3f position) {
		this.type = type;
		this.position = position;
		this.aabb = new AABB();
		this.covered = false;
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

	public AABB getAABB() {
		return aabb;
	}
}
