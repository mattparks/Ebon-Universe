package blocks;

import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;

import java.io.*;
import java.util.*;

public class Chunk {
	public static final int CHUNK_SIZE = 16;

	private final Vector3f position;
	private final Block[][][] blocks;
	private final AABB aabb;
	private int faceCount;
	private boolean forceUpdate;
	private boolean visible;
	private boolean empty;

	protected Chunk(final Vector3f position, final NoisePerlin noise, final Random random) {
		this.position = position;
		this.blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE + 1];
		this.aabb = new AABB(
				Vector3f.subtract(position, new Vector3f(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT), null),
				Vector3f.subtract(new Vector3f(
						(position.x + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT),
						(position.y + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT),
						(position.z + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT)
				), new Vector3f(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT), null)
		);
		this.faceCount = 0;
		this.forceUpdate = true;
		this.visible = false;
		this.empty = true;
		generate(noise);
		populate(random);
	}

	protected static void writeChunkData(final Chunk chunk) { // , final String file
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
	}

	protected static Chunk readChunkData(final String file) {
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

		return null;
	}

	public static Block createBlock(final Chunk chunk, final int x, final int y, final int z, final BlockTypes type) {
		return new Block(type, new Vector3f(calculateBlock(chunk.position.x, x), calculateBlock(chunk.position.y, y), calculateBlock(chunk.position.z, z)));
	}

	protected static float calculateBlock(final float position, final int array) {
		return position + (2.0f * array * BlockTypes.BLOCK_EXTENT);
	}

	protected static int inverseBlock(final float position, final float component) {
		return (int) ((component - position) / (2.0f * BlockTypes.BLOCK_EXTENT));
	}

	public static boolean inChunkBounds(final float x, final float y, final float z) {
		return !(x < 0 || y < 0 || z < 0 || x > CHUNK_SIZE - 1 || y > CHUNK_SIZE - 1 || z > CHUNK_SIZE - 1);
	}

	public static boolean blockExists(final Chunk chunk, final int x, final int y, final int z) {
		return inChunkBounds(x, y, z) && chunk.getBlock(x, y, z) != null;
	}

	protected static void update(final Chunk chunk, final FaceUpdates... faceUpdates) {
		chunk.empty = true;

		for (int x = 0; x < chunk.blocks.length; x++) {
			for (int z = 0; z < chunk.blocks[x].length; z++) {
				for (int y = 0; y < chunk.blocks[z].length; y++) {
					final Block block = chunk.blocks[x][z][y];

					if (block != null) {
						final int bx = inverseBlock(chunk.position.x, block.getPosition().x);
						final int by = inverseBlock(chunk.position.y, block.getPosition().y);
						final int bz = inverseBlock(chunk.position.z, block.getPosition().z);

						for (int i = 0; i < 6; i++) {
							final int currX = bx + ((i == 2) ? -1 : (i == 3) ? 1 : 0); // Left / Right
							final int currY = by + ((i == 4) ? 1 : (i == 5) ? -1 : 0); // Up / Down
							final int currZ = bz + ((i == 0) ? 1 : (i == 1) ? -1 : 0); // Front / Back

							final float cx = calculateBlock(chunk.position.x, currX);
							final float cy = calculateBlock(chunk.position.y, currY);
							final float cz = calculateBlock(chunk.position.z, currZ);

							for (FaceUpdates face : faceUpdates) {
								if (face != null) {
									face.update(chunk, block, i, cx, cy, cz);
								}
							}
						}

						chunk.empty = false;
					}
				}
			}
		}
	}

	private void generate(final NoisePerlin noise) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					BlockTypes type = null;
					/*final int posX = (int) position.x + x;
					final int posY = (int) position.y + y;
					final int posZ = (int) position.z + z;

					float height = ((float) noise.eval((double) posX / 50d, (double) posY / 50d, (double) posZ / 50d) + 1f) * (float) CHUNK_SIZE;

					if (posY < height) {
						type = BlockTypes.get("game::stone");
					} else if (posY - 1f <= height) {
						if (posY <= SEA_LEVEL + 1) {
							type = BlockTypes.get("game::sand");
						} else {
							type = BlockTypes.get("game::grass");
						}
					} else {
						if (posY <= SEA_LEVEL) {
							type = BlockTypes.get("game::water");
						} else {
							type = null;
						}
					}*/

					type = BlockTypes.get("game::stone");

					if (type != null) {
						empty = false;
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
							BlockTypes type = null;

							if (rand <= 100) {
								type = BlockTypes.get("game::coalOre");
							} else if (rand > 100 && rand <= 150) {
								type = BlockTypes.get("game::ironOre");
							} else if (rand > 150 && rand <= 160) {
								type = BlockTypes.get("game::goldOre");
							}

							if (type != null) {
								blocks[x][z][y] = new Block(type, new Vector3f(calculateBlock(position.x, x), calculateBlock(position.y, y), calculateBlock(position.z, z)));
							}
						}
					}
				}
			}
		}
	}

	protected Block getBlock(final int x, final int y, final int z) {
		return blocks[x][z][y];
	}

	protected void putBlock(final Block block, final int x, final int y, final int z) {
		blocks[x][z][y] = block;
		forceUpdate = true;
	}

	protected void removeBlock(final int x, final int y, final int z) {
		blocks[x][z][y] = null;
		forceUpdate = true;
	}

	protected Vector3f getPosition() {
		return position;
	}

	protected Block[][][] getBlocks() {
		return blocks;
	}

	protected AABB getAABB() {
		return aabb;
	}

	protected boolean isVisible() {
		return visible;
	}

	protected void updateVisible() {
		this.visible = FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(aabb);
	}

	protected int getFaceCount() {
		return faceCount;
	}

	protected boolean getForceUpdate() {
		return forceUpdate;
	}

	protected void setForceUpdate(final boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	protected void resetFaceCount() {
		faceCount = 0;
	}

	protected void addFaceCount(final int newFaces) {
		faceCount += newFaces;
	}

	public boolean isEmpty() {
		return empty;
	}
}
