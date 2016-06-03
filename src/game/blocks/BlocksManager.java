package game.blocks;

import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;
import game.*;

import java.util.*;

public class BlocksManager {
	private final List<Chunk> CHUNK_LIST = new ArrayList<>();
	private PerlinNoise NOISE = new PerlinNoise((int) MainSeed.getSeed());
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

		for (Chunk chunk : CHUNK_LIST) {
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
			for (Chunk chunk : CHUNK_LIST) {
				chunk.generate(NOISE);
				chunk.populate(Maths.RANDOM);
				chunk.update();
			}

			initialized = true;
		}
	}

	public boolean blockExists(float x, float y, float z) {
		for (Chunk chunk : CHUNK_LIST) {
			int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				return chunk.blockExists(bx, by, bz);
			}
		}

		return false;
	}

	public Block getBlock(float x, float y, float z) {
		for (Chunk chunk : CHUNK_LIST) {
			int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				return chunk.getBlock(bx, by, bz);
			}
		}

		return null;
	}

	public void setBlock(float x, float y, float z, BlockTypes type) {
		for (Chunk chunk : CHUNK_LIST) {
			int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				Block block = Chunk.createBlock(chunk, bx, by, bz, type);
				chunk.putBlock(block, bx, by, bz);
			}
		}
	}

	public boolean insideBlock(Vector3f point) {
		for (Chunk chunk : CHUNK_LIST) {
			if (chunk.contains(point)) {
				int bx = Chunk.inverseBlock(chunk.getPosition().x, point.x);
				int by = Chunk.inverseBlock(chunk.getPosition().y, point.y);
				int bz = Chunk.inverseBlock(chunk.getPosition().z, point.z);

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
