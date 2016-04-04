package game.world;

public class Environment {
	public static final float GRAVITY = -50;

	private static Fog fog;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 */
	public static void init(Fog fog) {
		Environment.fog = fog;
	}

	public static Fog getFog() {
		return fog;
	}
}
