package blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;
import org.lwjgl.glfw.*;

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
			randomlyRemoveBlock();
			TIMER_TESTING += TIMER_OFFSET;
		}

		CHUNK_LIST.forEach(chunk -> {
			chunk.resetFaceCount();

			Chunk.update(chunk, WorldManager::blockUpdateFaces, WorldManager::blockUpdateVisibility, WorldManager::blockUpdatePreFaceMerge);
			Chunk.update(chunk, WorldManager::blockUpdateFaceCount);

			chunk.setForceUpdate(false);
			chunk.updateVisible();
			AABBManager.addAABBRender(chunk.getAABB());
		});
	}

	private static void randomlyRemoveBlock() {
		for (final Chunk chunk : CHUNK_LIST) {
			final int x = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);
			final int y = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);
			final int z = ThreadLocalRandom.current().nextInt(0, Chunk.CHUNK_SIZE);

			chunk.removeBlock(x, y, z);
		}
	}

	public static boolean blockExists(final float x, final float y, final float z) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inChunkBounds(bx, by, bz)) {
				return Chunk.blockExists(chunk, bx, by, bz);
			}
		}

		return false;
	}

	public static Block getBlock(final float x, final float y, final float z) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inChunkBounds(bx, by, bz)) {
				return chunk.getBlock(bx, by, bz);
			}
		}

		return null;
	}

	public static void setBlock(final float x, final float y, final float z, final BlockTypes type) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z);

			if (Chunk.inChunkBounds(bx, by, bz)) {
				final Block block = Chunk.createBlock(chunk, bx, by, bz, type);
				chunk.putBlock(block, bx, by, bz);
			}
		}
	}

	public static boolean insideBlock(final Vector3f point) {
		for (final Chunk chunk : CHUNK_LIST) {
			if (chunk.getAABB().contains(point)) {
				final int bx = Chunk.inverseBlock(chunk.getPosition().x, point.x);
				final int by = Chunk.inverseBlock(chunk.getPosition().y, point.y);
				final int bz = Chunk.inverseBlock(chunk.getPosition().z, point.z);

				if (Chunk.inChunkBounds(bx, by, bz)) {
					return chunk.getBlock(bx, by, bz) != null;
				}
			}
		}

		return false;
	}

	public static int renderableChunkFaces() {
		int count = 0;

		for (final Chunk chunk : CHUNK_LIST) {
			if (chunk.isVisible()) {
				count += chunk.getFaceCount();
			}
		}

		return count;
	}

	public static List<Chunk> getChunkList() {
		return CHUNK_LIST;
	}

	private static void blockUpdateFaces(Chunk chunk, Block parent, int faceIndex, float cx, float cy, float cz) {
		if (!chunk.getForceUpdate()) {
			return;
		}

		if (WorldManager.blockExists(cx, cy, cz)) {
			parent.getFaces()[faceIndex].setCovered(true);
		} else {
			parent.getFaces()[faceIndex].setCovered(false);

			if (parent.getFaces()[faceIndex].isStretched()) {
				parent.getFaces()[faceIndex].setStretch(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT);
			}
		}
	}

	private static void blockUpdateVisibility(Chunk chunk, Block parent, int faceIndex, float cx, float cy, float cz) {
		// TODO: Only calculate once.
		if (!ManagerDevices.getKeyboard().getKey(GLFW.GLFW_KEY_G)) {
			parent.setVisible(FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(parent.getAABB()));
		}
	}

	private static void blockUpdatePreFaceMerge(Chunk chunk, Block parent, int faceIndex, float cx, float cy, float cz) {
		if (!chunk.getForceUpdate()) {
			return;
		}

		final Block targetBlock = WorldManager.getBlock(cx, cy, cz);

		if (targetBlock != null && targetBlock.getType().equals(parent.getType())) {
			if (!targetBlock.getFaces()[faceIndex].isCovered()) {
				final boolean frontBack = ((faceIndex == 0) || (faceIndex == 1)); // Front / Back
				final boolean leftRight = ((faceIndex == 2) || (faceIndex == 3)); // Left / Right
				final boolean upDown = ((faceIndex == 4) || (faceIndex == 5)); // Up / Down

				parent.getFaces()[0].setBlockNearby(frontBack);
				parent.getFaces()[1].setBlockNearby(frontBack);
				parent.getFaces()[2].setBlockNearby(leftRight);
				parent.getFaces()[3].setBlockNearby(leftRight);
				parent.getFaces()[4].setBlockNearby(upDown);
				parent.getFaces()[5].setBlockNearby(upDown);
			}

			// TODO: Merge this face into that face.
			// parent.getFaces()[faceIndex].setCovered(true);
			// parent.getFaces()[faceIndex].setStretch(be, be, be);
		}
	}

	private static void blockUpdateFaceCount(Chunk chunk, Block parent, int faceIndex, float cx, float cy, float cz) {
		// TODO: Only calculate once.
		chunk.addFaceCount(parent.getVisibleFaces());
	}
}
