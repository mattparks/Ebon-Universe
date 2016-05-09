package blocks;

import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;

public class Block {
	public static final Vector3f ROTATION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f POSITION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f SCALE_REUSABLE = new Vector3f(0, 0, 0);

	private final BlockType type;
	private final Vector3f position;
	private final AABB aabb;
	private final BlockVisible[] faces;
	private boolean visible;

	public Block(final BlockType type, final Vector3f position) {
		this.type = type;
		this.position = position;
		this.aabb = new AABB();
		this.visible = false;

		updateBlockAABB(this, aabb);

		this.faces = new BlockVisible[6];
		this.faces[0] = new BlockVisible(BlockFaces.FRONT);
		this.faces[1] = new BlockVisible(BlockFaces.BACK);
		this.faces[2] = new BlockVisible(BlockFaces.LEFT);
		this.faces[3] = new BlockVisible(BlockFaces.RIGHT);
		this.faces[4] = new BlockVisible(BlockFaces.UP);
		this.faces[5] = new BlockVisible(BlockFaces.DOWN);
	}

	private static AABB updateBlockAABB(final Block block, AABB aabb) {
		aabb.setMinExtents(block.position.x - BlockType.BLOCK_EXTENT, block.position.y - BlockType.BLOCK_EXTENT, block.position.z - BlockType.BLOCK_EXTENT);
		aabb.setMaxExtents(block.position.x + BlockType.BLOCK_EXTENT, block.position.y + BlockType.BLOCK_EXTENT, block.position.z + BlockType.BLOCK_EXTENT);
		return aabb;
	}

	protected static Matrix4f blockModelMatrix(final Block block, final int face, Matrix4f modelMatrix) {
		POSITION_REUSABLE.set(block.getPosition());
		ROTATION_REUSABLE.set(0.0f, 0.0f, 0.0f);
		SCALE_REUSABLE.set(block.getFaces()[face].getStretch());
		blockPaneUpdate(block.getFaces()[face].getFace(), ROTATION_REUSABLE, POSITION_REUSABLE);
		Matrix4f.transformationMatrix(POSITION_REUSABLE, ROTATION_REUSABLE, SCALE_REUSABLE, modelMatrix);
		return modelMatrix;
	}

	private static void blockPaneUpdate(final BlockFaces faces, final Vector3f rotation, final Vector3f position) {
		switch (faces) {
			case FRONT:
				rotation.x = 90.0f;
				position.z += BlockType.BLOCK_EXTENT;
				break;
			case BACK:
				rotation.x = 90.0f;
				position.z -= BlockType.BLOCK_EXTENT;
				break;
			case LEFT:
				rotation.z = 90.0f;
				position.x -= BlockType.BLOCK_EXTENT;
				break;
			case RIGHT:
				rotation.z = 90.0f;
				position.x += BlockType.BLOCK_EXTENT;
				break;
			case UP:
				position.y += BlockType.BLOCK_EXTENT;
				break;
			case DOWN:
				position.y -= BlockType.BLOCK_EXTENT;
				break;
		}
	}

	public int getVisibleFaces() {
		if (!visible) {
			return 0;
		}

		int count = 0;

		for (int f = 0; f < 6; f++) {
			if (!faces[f].isCovered()) {
				count++;
			}
		}

		return count;
	}

	public BlockType getType() {
		return type;
	}

	public Vector3f getPosition() {
		return position;
	}

	public BlockVisible[] getFaces() {
		return faces;
	}

	public AABB getAABB() {
		return aabb;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public enum BlockFaces {
		FRONT, BACK, LEFT, RIGHT, UP, DOWN
	}

	public class BlockVisible {
		private final BlockFaces face;
		private final Vector3f stretch;
		private boolean covered;

		public BlockVisible(final BlockFaces face) {
			this.face = face;
			this.stretch = new Vector3f(BlockType.BLOCK_EXTENT, BlockType.BLOCK_EXTENT, BlockType.BLOCK_EXTENT);
			this.covered = true;
		}

		public BlockFaces getFace() {
			return face;
		}

		public Vector3f getStretch() {
			return stretch;
		}

		public void setStretch(final float x, final float y, final float z) {
			stretch.set(x, y, z);
		}

		public boolean isStretched() {
			return stretch.x != 1.0f || stretch.y != 1.0f || stretch.z != 1.0f;
		}

		public boolean isCovered() {
			return covered;
		}

		public void setCovered(final boolean covered) {
			this.covered = covered;
		}
	}
}
