package ebon;

import flounder.maths.*;

public class EbonSeed {
	private static long SEED = Maths.RANDOM.nextInt(Maths.RANDOM.nextInt(100000));

	public static void randomize() {
		SEED = Maths.RANDOM.nextInt(Maths.RANDOM.nextInt(100000));
	}

	/**
	 * @return Returns the current seed being used.
	 */
	public static long getSeed() {
		return SEED;
	}
}
