package blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class WorldManager {
	private static final List<Chunk> CHUNK_LIST = new ArrayList<>();
	private static final NoiseOpenSimplex NOISE = new NoiseOpenSimplex(); // (int) GameSeed.getSeed()
	private static final Chunk.FaceUpdates[] FACE_UPDATES = new Chunk.FaceUpdates[4];

	public static void init() {
		FACE_UPDATES[0] = (final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent) -> {
			if (!chunk.getForceUpdate()) {
				return;
			}

			if (WorldManager.blockExists(cx, cy, cz, extent)) {
				parent.getFaces()[faceIndex].setCovered(true);
			} else {
				parent.getFaces()[faceIndex].setCovered(false);
				parent.getFaces()[faceIndex].setStretch(extent, extent, extent);
			}
		};
		FACE_UPDATES[1] = (final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent) -> {
			if (!ManagerDevices.getKeyboard().getKey(GLFW.GLFW_KEY_G)) {
				parent.setVisible(FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(parent.getAABB()));
			}
		};
		FACE_UPDATES[2] = (final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent) -> {
			if (!chunk.getForceUpdate()) {
				return;
			}

			final Block currBlock = WorldManager.getBlock(cx, cy, cz, extent);

			if (currBlock != null && currBlock.getType().equals(parent.getType())) {
				// TODO: Merge this face into that face.
				// block.getFaces()[i].setCovered(true);
				// block.getFaces()[i].setStretch(be, be, be);
			}
		};
		FACE_UPDATES[3] = (final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent) -> {
			chunk.addFaceCount(parent.getVisibleFaces());
		};

		for (int x = 0; x < 1; x++) {
			for (int z = 0; z < 2; z++) {
				for (int y = -1; y < 2; y++) {
					FlounderLogger.log("Creating Chunk At: " + x + ", " + y + ", " + z);
					CHUNK_LIST.add(new Chunk(new Vector3f(x * (Chunk.CHUNK_SIZE * 2.0f), y * (Chunk.CHUNK_SIZE * 2.0f), z * (Chunk.CHUNK_SIZE * 2.0f)), NOISE, Maths.RANDOM));
				}
			}
		}

		//	Chunk.chunkData(CHUNK_LIST.get(0));
	}

	public static void update() {
		CHUNK_LIST.forEach(chunk -> {
			chunk.resetFaceCount();
			chunk.runFaceUpdate(FACE_UPDATES[0], FACE_UPDATES[1]);
			chunk.runFaceUpdate(FACE_UPDATES[2], FACE_UPDATES[3]);
			chunk.setForceUpdate(false);
		});
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
