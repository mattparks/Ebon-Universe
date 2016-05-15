package blocks;

import flounder.engine.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;

public class Block extends AABB {
	public static final Vector3f ROTATION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f POSITION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f SCALE_REUSABLE = new Vector3f(0, 0, 0);

	private final BlockTypes type;
	private final Vector3f position;
	private final BlockFace[] faces;

	private boolean highlighted;

	public Block(final BlockTypes type, final Vector3f position) {
		this.type = type;
		this.position = position;

		super.setMinExtents(position.x - BlockTypes.BLOCK_EXTENT, position.y - BlockTypes.BLOCK_EXTENT, position.z - BlockTypes.BLOCK_EXTENT);
		super.setMaxExtents(position.x + BlockTypes.BLOCK_EXTENT, position.y + BlockTypes.BLOCK_EXTENT, position.z + BlockTypes.BLOCK_EXTENT);

		this.faces = new BlockFace[6];
		this.faces[0] = new BlockFace(FaceTypes.FRONT, this);
		this.faces[1] = new BlockFace(FaceTypes.BACK, this);
		this.faces[2] = new BlockFace(FaceTypes.LEFT, this);
		this.faces[3] = new BlockFace(FaceTypes.RIGHT, this);
		this.faces[4] = new BlockFace(FaceTypes.UP, this);
		this.faces[5] = new BlockFace(FaceTypes.DOWN, this);

		this.highlighted = false;
	}

	protected static void update(final Block block, final Chunk chunk) {
		for (int f = 0; f < 6; f++) {
			BlockFace.update(block.faces[f], chunk, block);
		}
	}

	protected static Matrix4f blockModelMatrix(final Block block, final int face, final Matrix4f modelMatrix) {
		POSITION_REUSABLE.set(Block.getPosition(block));
		ROTATION_REUSABLE.set(0.0f, 0.0f, 0.0f);
		SCALE_REUSABLE.set(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT);

		switch (BlockFace.getType(Block.getFaces(block)[face])) {
			case FRONT:
				ROTATION_REUSABLE.x = 90.0f;
				POSITION_REUSABLE.z += BlockTypes.BLOCK_EXTENT;
				break;
			case BACK:
				ROTATION_REUSABLE.x = 90.0f;
				POSITION_REUSABLE.z -= BlockTypes.BLOCK_EXTENT;
				break;
			case LEFT:
				ROTATION_REUSABLE.z = 90.0f;
				POSITION_REUSABLE.x -= BlockTypes.BLOCK_EXTENT;
				break;
			case RIGHT:
				ROTATION_REUSABLE.z = 90.0f;
				POSITION_REUSABLE.x += BlockTypes.BLOCK_EXTENT;
				break;
			case UP:
				POSITION_REUSABLE.y += BlockTypes.BLOCK_EXTENT;
				break;
			case DOWN:
				POSITION_REUSABLE.y -= BlockTypes.BLOCK_EXTENT;
				break;
		}

		return Matrix4f.transformationMatrix(POSITION_REUSABLE, ROTATION_REUSABLE, SCALE_REUSABLE, modelMatrix);
	}

	protected static Vector3f getPosition(final Block block) {
		return block.position;
	}

	protected static BlockFace[] getFaces(final Block block) {
		return block.faces;
	}

	protected static int getVisibleFaces(final Block block) {
		int visibleFaces = 0;

		for (int f = 0; f < 6; f++) {
			if (BlockFace.isRenderable(block.faces[f])) {
				visibleFaces++;
			}
		}

		return visibleFaces;
	}

	protected static BlockTypes getType(final Block block) {
		return block.type;
	}

	protected static void setHighlighted(final Block block, final boolean highlighted) {
		block.highlighted = highlighted;
	}

	protected static boolean isHighlighted(final Block block) {
		return block.highlighted;
	}
}
