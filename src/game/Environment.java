package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.lights.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;
import game.terrains.*;

import java.util.*;

public class Environment {
	public static final float GRAVITY = -50.0f;

	public static final float TERRAIN_SIZE = 512.0f;
	public static final int TERRAIN_VERTEX_COUNT = 32;

	public static final float WATER_SIZE = TERRAIN_SIZE / 2.0f;
	public static final float WATER_Y_POS = 0.0f;

	private static Fog fog;
	private static Light sun;

	private static StructureBasic<Terrain> terrainTree;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 * @param sun The games main sunlight emitter.
	 */
	public static void init(Fog fog, Light sun) {
		Environment.terrainTree = new StructureBasic<>();
		Environment.fog = fog;
		Environment.sun = sun;

		generateWorld();
	}

	private static void generateWorld() {
		TerrainTexturePack terrainTexturePack = new TerrainTexturePack(Texture.newTexture(new MyFile(Terrain.TERRAINS_LOC, "grass.png")).create(), Texture.newTexture(new MyFile(Terrain.TERRAINS_LOC, "mud.png")).create(), Texture.newTexture(new MyFile(Terrain.TERRAINS_LOC, "flowers.png")).create(), Texture.newTexture(new MyFile(Terrain.TERRAINS_LOC, "path.png")).create());
		terrainTree.add(new Terrain(0, 0, terrainTexturePack));
		terrainTree.add(new Terrain(1, 0, terrainTexturePack));
		terrainTree.add(new Terrain(0, 1, terrainTexturePack));
		terrainTree.add(new Terrain(1, 1, terrainTexturePack));
	}

	public static void update() {
		List<Terrain> terrainList = new ArrayList<>();

		for (Terrain t : terrainTree.getAll(terrainList)) {
			FlounderEngine.getAABBs().addAABBRender(t.getAABB());
		}
	}

	/**
	 * Gets the terrain at a point.
	 *
	 * @param x Points X coord.
	 * @param z Points Z coord.
	 *
	 * @return Returns the terrain at that point.
	 */
	public static Terrain getTerrainAtPoint(float x, float z) {
		List<Terrain> terrainList = new ArrayList<>();

		for (Terrain t : terrainTree.getAll(terrainList)) {
			if (t.getHeightWorld(x, z) != 0) {
				return t;
			}
		}

		return null;
	}

	/**
	 * Gets the height of the terrain that the point is found to intersect.
	 *
	 * @param x Points X coord.
	 * @param z Points Z coord.
	 *
	 * @return Returns the Y height found at the point.
	 */
	public static float getTerrainHeight(float x, float z) {
		float height = 0;
		Terrain terrain = getTerrainAtPoint(x, z);

		if (terrain != null) {
			height = terrain.getHeightWorld(x, z);
		}

		return height;
	}

	public static Fog getFog() {
		return fog;
	}

	public static Light getSun() {
		return sun;
	}

	public static List<Terrain> getTerrainObjects(ICamera camera) {
		List<Terrain> terrainList = new ArrayList<>();
		return terrainTree.queryInFrustum(terrainList, camera.getViewFrustum());
	}
}
