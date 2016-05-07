package blocks;

import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.noise.*;

import java.util.*;

public class WorldManager {
	private static final List<Chunk> CHUNK_LIST = new ArrayList<>();
	private static final NoiseOpenSimplex NOISE = new NoiseOpenSimplex(); // (int) GameSeed.getSeed()

	public static void init() {
		for (int x = 0; x < 1; x++) {
			for (int z = 0; z < 2; z++) {
				for (int y = -1; y < 2; y++) {
					FlounderLogger.log("Creating Chunk At: " + x + ", " + y + ", " + z);
					CHUNK_LIST.add(new Chunk(new Vector3f(x * (Chunk.CHUNK_SIZE * 2.0f), y * (Chunk.CHUNK_SIZE * 2.0f), z * (Chunk.CHUNK_SIZE * 2.0f)), NOISE, Maths.RANDOM));
				}
			}
		}

		Chunk.chunkData(CHUNK_LIST.get(0));
	}

	public static void update() {
		CHUNK_LIST.forEach(Chunk::update);
	}

	public static boolean blockExists(final float x, final float y, final float z, final float extent) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x, extent);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y, extent);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z, extent);

			if (chunk.inBounds(bx, by, bz)) {
				return chunk.blockExists(bx, by, bz);
			}
		}

		return false;
	}

	public static Block getBlock(final float x, final float y, final float z, final float extent) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x, extent);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y, extent);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z, extent);

			if (chunk.inBounds(bx, by, bz)) {
				return chunk.getBlock(bx, by, bz);
			}
		}

		return null;
	}

	public static void setBlock(final float x, final float y, final float z, final BlockType type) {
		for (final Chunk chunk : CHUNK_LIST) {
			final float extent = type.getExtent();
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x, extent);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y, extent);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z, extent);

			if (chunk.inBounds(bx, by, bz)) {
				final Block block = Chunk.createBlock(chunk, bx, by, bz, type);
				chunk.setBlock(block, bx, by, bz);
			}
		}
	}

	public static int renderableChunkFaces() {
		int count = 0;

		for (final Chunk chunk : CHUNK_LIST) {
			if (chunk.renderable()) {
				count += chunk.getFaceCount();
			}
		}

		return count;
	}

	public static List<Chunk> getChunkList() {
		return CHUNK_LIST;
	}
}
