package blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class WorldManager {
	private static final List<Chunk> CHUNK_LIST = new ArrayList<>();
	private static final NoiseOpenSimplex NOISE = new NoiseOpenSimplex(); // (int) GameSeed.getSeed()

	public static void init() {
		for (int x = 0; x < 2; x++) {
			for (int z = 0; z < 1; z++) {
				for (int y = 0; y < 1; y++) {
					FlounderLogger.log("Creating Chunk At: " + x + ", " + y + ", " + z);
					CHUNK_LIST.add(new Chunk(new Vector3f(x * (Chunk.CHUNK_SIZE * 2.0f), y * (Chunk.CHUNK_SIZE * 2.0f), z * (Chunk.CHUNK_SIZE * 2.0f)), NOISE, Maths.RANDOM));
				}
			}
		}

		//	Chunk.chunkData(CHUNK_LIST.get(0));
	}

	public static void update() {
		CHUNK_LIST.forEach(chunkL -> {
			chunkL.resetFaceCount();

			Chunk.runFaceUpdate(chunkL, (final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent) -> {
				if (!chunk.getForceUpdate()) {
					return;
				}

				if (WorldManager.blockExists(cx, cy, cz, extent)) {
					parent.getFaces()[faceIndex].setCovered(true);
				} else {
					parent.getFaces()[faceIndex].setCovered(false);
					parent.getFaces()[faceIndex].setStretch(extent, extent, extent);
				}
			}, (final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent) -> {
				if (!ManagerDevices.getKeyboard().getKey(GLFW.GLFW_KEY_G)) {
					parent.setVisible(FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(parent.getAABB()));
				}
			});
			Chunk.runFaceUpdate(chunkL, (final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent) -> {
				if (!chunk.getForceUpdate()) {
					return;
				}

				final Block currBlock = WorldManager.getBlock(cx, cy, cz, extent);

			/*if (currBlock != null && currBlock.getType().equals(parent.getType())) { // !parent.getFaces()[faceIndex].isCovered() &&
				if (!currBlock.getFaces()[faceIndex].isCovered()) {
					if (!parent.getFaces()[faceIndex].isStretched() && !currBlock.getFaces()[faceIndex].isStretched()) {
						parent.getFaces()[faceIndex].setCovered(false);
						currBlock.getFaces()[faceIndex].setCovered(true);

						final float stretchDirX = extent + ((faceIndex == 2) ? -2.0f : (faceIndex == 3) ? 2.0f : 0.0f); // Left / Right
						final float stretchDirY = extent + ((faceIndex == 4) ? 2.0f : (faceIndex == 5) ? -2.0f : 0.0f); // Up / Down
						final float stretchDirZ = extent + ((faceIndex == 0) ? 2.0f : (faceIndex == 1) ? -2.0f : 0.0f); // Front / Back

						parent.getFaces()[faceIndex].setStretch(stretchDirX, stretchDirY, stretchDirZ);
					}
				}

				// TODO: Merge this face into that face.
				// parent.getFaces()[faceIndex].setCovered(true);
				// parent.getFaces()[faceIndex].setStretch(be, be, be);
			}*/
			}, (final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent) -> {
				chunk.addFaceCount(parent.getVisibleFaces());
			});

			chunkL.setForceUpdate(false);
			chunkL.updateVisible();

			if (chunkL.isVisible()) {
				AABBManager.addAABBRender(chunkL.getAABB());
			}
		});
	}

	public static boolean blockExists(final float x, final float y, final float z, final float extent) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x, extent);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y, extent);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z, extent);

			if (Chunk.inChunkBounds(bx, by, bz)) {
				return Chunk.blockExists(chunk, bx, by, bz);
			}
		}

		return false;
	}

	public static Block getBlock(final float x, final float y, final float z, final float extent) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x, extent);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y, extent);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z, extent);

			if (Chunk.inChunkBounds(bx, by, bz)) {
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

			if (Chunk.inChunkBounds(bx, by, bz)) {
				final Block block = Chunk.createBlock(chunk, bx, by, bz, type);
				chunk.setBlock(block, bx, by, bz);
			}
		}
	}

	public static boolean insideBlock(final float x, final float y, final float z) {
		for (final Chunk chunk : CHUNK_LIST) {
			final int bx = Chunk.inverseBlock(chunk.getPosition().x, x, BlockType.BLOCK_EXTENT);
			final int by = Chunk.inverseBlock(chunk.getPosition().y, y, BlockType.BLOCK_EXTENT);
			final int bz = Chunk.inverseBlock(chunk.getPosition().z, z, BlockType.BLOCK_EXTENT);

			if (Chunk.inChunkBounds(bx, by, bz)) {
				return chunk.getBlock(bx, by, bz) != null;
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
}
