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

	public static Star generateStar(Vector3f position) {
		double spawnKey = Maths.randomInRange(0.0, 100.0);
		Star.StarType starType = Star.StarType.getTypeMakeup(spawnKey);
		double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
		Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, position, new ArrayList<>());
		double currentOrbit = star.getPlanetFrostLine();

		while (currentOrbit >= star.getPlanetInnerLimit()) {
			if ((currentOrbit >= star.getHabitableMin() && currentOrbit <= star.getHabitableMax()) || Maths.RANDOM.nextBoolean()) {
				generateCelestial("Planet", new Pair<>(star, null), currentOrbit);
			}

			currentOrbit /= Maths.randomInRange(1.4, 2.0);
		}

		currentOrbit = star.getPlanetFrostLine() + 1.0;

		while (currentOrbit <= star.getPlanetOuterLimit()) {
			if (Maths.RANDOM.nextBoolean()) {
				generateCelestial("Planet", new Pair<>(star, null), currentOrbit);
			}

			currentOrbit *= Maths.randomInRange(1.4, 2.0);
		}

		Star.printSystem(star);
		return star;
	}

	private static void generateCelestial(String celestialName, Pair<Star, Celestial> parentTypes, double semiMajorAxis) {
		Star star = null;
		String parentName = null;
		double earthRadius = 0.0;
		double eccentricity = 0.0;

		if (parentTypes.getFirst() != null) {
			star = parentTypes.getFirst();
			parentName = star.getStarName();
			earthRadius = Maths.randomInRange(0.186, 1.75);
			eccentricity = 0.584 * Math.pow(Math.max(star.getChildObjects().size(), 2), -1.2);

			if (Maths.RANDOM.nextBoolean() && semiMajorAxis >= star.getPlanetFrostLine()) {
				earthRadius *= Maths.randomInRange(0.15, 3.0);
			}
		} else if (parentTypes.getSecond() != null) {
			star = parentTypes.getSecond().getParentStar();
			parentName = parentTypes.getSecond().getPlanetName();
			earthRadius = Maths.randomInRange(0.0391, parentTypes.getSecond().getEarthRadius());
			eccentricity = Maths.randomInRange(0.0, 0.2);
		}

		if (star == null) {
			return;
		}

		Orbit orbit = new Orbit(
				eccentricity, semiMajorAxis, star.getSolarMasses(),
				Maths.randomInRange(0.0, 180.0), Maths.randomInRange(0.0, 360.0), Maths.randomInRange(0.0, 360.0)
		);

		double targetDensity = (Maths.RANDOM.nextFloat() + 0.25) * Celestial.EARTH_DENSITY;
		double earthMasses = (targetDensity * (float) (4.0 * Math.PI * Math.pow(earthRadius * Celestial.EARTH_RADIUS, 3.0)) / 3.0) / (Celestial.EARTH_MASS * 1.0e-12);

		Celestial celestial = new Celestial(celestialName, parentName + " " + FauxGenerator.getFauxSentance(1, 4, 12),
				parentTypes, orbit, earthMasses,
				earthRadius, Maths.randomInRange(0.0, 40.0) * (Maths.RANDOM.nextBoolean() ? 1 : -1), new ArrayList<>()
		);

		/*Orbit moonOrbit = new Orbit(
				0.0549006, 0.00257188153, (float) (celestial.getEarthMasses() * 5.9723 * Math.pow(10, 24)) / (float) (1.989 * Math.pow(10, 30)), // Maths.RANDOM.nextInt(6000) / 10000.0f
				0, 0, 0//Maths.RANDOM.nextInt(1800) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f
		);

		Celestial moon = new Celestial(celestialName, celestial.getPlanetName() + " " + FauxGenerator.getFauxSentance(1, 2, 4),
				parentTypes, moonOrbit, 0.01230743469,
				0.27264165751, Maths.RANDOM.nextInt(400) * (Maths.RANDOM.nextBoolean() ? 1 : -1) / 10.0, new ArrayList<>()
		);*/

		/*if (parentTypes.getFirst() != null) {
			System.out.println(FlounderLogger.ANSI_BLUE + celestial.toString() + FlounderLogger.ANSI_RESET);

			float currentOrbit = celestial.getMinRingSpawns();//star.getPlanetInnerLimit();

			while (currentOrbit < celestial.getMaxRingSpawns()) {
				if (Maths.RANDOM.nextBoolean()) {
					generateCelestial("Moon", new Pair<>(null, celestial), currentOrbit);
				}

				currentOrbit += Maths.randomInRange(2.4, 3.0);
			}
		}*/

		star.getChildObjects().add(celestial);
	}
}
