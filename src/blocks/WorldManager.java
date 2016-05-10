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

	private static final UpdateBlock UPDATE_1_BLOCK[] = new UpdateBlock[]{WorldManager::update1BlockVisibility, WorldManager::update1BlockPreMerge};
	private static final UpdateFaces UPDATE_1_FACES[] = new UpdateFaces[]{WorldManager::update1FaceCovered};

	private static final UpdateBlock UPDATE_2_BLOCK[] = new UpdateBlock[]{WorldManager::update2BlockMerge};
	private static final UpdateFaces UPDATE_2_FACES[] = new UpdateFaces[]{};

	private static final UpdateBlock UPDATE_3_BLOCK[] = new UpdateBlock[]{WorldManager::update3BlockFaceCount};
	private static final UpdateFaces UPDATE_3_FACES[] = new UpdateFaces[]{};

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
			Chunk.resetFaceCount(chunk);

			Chunk.update(chunk, UPDATE_1_BLOCK, UPDATE_1_FACES);
			Chunk.update(chunk, UPDATE_2_BLOCK, UPDATE_2_FACES);
			Chunk.update(chunk, UPDATE_3_BLOCK, UPDATE_3_FACES);

			Chunk.updateVisibility(chunk);

			if (Chunk.isVisible(chunk)) {
				AABBManager.addAABBRender(Chunk.getAABB(chunk));
			}

			Chunk.setForceUpdate(chunk, false);
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

	private static void update1BlockVisibility(final Chunk chunk, final Block block) {
		block.setVisible(FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(block.getAABB()));
	}

	private static void update1BlockPreMerge(final Chunk chunk, final Block block) {
		if (!Chunk.getForceUpdate(chunk)) {
			return;
		}

		for (int i = 0; i < 6; i++) {
			final int currZ = Chunk.inverseBlock(Chunk.getPosition(chunk).x, block.getPosition().z) + ((i == 0) ? 1 : (i == 1) ? -1 : 0); // Front / Back
			final int currX = Chunk.inverseBlock(Chunk.getPosition(chunk).x, block.getPosition().x) + ((i == 2) ? -1 : (i == 3) ? 1 : 0); // Left / Right
			final int currY = Chunk.inverseBlock(Chunk.getPosition(chunk).x, block.getPosition().y) + ((i == 4) ? 1 : (i == 5) ? -1 : 0); // Up / Down
			final float cz = Chunk.calculateBlock(Chunk.getPosition(chunk).z, currZ);
			final float cx = Chunk.calculateBlock(Chunk.getPosition(chunk).x, currX);
			final float cy = Chunk.calculateBlock(Chunk.getPosition(chunk).y, currY);
			final Block nearby = WorldManager.getBlock(cx, cy, cz);
			final boolean blockNearby = nearby != null && nearby.getType().equals(block.getType());

			if (i == 0 || i == 1) { // Front / Back
				block.getFaces()[0].setBlockNearby(blockNearby);
				block.getFaces()[1].setBlockNearby(blockNearby);
			} else if (i == 2 || i == 3) { // Left / Right
				block.getFaces()[2].setBlockNearby(blockNearby);
				block.getFaces()[3].setBlockNearby(blockNearby);
			} else if (i == 4 || i == 5) { // Up / Down
				block.getFaces()[4].setBlockNearby(blockNearby);
				block.getFaces()[5].setBlockNearby(blockNearby);
			}
		}
	}

	private static void update1FaceCovered(final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz) {
		if (!Chunk.getForceUpdate(chunk)) {
			return;
		}

		parent.getFaces()[faceIndex].setCovered(false);
		parent.getFaces()[faceIndex].setBlockNearby(false);

		if (parent.getFaces()[faceIndex].isStretched()) {
			parent.getFaces()[faceIndex].setStretch(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT);
		}

		if (WorldManager.blockExists(cx, cy, cz)) {
			parent.getFaces()[faceIndex].setCovered(true);
		}
	}

	private static void update2BlockMerge(final Chunk chunk, final Block block) {
		if (!Chunk.getForceUpdate(chunk)) {
			return;
		}

		for (int i = 0; i < 6; i++) {

		}
	}

	private static void update3BlockFaceCount(final Chunk chunk, final Block block) {
		Chunk.addFaceCount(chunk, block.getVisibleFaces());
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
