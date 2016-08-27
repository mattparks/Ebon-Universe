package game;

import flounder.lights.*;
import flounder.space.*;
import game.entities.*;
import game.terrains.*;

import java.util.*;

public class Environment {
	public static final float TERRAIN_SIZE = 5100f;
	public static final int TERRAIN_VERTEX_COUNT = 156;

	public static final float GRAVITY = -50.0f;

	private static Fog fog;
	private static List<Light> lights;
	private static StructureBasic<Entity> entityQuadtree;
	private static StructureBasic<Terrain> terrainQuadtree;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 * @param sun The games main sunlight emitter.
	 */
	public static void init(Fog fog, Light sun) {
		Environment.fog = fog;
		Environment.lights = new ArrayList<>();
		Environment.lights.add(sun);
		Environment.entityQuadtree = new StructureBasic<>();
		Environment.terrainQuadtree = new StructureBasic<>();
	}

	public static void update() {
		if (entityQuadtree != null) {
			for (Entity entity : entityQuadtree.getAll(new ArrayList<>())) {
				entity.update();
			}
		}
	}

	public static Fog getFog() {
		return fog;
	}

	public static List<Light> getLights() {
		return lights;
	}

	public static ISpatialStructure<Entity> getEntities() {
		return entityQuadtree;
	}

	public static StructureBasic<Terrain> getTerrains() {
		return terrainQuadtree;
	}

	public static void destroy() {
		fog = null;

		if (lights != null) {
			lights.clear();
			lights = null;
		}

		if (entityQuadtree != null) {
			entityQuadtree.clear();
			entityQuadtree = null;
		}

		if (terrainQuadtree != null) {
			terrainQuadtree.clear();
			terrainQuadtree = null;
		}
	}
}
