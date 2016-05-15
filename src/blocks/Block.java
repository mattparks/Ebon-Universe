package blocks;

import flounder.maths.vectors.*;
import flounder.physics.*;

public class Block extends AABB {
	private final BlockTypes type;
	private final Vector3f position;

	public Block(final BlockTypes type, final Vector3f position) {
		this.type = type;
		this.position = position;

		super.setMinExtents(position.x - BlockTypes.BLOCK_EXTENT, position.y - BlockTypes.BLOCK_EXTENT, position.z - BlockTypes.BLOCK_EXTENT);
		super.setMaxExtents(position.x + BlockTypes.BLOCK_EXTENT, position.y + BlockTypes.BLOCK_EXTENT, position.z + BlockTypes.BLOCK_EXTENT);
	}

	protected static void update(final Block block, final Chunk chunk) {
	}

	protected static boolean getCovered(final Block block, final Chunk chunk) {
		for (int s = 0; s < 6; s++) {
			final int currZ = Chunk.inverseBlock(Chunk.getPosition(chunk).z, Block.getPosition(block).z) + ((s == 0) ? 1 : (s == 1) ? -1 : 0); // Front / Back
			final int currX = Chunk.inverseBlock(Chunk.getPosition(chunk).x, Block.getPosition(block).x) + ((s == 2) ? -1 : (s == 3) ? 1 : 0); // Left / Right
			final int currY = Chunk.inverseBlock(Chunk.getPosition(chunk).y, Block.getPosition(block).y) + ((s == 4) ? 1 : (s == 5) ? -1 : 0); // Up / Down
			final float cz = Chunk.calculateBlock(Chunk.getPosition(chunk).z, currZ);
			final float cx = Chunk.calculateBlock(Chunk.getPosition(chunk).x, currX);
			final float cy = Chunk.calculateBlock(Chunk.getPosition(chunk).y, currY);

			if (!WorldManager.blockExists(cx, cy, cz)) {
				return false;
			}
		}

		return true;
	}

	protected static Vector3f getPosition(final Block block) {
		return block.position;
	}

	protected static BlockTypes getType(final Block block) {
		return block.type;
	}
}
