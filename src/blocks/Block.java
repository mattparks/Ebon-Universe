package blocks;

import flounder.maths.matrices.*;
import flounder.maths.vectors.*;

public class Block {
	public static final Vector3f ROTATION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f POSITION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f SCALE_REUSABLE = new Vector3f(0, 0, 0);

	private final BlockType type;
	private final Vector3f position;
	private final BlockVisible[] faces;

	public Block(final BlockType type, final Vector3f position) {
		this.type = type;
		this.position = position;
		this.faces = new BlockVisible[6];
		this.faces[0] = new BlockVisible(BlockFaces.FRONT, type.getExtent());
		this.faces[1] = new BlockVisible(BlockFaces.BACK, type.getExtent());
		this.faces[2] = new BlockVisible(BlockFaces.LEFT, type.getExtent());
		this.faces[3] = new BlockVisible(BlockFaces.RIGHT, type.getExtent());
		this.faces[4] = new BlockVisible(BlockFaces.UP, type.getExtent());
		this.faces[5] = new BlockVisible(BlockFaces.DOWN, type.getExtent());
	}

	public static Matrix4f blockModelMatrix(final Block block, final int face, Matrix4f modelMatrix) {
		POSITION_REUSABLE.set(block.getPosition());
		ROTATION_REUSABLE.set(0.0f, 0.0f, 0.0f);
		SCALE_REUSABLE.set(block.getFaces()[face].getStretch());
		blockPaneUpdate(block.getFaces()[face].getFace(), block.getType().getExtent(), ROTATION_REUSABLE, POSITION_REUSABLE);
		Matrix4f.transformationMatrix(POSITION_REUSABLE, ROTATION_REUSABLE, SCALE_REUSABLE, modelMatrix);
		return modelMatrix;
	}

	private static void blockPaneUpdate(final BlockFaces faces, final float extent, final Vector3f rotation, final Vector3f position) {
		switch (faces) {
			case FRONT:
				rotation.x = 90.0f;
				position.z += extent;
				break;
			case BACK:
				rotation.x = 90.0f;
				position.z -= extent;
				break;
			case LEFT:
				rotation.z = 90.0f;
				position.x -= extent;
				break;
			case RIGHT:
				rotation.z = 90.0f;
				position.x += extent;
				break;
			case UP:
				position.y += extent;
				break;
			case DOWN:
				position.y -= extent;
				break;
		}
	}

	public BlockType getType() {
		return type;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void update(final boolean visibleFront, final boolean visibleBack, final boolean visibleLeft, final boolean visibleRight, final boolean visibleUp, final boolean visibleDown) {
		this.faces[0].setVisible(visibleFront);
		this.faces[1].setVisible(visibleBack);
		this.faces[2].setVisible(visibleLeft);
		this.faces[3].setVisible(visibleRight);
		this.faces[4].setVisible(visibleUp);
		this.faces[5].setVisible(visibleDown);
	}

	public BlockVisible[] getFaces() {
		return faces;
	}

	public enum BlockFaces {
		FRONT, BACK, LEFT, RIGHT, UP, DOWN
	}

	public class BlockVisible {
		private final BlockFaces face;
		private final Vector3f stretch;
		private boolean visible;

		public BlockVisible(final BlockFaces face, final float extent) {
			this.face = face;
			this.visible = false;
			this.stretch = new Vector3f(extent, extent, extent);
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

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}
	}
}
