package game.blocks;

import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;

public class Chunk {
	public static final int CHUNK_LENGTH = 32;
	public static final int CHUNK_HEIGHT = 16;
	public static final int DIRT_DEPTH = 3;

	private final Vector2f position;
	private final Block[][][] blocks;
	private final AABB aabb;

	protected Chunk(final Vector2f position) {
		this.position = position;
		this.blocks = new Block[CHUNK_LENGTH][CHUNK_HEIGHT][CHUNK_LENGTH];
		this.aabb = new AABB();
		this.aabb.setMaxExtents(position.x + (CHUNK_LENGTH / 2), (CHUNK_HEIGHT / 2), position.y + (CHUNK_LENGTH / 2));
		this.aabb.setMinExtents(position.x - (CHUNK_LENGTH / 2), (CHUNK_HEIGHT / 2), position.y - (CHUNK_LENGTH / 2));
		generate();
	}

	private void generate() {
		for (int x = 0; x < CHUNK_LENGTH; x++) {
			for (int z = 0; z < CHUNK_LENGTH; z++) {
				double height = NoiseSimplex.noise(position.x + x, position.y + z);

				// Negate any negative noise values.
				if (height < 0) {
					height = -height;
				}

				// Multiply by the max height, then round up.
				height = Math.ceil(height * CHUNK_HEIGHT);

				for (int y = (int) height; y >= 0 && y < CHUNK_HEIGHT; y--) {
					int depth = (int) (height - y);

					BlockType type;

					if (depth == 0) {
						type = BlockType.get("game::grass");
					} else if (depth <= DIRT_DEPTH) {
						type = BlockType.get("game::dirt");
					} else {
						type = BlockType.get("game::stone");
					}

					blocks[x][y][z] = new Block(type, new Vector3f(position.x + x + (x * type.getExtent()), y + (y * type.getExtent()), position.y + z + (z * type.getExtent())));
				}
			}
		}
	}

	public boolean renderable() {
		return FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(aabb);
	}

	protected Block[][][] getBlocks() {
		return blocks;
	}

	protected void addBlock(final Block block) {
		this.blocks[Math.round(block.getPosition().x)][Math.round(block.getPosition().y)][Math.round(block.getPosition().z)] = block;
	}

	protected void removeBlock(final Block block) {
		this.blocks[Math.round(block.getPosition().x)][Math.round(block.getPosition().y)][Math.round(block.getPosition().z)] = null;
	}

	protected void update() {
		// TODO: Sort blocks back to front.
	}
}
