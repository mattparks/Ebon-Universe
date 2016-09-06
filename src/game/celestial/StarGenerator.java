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
		Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), starType.solarMasses, position, new ArrayList<>());
		FlounderEngine.getLogger().log("===== Star " + star.getStarName() + ". =====");
		FlounderEngine.getLogger().log(star.toString());

		float currentOrbit = star.getPlanetInnerLimit();

		while (currentOrbit < star.getPlanetOuterLimit()) {
			if (Maths.RANDOM.nextBoolean()) {
				generateCelestial(new Pair<>(star, null), currentOrbit);
			}

			currentOrbit += Maths.randomInRange(2.4f, 3.0f);
		}

		FlounderEngine.getLogger().log("===== End of star " + star.getStarName() + ". =====\n");

		return star;
	}

	private static void generateCelestial(Pair<Star, Celestial> parentTypes, float semiMajorAxis) {
		Star star = null;

		if (parentTypes.getFirst() != null) {
			star = parentTypes.getFirst();
		} else if (parentTypes.getSecond() != null) {
			star = parentTypes.getSecond().getParentStar();
		}

		if (star == null) {
			return;
		}

		Orbit orbit = new Orbit(
				Maths.RANDOM.nextInt(6000) / 10000.0f, 1.524f, star.getSolarMasses(),
				semiMajorAxis, Maths.RANDOM.nextInt(3600) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f
		);

		float earthRadius = (Maths.RANDOM.nextInt(50000) + Maths.RANDOM.nextInt(50000)) / 10000.0f;
		float targetDensity = (Maths.RANDOM.nextFloat() + 0.25f) * 5.4950f;
		float earthMasses = (targetDensity * (float) (4.0f * Math.PI * Math.pow(earthRadius * 6378.137f, 3)) / 3.0f) / (float) (5.9723f * Math.pow(10.0f, 12.0f));

		Celestial celestial = new Celestial("Planet", star.getStarName() + " " + FauxGenerator.getFauxSentance(1, 4, 12),
				parentTypes, orbit, earthMasses,
				earthRadius, Maths.RANDOM.nextInt(400) * (Maths.RANDOM.nextBoolean() ? 1 : -1) / 10.0f, new ArrayList<>()
		);


		FlounderEngine.getLogger().log(celestial.toString());
		star.getChildObjects().add(celestial);
	}
}
