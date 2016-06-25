package game.terrains;

import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.resources.*;
import flounder.space.*;
import game.*;
import game.models.*;

import java.util.*;

/**
 * Represents a terrain object/
 */
public class Terrain implements ISpatialObject {
	public static final MyFile TERRAINS_LOC = new MyFile(MyFile.RES_FOLDER, "terrain");

	//private QuadTree<Entity> entityQuadtree;

	private Vector3f position;
	private Vector3f rotation;
	private AABB aabb;
	private float[][] heights;
	private Model model;
	private TerrainTexturePack texturePack;
	private HeightsGenerator noise;

	/**
	 * Creates a new game terrain that the player and engine.entities can interact on.
	 *
	 * @param gridX The position on the x grid.
	 * @param gridZ The position on the z grid.
	 * @param texturePack The set of textures used when rendering the engine.terrains blend maps.
	 */
	public Terrain(int gridX, int gridZ, TerrainTexturePack texturePack) {
		//entityQuadtree = new QuadTree<>();

		this.texturePack = texturePack;
		position = new Vector3f(gridX * Environment.TERRAIN_SIZE, 0.0f, gridZ * Environment.TERRAIN_SIZE);
		rotation = new Vector3f(0, 0, 0); // TODO: Rotation!
		aabb = new AABB(
				new Vector3f(position.getX(), position.getY() - (Environment.TERRAIN_SIZE / 2.0f), position.getZ()),
				new Vector3f(position.getX() + Environment.TERRAIN_SIZE, position.getY() + (Environment.TERRAIN_SIZE / 2.0f), position.getZ() + Environment.TERRAIN_SIZE)
		);
		noise = new HeightsGenerator(gridX, gridZ, Environment.TERRAIN_VERTEX_COUNT);

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

		model = new Model(vertices, textureCoords, normals, indices);
	}

	private float getHeightFromMap(int x, int z) {
		return noise.generateHeight(x, z);
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
		float gridSquareSize = Environment.TERRAIN_SIZE / (heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoord = terrainX % gridSquareSize / gridSquareSize;
		float zCoord = terrainZ % gridSquareSize / gridSquareSize;
		float result;

		if (xCoord <= 1 - zCoord) {
			result = Maths.baryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			result = Maths.baryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}

		return result;
	}

	//public QuadTree<Entity> getEntityQuadtree() {
	//	return entityQuadtree;
	//}

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

	@Override
	public AABB getAABB() {
		return aabb;
	}

	/**
	 * @return Gets the engine.terrains position in world space.
	 */
	public Vector3f getPosition() {
		return position;
	}

	public class HeightsGenerator {
		private static final float AMPLITUDE = 75f;
		private static final int OCTAVES = 3;
		private static final float ROUGHNESS = 0.3f;

		private Random random = new Random();
		private int xOffset = 0;
		private int zOffset = 0;

		//only works with POSITIVE gridX and gridZ values!
		public HeightsGenerator(int gridX, int gridZ, int vertexCount) {
			xOffset = gridX * (vertexCount - 1);
			zOffset = gridZ * (vertexCount - 1);
		}

		public float generateHeight(int x, int z) {
			float total = 0;
			float d = (float) Math.pow(2, OCTAVES - 1);

			for (int i = 0; i < OCTAVES; i++) {
				float freq = (float) (Math.pow(2, i) / d);
				float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
				total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
			}

			return total;
		}

		private float getInterpolatedNoise(float x, float z) {
			int intX = (int) Math.abs(x);
			int intZ = (int) Math.abs(z);
			float fracX = Math.abs(x) - intX;
			float fracZ = Math.abs(z) - intZ;

			float v1 = getSmoothNoise(intX, intZ);
			float v2 = getSmoothNoise(intX + 1, intZ);
			float v3 = getSmoothNoise(intX, intZ + 1);
			float v4 = getSmoothNoise(intX + 1, intZ + 1);
			float i1 = interpolate(v1, v2, fracX);
			float i2 = interpolate(v3, v4, fracX);
			return interpolate(i1, i2, fracZ);
		}

		private float interpolate(float a, float b, float blend) {
			double theta = blend * Math.PI;
			float f = (float) (1.0f - Math.cos(theta)) * 0.5f;
			return a * (1.0f - f) + b * f;
		}

		private float getSmoothNoise(int x, int z) {
			float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1) + getNoise(x + 1, z + 1)) / 16.0f;
			float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8.0f;
			float center = getNoise(x, z) / 4f;
			return corners + sides + center;
		}

		private float getNoise(int x, int z) {
			random.setSeed(x * 49632 + z * 325176 + MainSeed.getSeed());
			return random.nextFloat() * 2.0f - 1.0f;
		}
	}
}
