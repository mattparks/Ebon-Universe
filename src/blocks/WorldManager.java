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

	private static long TIMER_OFFSET = 2000;
	private static long TIMER_TESTING = System.currentTimeMillis() + TIMER_OFFSET;

	public static void init() {
		for (int x = -1; x < 0; x++) {
			for (int z = -1; z < 0; z++) {
				for (int y = -1; y < 1; y++) {
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
			Chunk.update(chunk, WorldManager::updateCovered, WorldManager::updatePremerge);
			Chunk.update(chunk, WorldManager::updateMerge);
			Chunk.updateVisibility(chunk);
			Chunk.setForceUpdate(chunk, false);

			if (Chunk.isVisible(chunk)) {
				AABBManager.addAABBRender(Chunk.getAABB(chunk));
			}
		}
	}

	private static void updateRandomlyRemoveBlock() {
		for (final Chunk chunk : CHUNK_LIST) {
			final int x = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);
			final int y = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);
			final int z = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);
			Chunk.removeBlock(chunk, x, y, z);
		}
	}

	private static void updateCovered(final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz) {
		if (!Chunk.getForceUpdate(chunk)) {
			return;
		}

		parent.getFaces()[faceIndex].setCovered(WorldManager.blockExists(cx, cy, cz));

		if (parent.getFaces()[faceIndex].isStretched()) {
			parent.getFaces()[faceIndex].setStretch(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT);
		}
	}

	private static void updatePremerge(final Chunk chunk, final Block block, final int faceIndex, final float cx, final float cy, final float cz) {
		if (!Chunk.getForceUpdate(chunk) || !block.getFaces()[faceIndex].isCovered()) {
			return;
		}

		final Block nearby = WorldManager.getBlock(cx, cy, cz);
		final boolean blockNearby = nearby != null && nearby.getType().equals(block.getType());

		if (faceIndex != 0 && faceIndex != 1) { // Front / Back
			if (!block.getFaces()[0].isBlockNearby() || !block.getFaces()[1].isBlockNearby()) {
				block.getFaces()[0].setBlockNearby(blockNearby);
				block.getFaces()[1].setBlockNearby(blockNearby);
			}
		}

		if (faceIndex != 2 && faceIndex != 3) { // Left / Right
			if (!block.getFaces()[2].isBlockNearby() || !block.getFaces()[3].isBlockNearby()) {
				block.getFaces()[2].setBlockNearby(blockNearby);
				block.getFaces()[3].setBlockNearby(blockNearby);
			}
		}

		if (faceIndex != 4 && faceIndex != 5) { // Up / Down
			if (!block.getFaces()[4].isBlockNearby() || !block.getFaces()[5].isBlockNearby()) {
				block.getFaces()[4].setBlockNearby(blockNearby);
				block.getFaces()[5].setBlockNearby(blockNearby);
			}
		}
	}

	private static void updateMerge(final Chunk chunk, final Block block, final int faceIndex, final float cx, final float cy, final float cz) {
		if (!Chunk.getForceUpdate(chunk)) {
			return;
		}

		if (!block.getFaces()[faceIndex].isCovered()) {
			final boolean blockNearby = block.getFaces()[faceIndex].isBlockNearby();

			if (blockNearby) {
				// TODO: BlockNearby && !BlockNearby.isCovered ? MERGE
			}
		}
	}

	public static boolean blockExists(final float x, final float y, final float z) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(Chunk.getPosition(chunk).x, x);
			final int by = Chunk.inverseBlock(Chunk.getPosition(chunk).y, y);
			final int bz = Chunk.inverseBlock(Chunk.getPosition(chunk).z, z);

			if (Chunk.inChunkBounds(bx, by, bz)) {
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

			if (Chunk.inChunkBounds(bx, by, bz)) {
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

			if (Chunk.inChunkBounds(bx, by, bz)) {
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

				if (Chunk.inChunkBounds(bx, by, bz)) {
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
