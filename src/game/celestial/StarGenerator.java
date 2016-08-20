package game.celestial;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.vectors.*;

import java.util.*;

public class StarGenerator {
	public static final float STARS_PER_LY3 = 0.004f; // Stars/ Light Year ^ 3, Real galactic distribution.

	public static void testGenerate() {
		List<Star> stars = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			Star star = generateStar(new Vector3f());
			stars.add(star);
			FlounderEngine.getLogger().log(star.toString());
		}
	}

	private static float generateValue(float average, float errorMargin) {
		float offset = (Maths.RANDOM.nextFloat() - 0.5f) * 2.0f * errorMargin;
		return average + offset;
	}

	public static Star generateStar(Vector3f position) {
		float spawnKey = Maths.RANDOM.nextFloat() * 100.0f; // TODO: Perlin noise.
		Star.StarType starType = Star.StarType.getTypeMakeup(spawnKey);
		List<Planet> planets = generatePlanets(starType);
		return new Star(FauxGenerator.getFauxSentance(2, 4, 12), starType.solarMasses, position, planets);
	}

	public static List<Planet> generatePlanets(Star.StarType starType) {
		List<Planet> planets = new ArrayList<>();
		return planets;
	}
}
