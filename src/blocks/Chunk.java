package blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;
import org.lwjgl.glfw.*;

public class Chunk {
	public static final int CHUNK_LENGTH = 64;
	public static final int CHUNK_HEIGHT = 32;
	public static final int DIRT_DEPTH = 3;

	private final Vector2f position;
	private final Block[][][] blocks;
	private final AABB aabb;
	private final AABB updateAABB;
	private int faceCount;

	protected Chunk(final Vector2f position, final NoisePerlin perlinNoise) {
		this.position = position;
		this.blocks = new Block[CHUNK_LENGTH + 1][CHUNK_HEIGHT][CHUNK_LENGTH + 1];
		this.aabb = new AABB(new Vector3f(position.x, 0, position.y), new Vector3f(position.x + (CHUNK_LENGTH * 2), (CHUNK_HEIGHT * 2), position.y + (CHUNK_LENGTH * 2)));
		this.updateAABB = new AABB();
		this.faceCount = 0;
		generate(perlinNoise);
	}

	private void generate(final NoisePerlin perlinNoise) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				double height = perlinNoise.noise2((position.x + x) / CHUNK_HEIGHT, (position.y + z) / CHUNK_HEIGHT);

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

					blocks[x][y][z] = new Block(type, new Vector3f(calculateBlockX(x, type.getExtent()), calculateBlockY(y, type.getExtent()), calculateBlockZ(z, type.getExtent())));
				}
			}
		}
	}

	private float calculateBlockX(final int arrayX, final float extent) {
		return position.x + arrayX + (arrayX * extent);
	}

	private float calculateBlockY(final int arrayY, final float extent) {
		return arrayY + (arrayY * extent);
	}

	private float calculateBlockZ(final int arrayZ, final float extent) {
		return position.y + arrayZ + (arrayZ * extent);
	}

	public Vector2f getPosition() {
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
					final Block b = blocks[x][y][z];

					if (b != null) {
						final float e = b.getType().getExtent();

						updateAABB.setMinExtents(b.getPosition().x - e, b.getPosition().y - e, b.getPosition().z - e);
						updateAABB.setMaxExtents(b.getPosition().x + e, b.getPosition().y + e, b.getPosition().z + e);

						if (!ManagerDevices.getKeyboard().getKey(GLFW.GLFW_KEY_G)) {
							if (!FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(updateAABB)) {
								b.update(false, false, false, false, false, false);
							} else {
								b.update(
										!blockExists(x, y, z + 1), !blockExists(x, y, z - 1), // Front Back
										!blockExists(x - 1, y, z), !blockExists(x + 1, y, z), // Left Right
										!blockExists(x, y + 1, z), !blockExists(x, y - 1, z)  // Up Down
								);
							}
						}

						for (int f = 0; f < 6; f++) {
							if (b.getFaces()[f].isVisible()) {
								faceCount++;
							}
						}
					}
				}
			}
		}
	}

	public boolean blockExists(final int x, final int y, final int z) {
		if (!inBounds(x, y, z)) {
			//	return WorldManager.getWorldBlock(x, y, z) != null;
			return false;
		} else {
			return blocks[x][y][z] != null;
		}
	}

	public boolean inBounds(final int x, final int y, final int z) {
		return !(x < 0 || y < 0 || z < 0 || x > CHUNK_LENGTH - 1 || y > CHUNK_HEIGHT - 2 || z > CHUNK_LENGTH - 1);
	}

	public int getFaceCount() {
		return faceCount;
	}

	public void addBlock(final Block block, final int positionX, final int positionY, final int positionZ) {
		this.blocks[positionX][positionY][positionZ] = block;
	}

	public void removeBlock(final int positionX, final int positionY, final int positionZ) {
		this.blocks[positionX][positionY][positionZ] = null;
	}

	public Block getBlock(final int positionX, final int positionY, final int positionZ) {
		if (!inBounds(positionX, positionY, positionZ)) {
			return null;
		}

		return blocks[positionX][positionY][positionZ];
	}
}
