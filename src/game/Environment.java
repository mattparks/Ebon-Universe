package game;

import flounder.lights.*;
import game.entitys.*;

import java.util.*;

public class Environment {
	public static final float GRAVITY = -50.0f;

	private static Fog fog;
	private static List<Light> lights;
	private static List<Entity> entitys;

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
		Environment.entitys = new ArrayList<>();
	}

	public static void update() {
	}

	public static Fog getFog() {
		return fog;
	}

	public static List<Light> getLights() {
		return lights;
	}

	public static List<Entity> getEntitys() {
		return entitys;
	}
}
