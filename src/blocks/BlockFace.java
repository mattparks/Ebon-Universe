package blocks;

import flounder.engine.*;
import flounder.physics.*;

public class BlockFace extends AABB {
	private final FaceTypes type;
	private boolean covered;
	private boolean visible;

	protected BlockFace(final FaceTypes face, final Block parent) {
		this.type = face;
		this.covered = false;
		this.visible = false;

		// TODO: Fit AABB to face not block.
		super.setMinExtents(Block.getPosition(parent).x - BlockTypes.BLOCK_EXTENT, Block.getPosition(parent).y - BlockTypes.BLOCK_EXTENT, Block.getPosition(parent).z - BlockTypes.BLOCK_EXTENT);
		super.setMaxExtents(Block.getPosition(parent).x + BlockTypes.BLOCK_EXTENT, Block.getPosition(parent).y + BlockTypes.BLOCK_EXTENT, Block.getPosition(parent).z + BlockTypes.BLOCK_EXTENT);
	}

	protected static void update(final BlockFace face, final Chunk chunk, final Block block) {
		if (Chunk.getForceUpdate(chunk)) {
			final int currZ = Chunk.inverseBlock(Chunk.getPosition(chunk).z, Block.getPosition(block).z) + (face.type.equals(FaceTypes.FRONT) ? 1 : face.type.equals(FaceTypes.BACK) ? -1 : 0); // Front / Back
			final int currX = Chunk.inverseBlock(Chunk.getPosition(chunk).x, Block.getPosition(block).x) + (face.type.equals(FaceTypes.LEFT) ? -1 : face.type.equals(FaceTypes.RIGHT) ? 1 : 0); // Left / Right
			final int currY = Chunk.inverseBlock(Chunk.getPosition(chunk).y, Block.getPosition(block).y) + (face.type.equals(FaceTypes.UP) ? 1 : face.type.equals(FaceTypes.DOWN) ? -1 : 0); // Up / Down
			final float cz = Chunk.calculateBlock(Chunk.getPosition(chunk).z, currZ);
			final float cx = Chunk.calculateBlock(Chunk.getPosition(chunk).x, currX);
			final float cy = Chunk.calculateBlock(Chunk.getPosition(chunk).y, currY);

			face.covered = WorldManager.blockExists(cx, cy, cz);

			// TODO: Face Merge! Only check within this chunk!
		}

		face.visible = FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(face);
	}

	protected static FaceTypes getType(final BlockFace face) {
		return face.type;
	}

	protected static boolean isCovered(final BlockFace face) {
		return face.covered;
	}

	protected static boolean isRenderable(final BlockFace face) {
		return face.visible && !face.covered;
	}
}
