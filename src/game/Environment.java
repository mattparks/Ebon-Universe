package game;

import flounder.engine.implementation.*;
import flounder.lights.*;
import flounder.resources.*;
import flounder.textures.*;
import game.terrains.*;
import game.waters.*;
import game.world.*;

import java.util.*;

public class Environment {
	public static final float GRAVITY = -50.0f;

	public static final float TERRAIN_SIZE = 512.0f;
	public static final int TERRAIN_VERTEX_COUNT = 32;

	public static final float WATER_SIZE = TERRAIN_SIZE / 2.0f;
	public static final float WATER_Y_POS = 0.0f;
	public static final TerrainTexturePack TERRAIN_TEXTURE_PACK = new TerrainTexturePack(Texture.newTexture(new MyFile(Terrain.TERRAINS_LOC, "grass.png")).create(), Texture.newTexture(new MyFile(Terrain.TERRAINS_LOC, "mud.png")).create(), Texture.newTexture(new MyFile(Terrain.TERRAINS_LOC, "flowers.png")).create(), Texture.newTexture(new MyFile(Terrain.TERRAINS_LOC, "path.png")).create());

	private static Fog fog;
	private static Light sun;
	private static World world;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 * @param sun The games main sunlight emitter.
	 */
	public static void init(Fog fog, Light sun) {
		Environment.world = new World();
		Environment.fog = fog;
		Environment.sun = sun;
	}

	public static void update() {
		world.update();
	}

	public static Fog getFog() {
		return fog;
	}

	public static Light getSun() {
		return sun;
	}

	public static List<Terrain> getTerrainObjects(ICamera camera) {
		List<Terrain> terrainList = new ArrayList<>();
		world.getChunks(camera).forEach(chunk -> {
			if (chunk.inPlayerRange()) {
				terrainList.add(chunk.getTerrain());
			}
		});
		return terrainList;
	}

	public static List<Water> getWaterObjects(ICamera camera) {
		List<Water> waterList = new ArrayList<>();
		world.getChunks(camera).forEach(chunk -> {
			if (chunk.inPlayerRange()) {
				waterList.add(chunk.getWater());
			}
		});
		return waterList;
	}
}
