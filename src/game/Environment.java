package game;

import flounder.lights.*;
import game.blocks.*;

public class Environment {
	public static final float GRAVITY = -50;

	private static Fog fog;
	private static Light sun;
	private static BlocksManager blocksManager;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 * @param sun The games main sunlight emitter.
	 */
	public static void init(final Fog fog, final Light sun) {
		Environment.fog = fog;
		Environment.sun = sun;
		blocksManager = new BlocksManager();
	}

	public static void update() {
		blocksManager.update();
	}

	public static Fog getFog() {
		return fog;
	}

	public static Light getSun() {
		return sun;
	}

	public static BlocksManager getBlocksManager() {
		return blocksManager;
	}
}
