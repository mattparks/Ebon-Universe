package game;

import flounder.lights.*;
import flounder.space.*;
import game.entities.*;

import java.util.*;

public class Environment {
	public static final float GRAVITY = -50.0f;

	private static Fog fog;
	private static List<Light> lights;
	private static StructureBasic<Entity> entityQuadtree;

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
	}

	public static void update() {
		for (Entity entity : entityQuadtree.getAll(new ArrayList<>())) {
			entity.update();
		}
	}

	public static Fog getFog() {
		return fog;
	}

	public static List<Light> getLights() {
		return lights;
	}

	public static ISpatialStructure<Entity> getEntitys() {
		return entityQuadtree;
	}
}
