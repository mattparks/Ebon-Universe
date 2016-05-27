package game;

import flounder.maths.*;

public class MainSeed {
	private static long seed = Maths.RANDOM.nextInt(1000000);

	/**
	 * @return Returns the current seed being used.
	 */
	public static long getSeed() {
		return MainSeed.seed;
	}
}
