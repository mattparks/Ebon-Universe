package game;

import flounder.lights.*;

public class Environment {
	public static final float GRAVITY = -50;

	private static Fog fog;
	private static Light sun;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 * @param sun The games main sunlight emitter.
	 */
	public static void init(final Fog fog, final Light sun) {
		Environment.fog = fog;
		Environment.sun = sun;
	}

	public static Fog getFog() {
		return fog;
	}

	public static Light getSun() {
		return sun;
	}
}
