package ebon.universe;

import flounder.maths.vectors.*;

/**
 * A class that contains functions for converting between world space units.
 */
public class SpaceConversions {
	public static float LY_TO_AU = 63241.1f; // The conversion from LY to AU.
	public static float AU_TO_LY = 1.58125e-5f; // The conversion from AU to LY.

	public static float AU_TO_KM = 1.496e+8f; // The conversion from AU to KM.
	public static float KM_TO_AU = 6.68459e-9f; // The conversion from KM to AU.

	/**
	 * Converts from galaxy space to star space.
	 *
	 * @param galaxySpace Galaxy space.
	 *
	 * @return Star space.
	 */
	public static Vector3f fromGalaxyToStar(Vector3f galaxySpace) {
		return new Vector3f(galaxySpace.x * LY_TO_AU, galaxySpace.y * LY_TO_AU, galaxySpace.z * LY_TO_AU); // LY->AU
	}

	/**
	 * Converts from star space to galaxy space.
	 *
	 * @param starSpace Star space.
	 *
	 * @return Galaxy space.
	 */
	public static Vector3f fromStarToGalaxy(Vector3f starSpace) {
		return new Vector3f(starSpace.x * AU_TO_LY, starSpace.y * AU_TO_LY, starSpace.z * AU_TO_LY); // AU->LY
	}

	/**
	 * Converts from star space to celestial space.
	 *
	 * @param starSpace Star space.
	 *
	 * @return Celestial space.
	 */
	public static Vector3f fromStarToCelestial(Vector3f starSpace) {
		return new Vector3f(starSpace.x * AU_TO_KM, starSpace.y * AU_TO_KM, starSpace.z * AU_TO_KM); // AU->KM
	}

	/**
	 * Converts from celestial space to star space.
	 *
	 * @param celestialSpace Celestial space.
	 *
	 * @return Star space.
	 */
	public static Vector3f fromCelestialToStar(Vector3f celestialSpace) {
		return new Vector3f(celestialSpace.x * KM_TO_AU, celestialSpace.y * KM_TO_AU, celestialSpace.z * KM_TO_AU); // KM-AU
	}
}
