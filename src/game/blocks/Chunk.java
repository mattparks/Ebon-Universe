package game.blocks;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.noise.*;
import flounder.physics.*;
import org.lwjgl.opengl.*;

import java.util.*;

public class Chunk extends AABB implements Comparable<Chunk> {
	public static final Vector3f ROTATION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f POSITION_REUSABLE = new Vector3f(0, 0, 0);
	public static final Vector3f SCALE_REUSABLE = new Vector3f(0, 0, 0);

	public static final int CHUNK_SIZE = 16;

	private Vector3f position;
	private Block[][][] blocks;

	private int vaoID;
	private int vaoLength;

	private boolean forceRemesh;
	private boolean visible;
	private boolean empty;

	protected Chunk(Vector3f position) {
		this.position = position;
		this.blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE + 1];

		Vector3f.subtract(position, new Vector3f(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT), super.getMinExtents());
		Vector3f.subtract(new Vector3f(
				(position.x + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT),
				(position.y + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT),
				(position.z + (2.0f * CHUNK_SIZE) * BlockTypes.BLOCK_EXTENT)
		), new Vector3f(BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT, BlockTypes.BLOCK_EXTENT), super.getMaxExtents());

		this.vaoID = FlounderEngine.getLoader().createVAO();
		this.vaoLength = 0;

