package blocks;

import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;

import java.util.*;
import java.util.concurrent.*;

public class WorldManager {
	private static final List<Chunk> CHUNK_LIST = new ArrayList<>();
	private static final NoisePerlin NOISE = new NoisePerlin((int) GameSeed.getSeed());

	private static long TIMER_OFFSET = 1000;
	private static long TIMER_TESTING = System.currentTimeMillis() + TIMER_OFFSET;

	public static void init() {
		for (int x = -1; x < 0; x++) {
			for (int z = -1; z < 0; z++) {
				for (int y = -1; y < 2; y++) {
					FlounderLogger.log("Creating Chunk At: " + x + ", " + y + ", " + z);
					CHUNK_LIST.add(new Chunk(new Vector3f(x * (Chunk.CHUNK_SIZE * (2.0f * BlockTypes.BLOCK_EXTENT)), y * (Chunk.CHUNK_SIZE * (2.0f * BlockTypes.BLOCK_EXTENT)), z * (Chunk.CHUNK_SIZE * (2.0f * BlockTypes.BLOCK_EXTENT))), NOISE, Maths.RANDOM));
				}
			}
		}

		// CHUNK_LIST.forEach(Chunk::writeChunkData);
	}

	public static void update() {
		if (System.currentTimeMillis() - TIMER_TESTING > TIMER_OFFSET) {
			updateRandomlyRemoveBlock();
			TIMER_TESTING += TIMER_OFFSET;
		}

		for (final Chunk chunk : CHUNK_LIST) {
			chunk.update();

			if (Chunk.isVisible(chunk)) {
				AABBManager.addAABBRender(Chunk.getAABB(chunk));
			}
		}
	}

	private static void updateRandomlyRemoveBlock() {
		final int randomChunk = ThreadLocalRandom.current().nextInt(0, CHUNK_LIST.size());
		final Chunk chunk = CHUNK_LIST.get(randomChunk);

		if (chunk != null) {
			final int x = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);
			final int y = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);
			final int z = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);
			Chunk.removeBlock(chunk, x, y, z);
		}
	}

	public static boolean blockExists(final float x, final float y, final float z) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(Chunk.getPosition(chunk).x, x);
			final int by = Chunk.inverseBlock(Chunk.getPosition(chunk).y, y);
			final int bz = Chunk.inverseBlock(Chunk.getPosition(chunk).z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				return Chunk.blockExists(chunk, bx, by, bz);
			}
		}

		return false;
	}

	public static Block getBlock(final float x, final float y, final float z) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(Chunk.getPosition(chunk).x, x);
			final int by = Chunk.inverseBlock(Chunk.getPosition(chunk).y, y);
			final int bz = Chunk.inverseBlock(Chunk.getPosition(chunk).z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				return Chunk.getBlock(chunk, bx, by, bz);
			}
		}

		return null;
	}

	public static void setBlock(final float x, final float y, final float z, final BlockTypes type) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(Chunk.getPosition(chunk).x, x);
			final int by = Chunk.inverseBlock(Chunk.getPosition(chunk).y, y);
			final int bz = Chunk.inverseBlock(Chunk.getPosition(chunk).z, z);

			if (Chunk.inBounds(bx, by, bz)) {
				final Block block = Chunk.createBlock(chunk, bx, by, bz, type);
				Chunk.putBlock(chunk, block, bx, by, bz);
			}
		}
	}

	public static boolean insideBlock(final Vector3f point) {
		for (final Chunk chunk : CHUNK_LIST) {
			if (Chunk.getAABB(chunk).contains(point)) {
				final int bx = Chunk.inverseBlock(Chunk.getPosition(chunk).x, point.x);
				final int by = Chunk.inverseBlock(Chunk.getPosition(chunk).y, point.y);
				final int bz = Chunk.inverseBlock(Chunk.getPosition(chunk).z, point.z);

				if (Chunk.inBounds(bx, by, bz)) {
					return Chunk.getBlock(chunk, bx, by, bz) != null;
				}
			}
		}

		return false;
	}

	public static int renderableChunkFaces() {
		int count = 0;

		for (final Chunk chunk : CHUNK_LIST) {
			if (Chunk.isVisible(chunk)) {
				count += Chunk.getFaceCount(chunk);
			}
		}

		return count;
	}

	public static List<Chunk> getChunkList() {
		return CHUNK_LIST;
	}
}
