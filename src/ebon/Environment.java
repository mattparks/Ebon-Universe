package ebon;

import ebon.celestial.manager.*;
import ebon.entities.*;
import flounder.lights.*;
import flounder.space.*;

import java.util.*;

/**
 * A class that represents the game environment.
 */
public class Environment {
	private static Fog fog;
	private static StructureBasic<Light> lights;
	private static StructureBasic<Entity> entityStructure;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 * @param sun The games main sunlight emitter.
	 */
	public static void init(Fog fog, Light sun) {
		Environment.fog = fog;
		Environment.lights = new StructureBasic<>();
		Environment.entityStructure = new StructureBasic<>();
		Environment.lights.add(sun);
	}

	/**
	 * Called when the game world is needed to be created.
	 */
	public static void createWorld() {
		GalaxyManager.generateGalaxy();
	}

	/**
	 * Updates the environment.
	 */
	public static void update() {
		if (entityStructure != null) {
			entityStructure.getAll(new ArrayList<>()).forEach(Entity::update);
		}
	}

	public static Fog getFog() {
		return fog;
	}

	public static StructureBasic<Light> getLights() {
		return lights;
	}

	public static StructureBasic<Entity> getEntities() {
		return entityStructure;
	}

	/**
	 * Destroys the environment.
	 */
	public static void destroy() {
		fog = null;

		if (lights != null) {
			lights.clear();
			lights = null;
		}

		if (entityStructure != null) {
			entityStructure.clear();
			entityStructure = null;
		}

		GalaxyManager.clear();
	}
}