		this.forceRemesh = true;
		this.visible = true;
		this.empty = true;
	}

	public void generate(PerlinNoise noise) {
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

		forceRemesh = true;
	}

	public void populate(Random random) {
		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					Block block = blocks[x][z][y];

					if (block != null) {
						if (block.getType().equals("game::stone")) {
							int rand = random.nextInt(10000);
							BlockTypes type = null;

							if (rand <= 100) {
								type = BlockTypes.get("game::coal_ore");
							} else if (rand > 100 && rand <= 150) {
								type = BlockTypes.get("game::iron_ore");
							} else if (rand > 150 && rand <= 160) {
								type = BlockTypes.get("game::gold_ore");
							}

							if (type != null) {
								blocks[x][z][y] = new Block(type.getName(), new Vector3f(calculateBlock(position.x, x), calculateBlock(position.y, y), calculateBlock(position.z, z)));
							}
						}
					}
				}
			}
		}

		forceRemesh = true;
	}

	public static Block createBlock(Chunk chunk, int x, int y, int z, BlockTypes type) {
		return new Block(type.getName(), new Vector3f(calculateBlock(chunk.position.x, x), calculateBlock(chunk.position.y, y), calculateBlock(chunk.position.z, z)));
	}

	protected static float calculateBlock(float position, int array) {
		return position + (2.0f * array * BlockTypes.BLOCK_EXTENT);
	}

	protected static int inverseBlock(float position, float component) {
		return (int) ((component - position) / (2.0f * BlockTypes.BLOCK_EXTENT));
	}

	public static boolean inBounds(float x, float y, float z) {
		return !(x < 0 || y < 0 || z < 0 || x > CHUNK_SIZE - 1 || y > CHUNK_SIZE - 1 || z > CHUNK_SIZE - 1);
	}

	protected void update() {
		visible = FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(this);
		empty = true;

		for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					Block block = blocks[x][z][y];

					if (block != null) {
						block.update(this);
						empty = false;
					}
				}
			}
		}

		if (forceRemesh) {
			remesh();
		}

		forceRemesh = false;
	}

	private void remesh() {
		// Gets all of the points for the chunk model.
		List<Float> chunkVertices = new ArrayList<>();
		List<Float> chunkTextures = new ArrayList<>();
		List<Float> chunkNormals = new ArrayList<>();
		List<Float> chunkColours = new ArrayList<>();

		/*for (int x = 0; x < blocks.length; x++) {
			for (int z = 0; z < blocks[x].length; z++) {
				for (int y = 0; y < blocks[z].length; y++) {
					 Block block = blocks[x][z][y];

					if (block != null && !block.isCovered(this)) {
						 BlockTypes types = BlockTypes.get(block.getType());

						if (types != null) {
							 Model model = types.getModel();

							for (int i = 0; i < model.getIndices().length; i++) {
								 int index = model.getIndices()[i];

								// TODO: Grab added number from block!
								chunkVertices.add(model.getVertices()[index] + (x * 2.0f * BlockTypes.BLOCK_EXTENT));
								chunkVertices.add(model.getVertices()[index + 1] + (y * 2.0f * BlockTypes.BLOCK_EXTENT));
								chunkVertices.add(model.getVertices()[index + 2] + (z * 2.0f * BlockTypes.BLOCK_EXTENT));

								chunkTextures.add(model.getTextures()[index]);
								chunkTextures.add(model.getTextures()[index + 1]);

								chunkNormals.add(model.getNormals()[index]);
								chunkNormals.add(model.getNormals()[index + 1]);
								chunkNormals.add(model.getNormals()[index + 2]);

								chunkColours.add(types.getColour().r);
								chunkColours.add(types.getColour().g);
								chunkColours.add(types.getColour().b);
							}
						}
					}
				}
			}
		}*/

		// TODO: Mesh merge!

		// Converts into array form.
		float[] vertices = new float[chunkVertices.size()];
		float[] textures = new float[chunkTextures.size()];
		float[] normals = new float[chunkNormals.size()];
		float[] colours = new float[chunkColours.size()];
		ArrayUtils.copyToArray(chunkVertices, vertices);
		ArrayUtils.copyToArray(chunkTextures, textures);
		ArrayUtils.copyToArray(chunkNormals, normals);
		ArrayUtils.copyToArray(chunkColours, colours);

		// Loads into the VAO.
		FlounderEngine.getLoader().storeDataInVBO(vaoID, vertices, 0, 3);
		FlounderEngine.getLoader().storeDataInVBO(vaoID, textures, 1, 2);
		FlounderEngine.getLoader().storeDataInVBO(vaoID, normals, 2, 3);
		FlounderEngine.getLoader().storeDataInVBO(vaoID, colours, 3, 3);
		GL30.glBindVertexArray(0);
		this.vaoLength = vertices.length / 3;
	}

	protected Matrix4f updateModelMatrix(Matrix4f modelMatrix) {
		modelMatrix.setIdentity();
		POSITION_REUSABLE.set(position);
		ROTATION_REUSABLE.set(0.0f, 0.0f, 0.0f);
		SCALE_REUSABLE.set(1.0f, 1.0f, 1.0f);
		Matrix4f.transformationMatrix(POSITION_REUSABLE, ROTATION_REUSABLE, SCALE_REUSABLE, modelMatrix);
		return modelMatrix;
	}

	public boolean blockExists(int x, int y, int z) {
		return inBounds(x, y, z) && getBlock(x, y, z) != null;
	}

	protected Block getBlock(int x, int y, int z) {
		return blocks[x][z][y];
	}

	public boolean inChunkBounds(float x, float y, float z) {
		return !(x < position.x || y < position.y || z < position.z || x > CHUNK_SIZE - 1 || y > CHUNK_SIZE - 1 || z > CHUNK_SIZE - 1);
	}

	protected void putBlock(Block block, int x, int y, int z) {
		removeBlock(x, y, z);

		if (block != null) {
			block.playPlaceNoise();
		}

		blocks[x][z][y] = block;
		forceRemesh = true;
	}

	protected void removeBlock(int x, int y, int z) {
		if (blocks[x][z][y] != null) {
			blocks[x][z][y].playBreakNoise();
		}

		blocks[x][z][y] = null;
		forceRemesh = true;
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

	public boolean isEmpty() {
		return empty;
	}

	@Override
	public int compareTo(Chunk other) {
		float thisToCamera = Vector3f.getDistanceSquared(FlounderEngine.getCamera().getPosition(), position);
		float otherToCamera = Vector3f.getDistanceSquared(FlounderEngine.getCamera().getPosition(), other.position);
		return Float.compare(thisToCamera, otherToCamera);
	}
}
