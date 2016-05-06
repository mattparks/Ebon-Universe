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
		for (int x = 0; x < 2; x++) {
			for (int z = 0; z < 2; z++) {
				for (int y = -1; y < 2; y++) {
					FlounderLogger.log("Creating Chunk At: "+ x + ", " + y + ", " + z);
					CHUNK_LIST.add(new Chunk(new Vector3f(x * (Chunk.CHUNK_SIZE * 2.0f), y * (Chunk.CHUNK_SIZE * 2.0f), z * (Chunk.CHUNK_SIZE * 2.0f)), NOISE, Maths.RANDOM));
				}
			}
		}
	}

	public static void update() {
		CHUNK_LIST.forEach(Chunk::update);
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

	public static void setBlock(final float x, final float y, final float z, final BlockType type) {
		CHUNK_LIST.forEach(chunk -> {
			int inverseX, inverseY, inverseZ;

			if (chunk.inBounds(
					inverseX = Chunk.inversePosition(chunk.getPosition().x, x, type.getExtent()),
					inverseY = Chunk.inversePosition(chunk.getPosition().y, y, type.getExtent()),
					inverseZ = Chunk.inversePosition(chunk.getPosition().z, z, type.getExtent())
			)) {
				FlounderLogger.error("Setting block at " + inverseX + "," + inverseY + "," + inverseZ);

				final Block block = Chunk.createBlock(chunk, inverseX, inverseY, inverseZ, type);
				chunk.addBlock(block, inverseX, inverseY, inverseZ);
			}
		});
	}

	public static Block getWorldBlock(final float x, final float y, final float z, final float extent) {
		for (final Chunk chunk : CHUNK_LIST) {
			int inverseX, inverseY, inverseZ;

			if (chunk.inBounds(
					inverseX = Chunk.inversePosition(chunk.getPosition().x, x, extent),
					inverseY = Chunk.inversePosition(chunk.getPosition().y, y, extent),
					inverseZ = Chunk.inversePosition(chunk.getPosition().z, z, extent)
			)) {
				FlounderLogger.error("Getting block at " + inverseX + "," + inverseY + "," + inverseZ);

				return chunk.getBlock(inverseX, inverseY, inverseZ);
			}
		}

		return null;
	}
}
