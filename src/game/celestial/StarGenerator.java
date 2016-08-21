package game.celestial;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.vectors.*;

import java.util.*;

public class StarGenerator {
	public static final float STARS_PER_LY3 = 0.004f; // Stars/Light Year^3, Real galactic distribution.

	public static void testGenerate() {
		FlounderEngine.getLogger().log("\n======= TESTS =======");
		List<Star> stars = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			Star star = generateStar(new Vector3f());
			stars.add(star);
		}

		FlounderEngine.getLogger().log("======= END =======\n");
	}

	private static float generateValue(float average, float errorMargin) {
		float offset = (Maths.RANDOM.nextFloat() - 0.5f) * 2.0f * errorMargin;
		return average + offset;
	}

	public static Star generateStar(Vector3f position) {
		float spawnKey = Maths.RANDOM.nextFloat() * 100.0f; // TODO: Perlin noise.
		Star.StarType starType = Star.StarType.getTypeMakeup(spawnKey);
		Star star = new Star(FauxGenerator.getFauxSentance(2, 4, 7), starType.solarMasses, position, new ArrayList<>());
		FlounderEngine.getLogger().log(star.toString());
		generatePlanets(star);
		return star;
	}

	public static void generatePlanets(Star star) {
		Orbit orbit = new Orbit(
				0.0934f, 1.524f, star.getSolarMasses(),
				Maths.RANDOM.nextInt(30) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f);
		Celestial celestial = new Celestial(
				FauxGenerator.getFauxSentance(2, 4, 12), star, orbit,
				0.1069f, 0.5319f, Maths.RANDOM.nextInt(400) * (Maths.RANDOM.nextBoolean() ? 1 : -1) / 10.0f);
		FlounderEngine.getLogger().log(celestial.toString());
		star.getCelestials().add(celestial);
	}
}
