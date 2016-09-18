package game.celestial.manager;

import flounder.maths.vectors.*;

/**
 * A class that contains functions for converting between world space units.
 */
public class SpaceConversions {
	/**
	 * Converts from galaxy space to star space.
	 *
	 * @param galaxySpace Galaxy space.
	 *
	 * @return Star space.
	 */
	public static Vector3f fromGalaxyToStar(Vector3f galaxySpace) {
		return new Vector3f(galaxySpace); // TODO: LY->AU
	}

	/**
	 * Converts from star space to galaxy space.
	 *
	 * @param starSpace Star space.
	 *
	 * @return Galaxy space.
	 */
	public static Vector3f fromStarToGalaxy(Vector3f starSpace) {
		return new Vector3f(starSpace); // TODO: AU->LY
	}

	/**
	 * Converts from star space to celestial space.
	 *
	 * @param starSpace Star space.
	 *
	 * @return Celestial space.
	 */
	public static Vector3f fromStarToCelestial(Vector3f starSpace) {
		return new Vector3f(starSpace); // TODO: AU->KM
	}

	/**
	 * Converts from celestial space to star space.
	 *
	 * @param celestialSpace Celestial space.
	 *
	 * @return Star space.
	 */
	public static Vector3f fromCelestialToStar(Vector3f celestialSpace) {
		return new Vector3f(celestialSpace); // TODO: KM-AU
	}
}
