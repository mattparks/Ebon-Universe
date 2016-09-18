package game;

import flounder.lights.*;
import flounder.space.*;
import game.celestial.manager.*;
import game.entities.*;

import java.util.*;

public class Environment {
	private static Fog fog;
	private static List<Light> lights;
	private static ISpatialStructure<Entity> entityQuadtree;
	private static GalaxyManager galaxyManager;

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
		Environment.galaxyManager = new GalaxyManager();
	}

	public static void update() {
		if (entityQuadtree != null) {
			for (Entity entity : entityQuadtree.getAll(new ArrayList<>())) {
				entity.update();
			}
		}

		if (galaxyManager != null) {
			galaxyManager.update();
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

	public static GalaxyManager getGalaxyManager() {
		return galaxyManager;
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

		if (galaxyManager != null) {
			galaxyManager.destroy();
			galaxyManager = null;
		}
	}
}
