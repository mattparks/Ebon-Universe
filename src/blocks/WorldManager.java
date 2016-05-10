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
	private static long TIMER_OFFSET = 500;
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

			Chunk.update(chunk, new BlockUpdateFaces(), new BlockUpdateVisibility());
			Chunk.update(chunk, new BlockUpdateFaceMerge(), new BlockUpdateFaceCount());

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

	private static class BlockUpdateFaces implements FaceUpdates {
		@Override
		public void update(Chunk chunk, Block parent, int faceIndex, float cx, float cy, float cz) {
			if (!chunk.getForceUpdate()) {
				return;
			}

			if (WorldManager.blockExists(cx, cy, cz)) {
				parent.getFaces()[faceIndex].setCovered(true);
			} else {
				parent.getFaces()[faceIndex].setCovered(false);
				parent.getFaces()[faceIndex].setStretch(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT);
			}
		}
	}

	private static class BlockUpdateVisibility implements FaceUpdates {
		@Override
		public void update(Chunk chunk, Block parent, int faceIndex, float cx, float cy, float cz) {
			if (!ManagerDevices.getKeyboard().getKey(GLFW.GLFW_KEY_G)) {
				parent.setVisible(FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(parent.getAABB()));
			}
		}
	}

	private static class BlockUpdateFaceMerge implements FaceUpdates {
		@Override
		public void update(Chunk chunk, Block parent, int faceIndex, float cx, float cy, float cz) {
			if (!chunk.getForceUpdate()) {
				return;
			}

			final Block currBlock = WorldManager.getBlock(cx, cy, cz);

				/*if (currBlock != null && currBlock.getType().equals(parent.getType())) { // !parent.getFaces()[faceIndex].isCovered() &&
					if (!currBlock.getFaces()[faceIndex].isCovered()) {
						if (!parent.getFaces()[faceIndex].isStretched() && !currBlock.getFaces()[faceIndex].isStretched()) {
							parent.getFaces()[faceIndex].setCovered(false);
							currBlock.getFaces()[faceIndex].setCovered(true);

							final float stretchDirX = BlockTypes.BLOCK_EXTENT + ((faceIndex == 2) ? -2.0f : (faceIndex == 3) ? 2.0f : 0.0f); // Left / Right
							final float stretchDirY = BlockTypes.BLOCK_EXTENT + ((faceIndex == 4) ? 2.0f : (faceIndex == 5) ? -2.0f : 0.0f); // Up / Down
							final float stretchDirZ = BlockTypes.BLOCK_EXTENT + ((faceIndex == 0) ? 2.0f : (faceIndex == 1) ? -2.0f : 0.0f); // Front / Back

							parent.getFaces()[faceIndex].setStretch(stretchDirX, stretchDirY, stretchDirZ);
						}
					}

					// TODO: Merge this face into that face.
					// parent.getFaces()[faceIndex].setCovered(true);
					// parent.getFaces()[faceIndex].setStretch(be, be, be);
				}*/
		}
	}

	private static class BlockUpdateFaceCount implements FaceUpdates {
		@Override
		public void update(Chunk chunk, Block parent, int faceIndex, float cx, float cy, float cz) {
			chunk.addFaceCount(parent.getVisibleFaces());
		}
	}
}
