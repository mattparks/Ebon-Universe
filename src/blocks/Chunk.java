package blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;
import org.lwjgl.glfw.*;

public class Chunk {
	public static final int CHUNK_LENGTH = 32;
	public static final int CHUNK_HEIGHT = 16;
	public static final int DIRT_DEPTH = 3;

	private final Vector3f position;
	private final Block[][][] blocks;
	private final AABB aabb;
	private int faceCount;
	private boolean forceUpdate;

	protected Chunk(final Vector3f position, final NoisePerlin perlinNoise) {
		this.position = position;
		this.blocks = new Block[CHUNK_LENGTH][CHUNK_LENGTH][CHUNK_HEIGHT];
		this.aabb = new AABB(position, new Vector3f(position.x + (CHUNK_LENGTH * 2), position.y + (CHUNK_HEIGHT * 2), position.z + (CHUNK_LENGTH * 2)));
		this.faceCount = 0;
		this.forceUpdate = true;
		generate(perlinNoise);
	}

	private void generate(final NoisePerlin perlinNoise) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				double height = perlinNoise.noise2((position.x + x) / CHUNK_HEIGHT, (position.z + z) / CHUNK_HEIGHT);

				// Negate any negative noise values.
				if (height < 0) {
					height = -height;
				}

				// Multiply by the max height, then round up.
				height = Math.ceil(height * CHUNK_HEIGHT);

				for (int y = (int) height; y >= 0 && y < blocks[z].length; y--) {
					int depth = (int) (height - y);

					BlockType type;

					// TODO: Biomes.

					if (depth == 0) {
						type = BlockType.get("game::grass");
					} else if (depth <= DIRT_DEPTH) {
						type = BlockType.get("game::dirt");
					} else {
						type = BlockType.get("game::stone");
						// TODO: Random ore spawns.
					}

					blocks[x][z][y] = new Block(type, new Vector3f(calculateBlock(x, type.getExtent()), calculateBlock(y, type.getExtent()), calculateBlock(z, type.getExtent())));
				}
			}
		}
	}

	private float calculateBlock(final int array, final float extent) {
		return position.z + array + (array * extent);
	}

	public Vector3f getPosition() {
		return position;
	}

	public Block[][][] getBlocks() {
		return blocks;
	}

	public boolean renderable() {
		return FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(aabb);
	}

	public void update() {
		faceCount = 0;

		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y]; // TODO: Fix out of bounds?

					if (block != null) {
						if (!ManagerDevices.getKeyboard().getKey(GLFW.GLFW_KEY_G)) {
							block.setVisible(FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(block.getAABB()));
						}

						if (forceUpdate) {
							updateCoveredFaces(block, x, y, z);
						}

						if (block.isVisible()) {
							for (int f = 0; f < 6; f++) {
								if (!block.getFaces()[f].isCovered()) {
									faceCount++;
								}
							}
						}
					}
				}
			}
		}

		forceUpdate = false;
	}

	public void updateCoveredFaces(final Block block, final int x, final int y, final int z) {
		block.getFaces()[0].setCovered(blockExists(x, y, z + 1)); // Front
		block.getFaces()[1].setCovered(blockExists(x, y, z - 1)); // Back
		block.getFaces()[2].setCovered(blockExists(x - 1, y, z)); // Left
		block.getFaces()[3].setCovered(blockExists(x + 1, y, z)); // Right
		block.getFaces()[4].setCovered(blockExists(x, y + 1, z)); // Up
		block.getFaces()[5].setCovered(blockExists(x, y - 1, z)); // Down
	}

	public boolean blockExists(final int x, final int y, final int z) {
		if (!inBounds(x, y, z)) {
			//	return WorldManager.getWorldBlock(x, y, z) != null;
			return false;
		} else {
			return blocks[x][z][y] != null;
		}
	}

	public boolean inBounds(final int x, final int y, final int z) {
		return !(x < 0 || y < 0 || z < 0 || x > CHUNK_LENGTH - 1 || y > CHUNK_HEIGHT - 2 || z > CHUNK_LENGTH - 1);
	}

	public int getFaceCount() {
		return faceCount;
	}

	public void addBlock(final Block block, final int positionX, final int positionY, final int positionZ) {
		this.blocks[positionX][positionZ][positionY] = block;
		this.forceUpdate = true;
	}

	public void removeBlock(final int positionX, final int positionY, final int positionZ) {
		this.blocks[positionX][positionZ][positionY] = null;
		this.forceUpdate = true;
	}

	public Block getBlock(final int positionX, final int positionY, final int positionZ) {
		if (!inBounds(positionX, positionY, positionZ)) {
			return null;
		}

		return blocks[positionX][positionZ][positionY];
	}
}
