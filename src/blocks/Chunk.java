package blocks;

import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;

import java.io.*;
import java.util.*;

public class Chunk {
	public static final int CHUNK_SIZE = 16;
	public static final int DIRT_DEPTH = 3;
	public static final int SEA_LEVEL = 10;

	private final Vector3f position;
	private final Block[][][] blocks;
	private final AABB aabb;
	private int faceCount;
	private boolean forceUpdate;
	private boolean visible;

	protected Chunk(final Vector3f position, final NoiseOpenSimplex noise, final Random random) {
		this.position = position;
		this.blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE + 1];
		this.aabb = new AABB(position, new Vector3f(position.x + (CHUNK_SIZE * 2), position.y + (CHUNK_SIZE * 2), position.z + (CHUNK_SIZE * 2)));
		this.faceCount = 0;
		this.forceUpdate = true;
		this.visible = false;
		generate(noise);
		populate(random);
	}

	public static void chunkData(final Chunk chunk) {
		File file = new File("chunk-" + chunk.position.x + "-" + chunk.position.y + "-" + chunk.position.z + ".txt");
		String string = "";

		for (int x = 0; x < chunk.blocks.length; x++) {
			for (int z = 0; z < chunk.blocks[x].length; z++) {
				for (int y = 0; y < chunk.blocks[z].length; y++) {
					final Block block = chunk.blocks[x][z][y];

					if (block != null) {
						string += "[(" + +x + "," + y + "," + z + ")'" + block.getType().getName() + "'],";
					}
				}
			}
		}

		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(string); // new String(CompressionUtils.compress(string))
			fileWriter.flush();
			fileWriter.close();
		} catch (final Exception e) {
			FlounderLogger.exception(e);
		}

		/*try {
			BufferedReader fileReader = new BufferedReader(new FileReader(file));
			String fileData = fileReader.toString();
			String line = fileReader.readLine();

			while (line != null) {
				fileData += line;
				fileData += "\n";
				line = fileReader.readLine();
			}

			byte[] data = fileData.getBytes();
			String read = CompressionUtils.decompress(data);
			FlounderLogger.log(read);
		} catch (final Exception e) {
			FlounderLogger.exception(e);
		}*/
	}

	public static Block createBlock(final Chunk chunk, final int x, final int y, final int z, final BlockType type) {
		return new Block(type, new Vector3f(calculateBlock(chunk.position.x, x, type.getExtent()), calculateBlock(chunk.position.y, y, type.getExtent()), calculateBlock(chunk.position.z, z, type.getExtent())));
	}

	public static float calculateBlock(final float position, final int array, final float extent) {
		return position + array + (array * extent);
	}

	public static int inverseBlock(final float position, final float component, final float extent) {
		return (int) ((component - position) / (2.0f * extent));
	}

	private void generate(final NoiseOpenSimplex noise) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					BlockType type = null;
					/*int posX = (int) position.x + x;
					int posY = (int) position.y + y;
					int posZ = (int) position.z + z;

					float height = ((float) noise.eval((double) posX / 50d, (double) posY / 50d, (double) posZ / 50d) + 1f) * (float) CHUNK_SIZE;

					if (posY < height) {
						type = BlockType.get("game::stone");
					} else if (posY - 1f <= height) {
						if (posY <= SEA_LEVEL + 1) {
							type = BlockType.get("game::sand");
						} else {
							type = BlockType.get("game::grass");
						}
					} else {
						if (posY <= SEA_LEVEL) {
							type = BlockType.get("game::water");
						} else {
							type = null;
						}
					}*/

					type = BlockType.get("game::stone");

					if (type != null) {
						blocks[x][z][y] = createBlock(this, x, y, z, type);
					}
				}
			}
		}
	}

	private void populate(final Random random) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y];

					if (block != null) {
						if (block.getType().getName().equals("game::stone")) {
							int rand = random.nextInt(10000);
							BlockType type = null;

							if (rand <= 100) {
								type = BlockType.get("game::coalOre");
							} else if (rand > 100 && rand <= 150) {
								type = BlockType.get("game::ironOre");
							} else if (rand > 150 && rand <= 160) {
								type = BlockType.get("game::goldOre");
							}

							if (type != null) {
								blocks[x][z][y] = new Block(type, new Vector3f(calculateBlock(position.x, x, type.getExtent()), calculateBlock(position.y, y, type.getExtent()), calculateBlock(position.z, z, type.getExtent())));
							}
						}
					}
				}
			}
		}
	}

	public boolean inBounds(final float x, final float y, final float z) {
		return !(x < 0 || y < 0 || z < 0 || x > CHUNK_SIZE - 1 || y > CHUNK_SIZE - 1 || z > CHUNK_SIZE - 1);
	}

	public boolean blockExists(final int x, final int y, final int z) {
		return inBounds(x, y, z) && getBlock(x, y, z) != null;
	}

	public Block getBlock(final int x, final int y, final int z) {
		return blocks[x][z][y];
	}

	public void setBlock(final Block block, final int x, final int y, final int z) {
		blocks[x][z][y] = block;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Block[][][] getBlocks() {
		return blocks;
	}

	public AABB getAABB() {
		return aabb;
	}

	public boolean isVisible() {
		return visible;
	}

	public void updateVisible() {
		this.visible = FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(aabb);
	}

	public int getFaceCount() {
		return faceCount;
	}

	public boolean getForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(final boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public void resetFaceCount() {
		faceCount = 0;
	}

	public void addFaceCount(final int newFaces) {
		faceCount += newFaces;
	}

	public void runFaceUpdate(final FaceUpdates... faceUpdates) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y];

					if (block != null) {
						final float be = block.getType().getExtent();
						final int bx = inverseBlock(position.x, block.getPosition().x, be);
						final int by = inverseBlock(position.y, block.getPosition().y, be);
						final int bz = inverseBlock(position.z, block.getPosition().z, be);

						for (int i = 0; i < 6; i++) {
							final int currX = bx + ((i == 2) ? -1 : (i == 3) ? 1 : 0); // Left / Right
							final int currY = by + ((i == 4) ? 1 : (i == 5) ? -1 : 0); // Up / Down
							final int currZ = bz + ((i == 0) ? 1 : (i == 1) ? -1 : 0); // Front / Back

							final float cx = calculateBlock(position.x, currX, be);
							final float cy = calculateBlock(position.y, currY, be);
							final float cz = calculateBlock(position.z, currZ, be);

							for (FaceUpdates updates : faceUpdates) {
								if (updates != null) {
									updates.update(this, block, i, cx, cy, cz, be);
								}
							}
						}
					}
				}
			}
		}
	}

	public interface FaceUpdates {
		void update(final Chunk chunk, final Block parent, final int faceIndex, final float cx, final float cy, final float cz, final float extent);
	}
}
