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
		return inChunkBounds(x, y, z) && Chunk.getBlock(chunk, x, y, z) != null;
	}

	protected static void update(final Chunk chunk, final UpdateFaces... faceUpdates) {
		chunk.empty = true;

		for (int x = 0; x < chunk.blocks.length; x++) {
			for (int z = 0; z < chunk.blocks[x].length; z++) {
				for (int y = 0; y < chunk.blocks[z].length; y++) {
					final Block block = chunk.blocks[x][z][y];

					if (block != null) {
						block.setVisible(FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(block.getAABB()));

						if (faceUpdates != null && faceUpdates.length != 0) {
							for (int f = 0; f < 6; f++) {
								final int currZ = Chunk.inverseBlock(Chunk.getPosition(chunk).z, block.getPosition().z) + ((f == 0) ? 1 : (f == 1) ? -1 : 0); // Front / Back
								final int currX = Chunk.inverseBlock(Chunk.getPosition(chunk).x, block.getPosition().x) + ((f == 2) ? -1 : (f == 3) ? 1 : 0); // Left / Right
								final int currY = Chunk.inverseBlock(Chunk.getPosition(chunk).y, block.getPosition().y) + ((f == 4) ? 1 : (f == 5) ? -1 : 0); // Up / Down
								final float cz = Chunk.calculateBlock(Chunk.getPosition(chunk).z, currZ);
								final float cx = Chunk.calculateBlock(Chunk.getPosition(chunk).x, currX);
								final float cy = Chunk.calculateBlock(Chunk.getPosition(chunk).y, currY);

								for (final UpdateFaces faceUpdate : faceUpdates) {
									if (faceUpdate != null) {
										faceUpdate.update(chunk, block, f, cx, cy, cz);
									}
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

	protected static Block getBlock(final Chunk chunk, final int x, final int y, final int z) {
		return chunk.blocks[x][z][y];
	}

	protected static void putBlock(final Chunk chunk, final Block block, final int x, final int y, final int z) {
		chunk.blocks[x][z][y] = block;
		chunk.forceUpdate = true;
	}

	protected static void removeBlock(final Chunk chunk, final int x, final int y, final int z) {
		chunk.blocks[x][z][y] = null;
		chunk.forceUpdate = true;
	}

	protected static Vector3f getPosition(final Chunk chunk) {
		return chunk.position;
	}

	protected static Block[][][] getBlocks(final Chunk chunk) {
		return chunk.blocks;
	}

	protected static AABB getAABB(final Chunk chunk) {
		return chunk.aabb;
	}

	protected static boolean isVisible(final Chunk chunk) {
		return chunk.visible;
	}

	protected static void updateVisibility(final Chunk chunk) {
		chunk.visible = FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(chunk.aabb);
	}


	protected static int getFaceCount(final Chunk chunk) {
		int faces = 0;

		for (int x = 0; x < chunk.blocks.length; x++) {
			for (int z = 0; z < chunk.blocks[x].length; z++) {
				for (int y = 0; y < chunk.blocks[z].length; y++) {
					final Block block = chunk.blocks[x][z][y];

					if (block != null) {
						faces += block.getVisibleFaces();
					}
				}
			}
		}

		return faces;
	}

	protected static boolean getForceUpdate(final Chunk chunk) {
		return chunk.forceUpdate;
	}

	protected static void setForceUpdate(final Chunk chunk, final boolean forceUpdate) {
		chunk.forceUpdate = forceUpdate;
	}

	public static boolean isEmpty(final Chunk chunk) {
		return chunk.empty;
	}
}
