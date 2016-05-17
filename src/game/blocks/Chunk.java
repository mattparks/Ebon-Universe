package game.blocks;

import flounder.engine.*;
import flounder.loaders.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;
import game.models.*;
import org.lwjgl.opengl.*;

import java.util.*;

public class Chunk extends AABB implements Comparable<Chunk> {
	public static final Vector3f ROTATION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f POSITION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f SCALE_REUSABLE = new Vector3f(0, 0, 0);

	public static final int CHUNK_SIZE = 16;

	private final Vector3f position;
	private final Block[][][] blocks;

	private final int vaoID;
	private int vaoLength;

	private boolean forceUpdate;
	private boolean visible;
	private boolean empty;

	protected Chunk(final Vector3f position, final PerlinNoise noise, final Random random) {
		this.position = position;
		this.blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE + 1];

		Vector3f.subtract(position, new Vector3f(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT), super.getMinExtents());
		Vector3f.subtract(new Vector3f(
				(position.x + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT),
				(position.y + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT),
				(position.z + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT)
		), new Vector3f(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT), super.getMaxExtents());

		this.vaoID = Loader.createVAO();
		this.vaoLength = 0;

		this.forceUpdate = true;
		this.visible = true;
		this.empty = true;

		generate(noise);
		populate(random);
		update();
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

	private void populate(final Random random) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y];

					if (block != null) {
						if (block.getType().equals("game::stone")) {
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
								blocks[x][z][y] = new Block(type.getName(), new Vector3f(calculateBlock(position.x, x), calculateBlock(position.y, y), calculateBlock(position.z, z)));
							}
						}
					}
				}
			}
		}
	}

	public static Block createBlock(final Chunk chunk, final int x, final int y, final int z, final BlockTypes type) {
		return new Block(type.getName(), new Vector3f(calculateBlock(chunk.position.x, x), calculateBlock(chunk.position.y, y), calculateBlock(chunk.position.z, z)));
	}

	protected static float calculateBlock(final float position, final int array) {
		return position + (2.0f * array * BlockTypes.BLOCK_EXTENT);
	}

	protected static int inverseBlock(final float position, final float component) {
		return (int) ((component - position) / (2.0f * BlockTypes.BLOCK_EXTENT));
	}

	public static boolean inBounds(final float x, final float y, final float z) {
		return !(x < 0 || y < 0 || z < 0 || x > CHUNK_SIZE - 1 || y > CHUNK_SIZE - 1 || z > CHUNK_SIZE - 1);
	}

	protected void update() {
		visible = FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(this);
		empty = true;

		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y];

					if (block != null) {
						block.update(this);
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
		// Gets all of the points for the chunk model.
		final List<Integer> chunkIndices = new ArrayList<>();
		final List<Float> chunkVertices = new ArrayList<>();
		final List<Float> chunkTextures = new ArrayList<>();
		final List<Float> chunkNormals = new ArrayList<>();
		final List<Float> chunkColours = new ArrayList<>();

		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					final Block block = blocks[x][z][y];

					if (block != null && !block.isCovered(this)) {
						if (chunkIndices.size() < 1) { // TODO: Remove!
							final Model model = BlockTypes.get(block.getType()).getModel();

							for (int i = 0; i < model.getIndices().length; i++) {
								chunkIndices.add(model.getIndices()[i]);
							}

							for (int i = 0; i < model.getVertices().length; i++) {
								chunkVertices.add(model.getVertices()[i]);
							}

							for (int i = 0; i < model.getTextures().length; i++) {
								chunkTextures.add(model.getTextures()[i]);
							}

							for (int i = 0; i < model.getNormals().length; i++) {
								chunkNormals.add(model.getNormals()[i]);
								chunkColours.add(model.getNormals()[i]);
							}
						}
					}
				}
			}
		}

		// Converts into array form.
		final int[] indices = new int[chunkIndices.size()];
		final float[] vertices = new float[chunkVertices.size()];
		final float[] textures = new float[chunkTextures.size()];
		final float[] normals = new float[chunkNormals.size()];
		final float[] colours = new float[chunkColours.size()];

		for (int i = 0; i < indices.length; i++) {
			indices[i] = chunkIndices.get(i);
		}

		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = chunkVertices.get(i);
		}

		for (int i = 0; i < textures.length; i++) {
			textures[i] = chunkTextures.get(i);
		}

		for (int i = 0; i < normals.length; i++) {
			normals[i] = chunkNormals.get(i);
		}

		for (int i = 0; i < colours.length; i++) {
			colours[i] = chunkColours.get(i);
		}

		// Loads into the VAO.
		Loader.createIndicesVBO(vaoID, indices);
		Loader.storeDataInVBO(vaoID, vertices, 0, 3);
		Loader.storeDataInVBO(vaoID, textures, 1, 2);
		Loader.storeDataInVBO(vaoID, normals, 2, 3);
		Loader.storeDataInVBO(vaoID, colours, 3, 3);
		GL30.glBindVertexArray(0);
		this.vaoLength = indices.length;
	}

	protected Matrix4f updateModelMatrix(final Matrix4f modelMatrix) {
		modelMatrix.setIdentity();
		POSITION_REUSABLE.set(position);
		ROTATION_REUSABLE.set(0.0f, 0.0f, 0.0f);
		SCALE_REUSABLE.set(1.0f, 1.0f, 1.0f);
		Matrix4f.transformationMatrix(POSITION_REUSABLE, ROTATION_REUSABLE, SCALE_REUSABLE, modelMatrix);
		return modelMatrix;
	}

	public boolean blockExists(final int x, final int y, final int z) {
		return inBounds(x, y, z) && getBlock(x, y, z) != null;
	}

	protected Block getBlock(final int x, final int y, final int z) {
		return blocks[x][z][y];
	}

	public boolean inChunkBounds(final float x, final float y, final float z) {
		return !(x < position.x || y < position.y || z < position.z || x > CHUNK_SIZE - 1 || y > CHUNK_SIZE - 1 || z > CHUNK_SIZE - 1);
	}

	protected void putBlock(final Block block, final int x, final int y, final int z) {
		removeBlock(x, y, z);

		if (block != null) {
			block.playPlaceNoise();
		}

		blocks[x][z][y] = block;
		forceUpdate = true;
	}

	protected void removeBlock(final int x, final int y, final int z) {
		if (blocks[x][z][y] != null) {
			blocks[x][z][y].playBreakNoise();
		}

		blocks[x][z][y] = null;
		forceUpdate = true;
	}

	protected Vector3f getPosition() {
		return position;
	}

	protected Block[][][] getBlocks() {
		return blocks;
	}

	protected int getVAO() {
		return vaoID;
	}

	protected int getVAOLength() {
		return vaoLength;
	}

	protected boolean isVisible() {
		return visible;
	}

	protected boolean getForceUpdate() {
		return forceUpdate;
	}

	public boolean isEmpty() {
		return empty;
	}

	@Override
	public int compareTo(final Chunk other) {
		final float thisToCamera = Vector3f.getDistanceSquared(FlounderEngine.getCamera().getPosition(), position);
		final float otherToCamera = Vector3f.getDistanceSquared(FlounderEngine.getCamera().getPosition(), other.position);
		return Float.compare(thisToCamera, otherToCamera);
	}
}
