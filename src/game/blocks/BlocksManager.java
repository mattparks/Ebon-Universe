package game.blocks;

import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;

import java.util.*;

public class BlocksManager {
	private final List<Chunk> CHUNK_LIST = new ArrayList<>();
	private final PerlinNoise NOISE = new PerlinNoise((int) GameSeed.getSeed());
	private boolean initialized;

	public BlocksManager() {
		for (int x = -1; x < 0; x++) {
			for (int z = -1; z < 0; z++) {
				for (int y = -1; y < 0; y++) {
					FlounderLogger.log("Creating Chunk At: " + x + ", " + y + ", " + z);
					CHUNK_LIST.add(new Chunk(new Vector3f(x * (Chunk.CHUNK_SIZE * (2.0f * BlockTypes.BLOCK_EXTENT)), y * (Chunk.CHUNK_SIZE * (2.0f * BlockTypes.BLOCK_EXTENT)), z * (Chunk.CHUNK_SIZE * (2.0f * BlockTypes.BLOCK_EXTENT)))));
				}
			}
		}
	}

	public void update() {
		if (!initialized) {
			initWorld();
		}

		for (final Chunk chunk : CHUNK_LIST) {
			chunk.update();

			if (chunk.isVisible()) {
				AABBManager.addAABBRender(chunk);
			}
		}

		SortingAlgorithms.quickSort(CHUNK_LIST); // Sorts furthest to nearest.
		Collections.reverse(CHUNK_LIST); // Reverses so closer chunks are first.
	}

	private void initWorld() {
		if (!initialized) {
			for (final Chunk chunk : CHUNK_LIST) {
				chunk.generate(NOISE);
				chunk.populate(Maths.RANDOM);
				chunk.update();
			}

			initialized = true;
		}
	}

	public boolean blockExists(final float x, final float y, final float z) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				return chunk.blockExists(bx, by, bz);
			}
		}

		return false;
	}

	public Block getBlock(final float x, final float y, final float z) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				return chunk.getBlock(bx, by, bz);
			}
		}

		return null;
	}

	public void setBlock(final float x, final float y, final float z, final BlockTypes type) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				final Block block = Chunk.createBlock(chunk, bx, by, bz, type);
				chunk.putBlock(block, bx, by, bz);
			}
		}
	}

	public boolean insideBlock(final Vector3f point) {
		for (final Chunk chunk : CHUNK_LIST) {
			if (chunk.contains(point)) {
				final int bx = Chunk.inverseBlock(chunk.getPosition().x, point.x);
				final int by = Chunk.inverseBlock(chunk.getPosition().y, point.y);
				final int bz = Chunk.inverseBlock(chunk.getPosition().z, point.z);

				if (Chunk.inBounds(bx, by, bz)) {
					return chunk.getBlock(bx, by, bz) != null;
				}
			}
		}

		return false;
	}

	public List<Chunk> getChunkList() {
		return CHUNK_LIST;
	}
}
