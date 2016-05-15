package blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.loaders.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;
import flounder.sounds.*;
import models.*;
import org.lwjgl.opengl.*;

import java.io.*;
import java.util.*;

public class Chunk {
	public static final Vector3f ROTATION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f POSITION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f SCALE_REUSABLE = new Vector3f(0, 0, 0);

	public static final int CHUNK_SIZE = 16;

	private final Vector3f position;
	private final Block[][][] blocks;
	private final AABB aabb;

	private final int vaoID;
	private int vertexCount;

	private boolean forceUpdate;
	private boolean visible;
	private boolean empty;

	protected Chunk(final Vector3f position, final PerlinNoise noise, final Random random) {
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

		this.vaoID = Loader.createVAO();
		this.vertexCount = 0;

		this.forceUpdate = true;
		this.visible = false;
		this.empty = true;

		generate(noise);
		populate(random);
		generateModel();
	}

	private void generate(final PerlinNoise noise) {
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

	public static Block createBlock(final Chunk chunk, final int x, final int y, final int z, final BlockTypes type) {
		return new Block(type, new Vector3f(calculateBlock(chunk.position.x, x), calculateBlock(chunk.position.y, y), calculateBlock(chunk.position.z, z)));
	}

	protected static float calculateBlock(final float position, final int array) {
		return position + (2.0f * array * BlockTypes.BLOCK_EXTENT);
	}

	private void populate(final Random random) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y];

					if (block != null) {
						if (Block.getType(block).getName().equals("game::stone")) {
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

	public static boolean blockExists(final Chunk chunk, final int x, final int y, final int z) {
		return inBounds(x, y, z) && Chunk.getBlock(chunk, x, y, z) != null;
	}

	public static boolean inChunkBounds(final Chunk chunk, final float x, final float y, final float z) {
		return !(x < chunk.position.x || y < chunk.position.y || z < chunk.position.z || x > CHUNK_SIZE - 1 || y > CHUNK_SIZE - 1 || z > CHUNK_SIZE - 1);
	}

	public static boolean inBounds(final float x, final float y, final float z) {
		return !(x < 0 || y < 0 || z < 0 || x > CHUNK_SIZE - 1 || y > CHUNK_SIZE - 1 || z > CHUNK_SIZE - 1);
	}

	protected static Block getBlock(final Chunk chunk, final int x, final int y, final int z) {
		return chunk.blocks[x][z][y];
	}

	protected void update() {
		visible = FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(aabb);
		empty = true;

		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y];

					if (block != null) {
						Block.update(block, this);
						empty = false;
					}
				}
			}
		}

		if (forceUpdate) {
			generateModel();
		}

		forceUpdate = false;
	}

	private void generateModel() {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y];

					if (block != null && Block.getCovered(block, this)) {
						final Model model = Block.getType(block).getModel();

					//	FlounderLogger.error("Indices = [ " + generateThingy(model.getIndices()) + " ];");
					//	FlounderLogger.error("Vertices = [ " + generateThingy(model.getVertices()) + " ];");
					//	FlounderLogger.error("Textures = [ " + generateThingy(model.getTextures()) + " ];");
					//	FlounderLogger.error("Normals = [ " + generateThingy(model.getNormals()) + " ];");

						vertexCount = model.getIndices().length;

						Loader.createIndicesVBO(vaoID, model.getIndices());
						Loader.storeDataInVBO(vaoID, model.getVertices(), 0, 3);
						Loader.storeDataInVBO(vaoID, model.getTextures(), 1, 2);
						Loader.storeDataInVBO(vaoID, model.getNormals(), 2, 3);
						Loader.storeDataInVBO(vaoID, model.getNormals(), 3, 3); // TODO: Colours
						GL30.glBindVertexArray(0);
						return;
					}
				}
			}
		}

		// TODO: Generate a model!
	}

	private String generateThingy(final int[] thingy) {
		String result = "";

		for (int i = 0;i<thingy.length;i++) {
			result += thingy[i] + ",";
		}

		return result;
	}

	private String generateThingy(final float[] thingy) {
		String result = "";

		for (int i = 0;i<thingy.length;i++) {
			result += thingy[i] + "f,";
		}

		return result;
	}

	protected static Matrix4f updateModelMatrix(final Chunk chunk, final Matrix4f modelMatrix) {
		modelMatrix.setIdentity();
		POSITION_REUSABLE.set(chunk.position);
		ROTATION_REUSABLE.set(0.0f, 0.0f, 0.0f);
		SCALE_REUSABLE.set(1.0f, 1.0f, 1.0f);
		Matrix4f.transformationMatrix(POSITION_REUSABLE, ROTATION_REUSABLE, SCALE_REUSABLE, modelMatrix);
		return modelMatrix;
	}

	protected static int inverseBlock(final float position, final float component) {
		return (int) ((component - position) / (2.0f * BlockTypes.BLOCK_EXTENT));
	}

	protected static Vector3f getPosition(final Chunk chunk) {
		return chunk.position;
	}

	protected static void putBlock(final Chunk chunk, final Block block, final int x, final int y, final int z) {
		if (chunk.blocks[x][z][y] != null) {
			ManagerDevices.getSound().play3DSound(PlayRequest.new3dSoundPlayRequest(Block.getType(chunk.blocks[x][z][y]).getBreakSound(), 1.0f, Block.getPosition(chunk.blocks[x][z][y]), 16.0f, 64.0f));
		}

		if (block != null) {
			ManagerDevices.getSound().play3DSound(PlayRequest.new3dSoundPlayRequest(Block.getType(block).getPlaceSound(), 1.0f, Block.getPosition(block), 16.0f, 64.0f));
		}

		chunk.blocks[x][z][y] = block;
		chunk.forceUpdate = true;
	}

	protected static void removeBlock(final Chunk chunk, final int x, final int y, final int z) {
		if (chunk.blocks[x][z][y] != null) {
			ManagerDevices.getSound().play3DSound(PlayRequest.new3dSoundPlayRequest(Block.getType(chunk.blocks[x][z][y]).getBreakSound(), 1.0f, Block.getPosition(chunk.blocks[x][z][y]), 16.0f, 64.0f));
		}

		chunk.blocks[x][z][y] = null;
		chunk.forceUpdate = true;
	}

	protected static Block[][][] getBlocks(final Chunk chunk) {
		return chunk.blocks;
	}

	protected static AABB getAABB(final Chunk chunk) {
		return chunk.aabb;
	}

	protected static int getVAO(final Chunk chunk) {
		return chunk.vaoID;
	}

	protected static int getVertexCount(final Chunk chunk) {
		return chunk.vertexCount;
	}

	protected static boolean isVisible(final Chunk chunk) {
		return chunk.visible;
	}

	protected static boolean getForceUpdate(final Chunk chunk) {
		return chunk.forceUpdate;
	}

	public static boolean isEmpty(final Chunk chunk) {
		return chunk.empty;
	}
}
