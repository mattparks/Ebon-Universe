package game.terrains;

import flounder.materials.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.noise.*;
import flounder.physics.*;
import flounder.resources.*;
import flounder.space.*;
import game.*;
import game.entities.*;
import game.entities.components.*;

/**
 * Represents a terrain object.
 */
public class Terrain implements ISpatialObject {
	public static final MyFile TERRAINS_LOC = new MyFile(MyFile.RES_FOLDER, "terrain");

	private StructureBasic<Entity> entityQuadtree;

	private Vector3f position;
	private Vector3f rotation;
	private Matrix4f modelMatrix;

	private float[][] heights;
	private Model model;
	private TerrainTexturePack texturePack;
	private PerlinNoise noise;

	/**
	 * Creates a new game terrain that the player and engine.entities can interact on.
	 *
	 * @param gridX The position on the x grid.
	 * @param gridZ The position on the z grid.
	 * @param texturePack The set of textures used when rendering the engine.terrains blend maps.
	 */
	public Terrain(float gridX, float gridZ, TerrainTexturePack texturePack) {
		entityQuadtree = new StructureBasic<>();

		this.texturePack = texturePack;
		position = new Vector3f(gridX * Environment.TERRAIN_SIZE, 0.0f, gridZ * Environment.TERRAIN_SIZE);
		rotation = new Vector3f(0.0f, 0.0f, 0.0f); // TODO: Rotation!
		modelMatrix = new Matrix4f();
		noise = new PerlinNoise((int) MainSeed.getSeed());

		generateTerrain();
	}

	private void generateTerrain() {
		heights = new float[Environment.TERRAIN_VERTEX_COUNT][Environment.TERRAIN_VERTEX_COUNT];

		int count = Environment.TERRAIN_VERTEX_COUNT * Environment.TERRAIN_VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (Environment.TERRAIN_VERTEX_COUNT - 1) * (Environment.TERRAIN_VERTEX_COUNT - 1)];
		int vertexPointer = 0;

		for (int i = 0; i < Environment.TERRAIN_VERTEX_COUNT; i++) {
			for (int j = 0; j < Environment.TERRAIN_VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = j / ((float) Environment.TERRAIN_VERTEX_COUNT - 1) * Environment.TERRAIN_SIZE;
				float height = getHeightFromMap(j, i);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i / ((float) Environment.TERRAIN_VERTEX_COUNT - 1) * Environment.TERRAIN_SIZE;
				Vector3f normal = calculateMapNormal(j, i);
				normals[vertexPointer * 3] = normal.getX();
				normals[vertexPointer * 3 + 1] = normal.getY();
				normals[vertexPointer * 3 + 2] = normal.getZ();
				textureCoords[vertexPointer * 2] = j / ((float) Environment.TERRAIN_VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = i / ((float) Environment.TERRAIN_VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}

		int pointer = 0;

		for (int gz = 0; gz < Environment.TERRAIN_VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < Environment.TERRAIN_VERTEX_COUNT - 1; gx++) {
				int topLeft = gz * Environment.TERRAIN_VERTEX_COUNT + gx;
				int topRight = topLeft + 1;
				int bottomLeft = (gz + 1) * Environment.TERRAIN_VERTEX_COUNT + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}

		model = Model.newModel(new ModelBuilder.LoadManual() {
			@Override
			public String getModelName() {
				return "terrain" + position.x + position.y + position.z;
			}

			@Override
			public float[] getVertices() {
				return vertices;
			}

			@Override
			public float[] getTextureCoords() {
				return textureCoords;
			}

			@Override
			public float[] getNormals() {
				return normals;
			}

			@Override
			public float[] getTangents() {
				return null;
			}

			@Override
			public int[] getIndices() {
				return indices;
			}

			@Override
			public Material[] getMaterials() {
				return new Material[0];
			}
		}).create();
	}

	private float getHeightFromMap(int x, int y) {
		float spread = 8.25f;
		float xTight = 0.75f;
		float yTight = 0.50f;
		float scale = 100.0f;
		return noise.tileableNoise2((position.x + x) / spread * xTight, (position.y + y) / spread * yTight, Environment.TERRAIN_VERTEX_COUNT, Environment.TERRAIN_VERTEX_COUNT) * scale;
	}

	private Vector3f calculateMapNormal(int x, int y) {
		float heightL = getHeightFromMap(x - 1, y);
		float heightR = getHeightFromMap(x + 1, y);
		float heightD = getHeightFromMap(x, y - 1);
		float heightU = getHeightFromMap(x, y + 1);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalize();
		return normal;
	}

	/**
	 * Gets the height of the terrain from a world coordinate.
	 *
	 * @param worldX World coordinate in the X.
	 * @param worldZ World coordinate in the Z.
	 *
	 * @return Returns the height at that spot.
	 */
	public float getHeightWorld(float worldX, float worldZ) {
		float terrainX = worldX - position.getX();
		float terrainZ = worldZ - position.getZ();
		float gridSquareSize = Environment.TERRAIN_SIZE / (heights.length - 1.0f);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1.0f || gridZ >= heights.length - 1.0f || gridX < 0.0f || gridZ < 0.0f) {
			return 0.0f;
		}

		float xCoord = terrainX % gridSquareSize / gridSquareSize;
		float zCoord = terrainZ % gridSquareSize / gridSquareSize;
		float result;

		if (xCoord <= 1.0f - zCoord) {
			result = Maths.baryCentric(new Vector3f(0.0f, heights[gridX][gridZ], 0.0f), new Vector3f(1.0f, heights[gridX + 1][gridZ], 0.0f), new Vector3f(0.0f, heights[gridX][gridZ + 1], 1.0f), new Vector2f(xCoord, zCoord));
		} else {
			result = Maths.baryCentric(new Vector3f(1.0f, heights[gridX + 1][gridZ], 0.0f), new Vector3f(1.0f, heights[gridX + 1][gridZ + 1], 1.0f), new Vector3f(0.0f, heights[gridX][gridZ + 1], 1.0f), new Vector2f(xCoord, zCoord));
		}

		return result;
	}

	public ISpatialStructure<Entity> getEntityQuadtree() {
		return entityQuadtree;
	}

	/**
	 * Gets the entitys model matrix.
	 *
	 * @return The entitys model matrix.
	 */
	public Matrix4f getModelMatrix() {
		modelMatrix.setIdentity();
		Matrix4f.transformationMatrix(position, rotation, 1.0f, modelMatrix);
		return modelMatrix;
	}

	/**
	 * @return Gets the engine.terrains total rotation on the XYZ axes.
	 */
	public Vector3f getRotation() {
		return rotation;
	}

	/**
	 * @return Gets the actual model behind the terrain.
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @return Gets the terrain texture pack used on this terrain.
	 */
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	/**
	 * @return Gets the engine.terrains position in world space.
	 */
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public AABB getAABB() {
		return new AABB(new Vector3f(getPosition().getX(), getPosition().getY(), getPosition().getZ()), new Vector3f(getPosition().getX() + Environment.TERRAIN_SIZE, getPosition().getY() + Environment.TERRAIN_SIZE, getPosition().getZ() + Environment.TERRAIN_SIZE));
	}
}
