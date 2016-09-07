package game.celestial;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.logger.*;
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

	private static double generateValue(double average, double errorMargin) {
		double offset = (Maths.RANDOM.nextFloat() - 0.5) * 2.0 * errorMargin;
		return average + offset;
	}

	public static Star generateStar(Vector3f position) {
		double spawnKey = Maths.RANDOM.nextFloat() * 100.0; // TODO: Perlin noise.
		Star.StarType starType = Star.StarType.getTypeMakeup(spawnKey);
		double solarMasses = 1.0; // starType.solarMasses
		Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, position, new ArrayList<>());
		System.out.println(FlounderLogger.ANSI_RED + "===== Star " + star.getStarName() + ". =====" + FlounderLogger.ANSI_RESET);
		System.out.println(FlounderLogger.ANSI_RED + star.toString() + FlounderLogger.ANSI_RESET);

		double currentOrbit = 1.0;//star.getPlanetInnerLimit();

		//	while (currentOrbit < star.getPlanetOuterLimit()) {
		//		if (Maths.RANDOM.nextBoolean()) {
		generateCelestial("Planet", new Pair<>(star, null), currentOrbit);
		//		}

		//		currentOrbit += Maths.randomInRange(2.4f, 3.0f);
		//	}

		System.out.println(FlounderLogger.ANSI_RED + "===== End of star " + star.getStarName() + ". =====\n" + FlounderLogger.ANSI_RESET);

		return star;
	}

	private static void generateCelestial(String celestialName, Pair<Star, Celestial> parentTypes, double semiMajorAxis) {
		Star star = null;
		String parentName = null;
		double earthRadius = 0.0;

		if (parentTypes.getFirst() != null) {
			star = parentTypes.getFirst();
			parentName = star.getStarName();
			earthRadius = 1.0;//(Maths.RANDOM.nextInt(50000) + Maths.RANDOM.nextInt(50000)) / 10000.0f;
		} else if (parentTypes.getSecond() != null) {
			star = parentTypes.getSecond().getParentStar();
			parentName = parentTypes.getSecond().getPlanetName();
			earthRadius = parentTypes.getSecond().getEarthRadius() * Maths.RANDOM.nextFloat();
		}

		if (star == null) {
			return;
		}

		Orbit orbit = new Orbit(
				0.0167, semiMajorAxis, star.getSolarMasses(), // Maths.RANDOM.nextInt(6000) / 10000.0f
				0, 0, 0//Maths.RANDOM.nextInt(1800) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f
		);

		double targetDensity = (Maths.RANDOM.nextFloat() + 0.25) * 5.4950;
		double earthMasses = 1.0;//(targetDensity * (float) (4.0f * Math.PI * Math.pow(earthRadius * 6378.137f, 3)) / 3.0f) / (float) (5.9723f * Math.pow(10.0f, 12.0f));

		Celestial celestial = new Celestial(celestialName, parentName + " " + FauxGenerator.getFauxSentance(1, 4, 12),
				parentTypes, orbit, earthMasses,
				earthRadius, Maths.RANDOM.nextInt(400) * (Maths.RANDOM.nextBoolean() ? 1 : -1) / 10.0, new ArrayList<>()
		);

		Orbit moonOrbit = new Orbit(
				0.0549006, 0.00257188153, (float) (celestial.getEarthMasses() * 5.9723 * Math.pow(10, 24)) / (float) (1.989 * Math.pow(10, 30)), // Maths.RANDOM.nextInt(6000) / 10000.0f
				0, 0, 0//Maths.RANDOM.nextInt(1800) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f
		);

		Celestial moon = new Celestial(celestialName, celestial.getPlanetName() + " " + FauxGenerator.getFauxSentance(1, 2, 4),
				parentTypes, moonOrbit, 0.01230743469,
				0.27264165751, Maths.RANDOM.nextInt(400) * (Maths.RANDOM.nextBoolean() ? 1 : -1) / 10.0, new ArrayList<>()
		);

		/*if (parentTypes.getFirst() != null) {
			System.out.println(FlounderLogger.ANSI_BLUE + celestial.toString() + FlounderLogger.ANSI_RESET);

			float currentOrbit = celestial.getMinRingSpawns();//star.getPlanetInnerLimit();

			while (currentOrbit < celestial.getMaxRingSpawns()) {
				if (Maths.RANDOM.nextBoolean()) {
					generateCelestial("Moon", new Pair<>(null, celestial), currentOrbit);
				}

				currentOrbit += Maths.randomInRange(2.4, 3.0);
			}
		} else {*/
		System.out.println(FlounderLogger.ANSI_GREEN + celestial.toString() + FlounderLogger.ANSI_RESET);
		//}

		System.out.println(FlounderLogger.ANSI_BLUE + moon.toString() + FlounderLogger.ANSI_RESET);

		star.getChildObjects().add(celestial);
	}
}
