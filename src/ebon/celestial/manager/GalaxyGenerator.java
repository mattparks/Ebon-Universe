package ebon.celestial.manager;

import ebon.celestial.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.space.*;

import java.util.*;

/**
 * A class that generates a list of stars to create a galaxy.
 */
public class GalaxyGenerator {
	/**
	 * Generates a galaxy of stars and dusts.
	 *
	 * @param starCount The amount of stars to create.
	 * @param galaxyRadius The radius of the galaxy, (LY).
	 * @param starsStructure The stars structure.
	 */
	public static void generateGalaxy(int starCount, double galaxyRadius, ISpatialStructure<Star> starsStructure) {
		/*for (int i = 0; i < starCount; i++) {
			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, randomSpiralPoint(galaxyRadius), new ArrayList<>());
			starsStructure.add(star);

			if (i == 0) {
				star.loadChildren();
			}
		}*/

		// Logarithmic spiral constants.
		// http://en.wikipedia.org/wiki/Logarithmic_spiral
		float A = 1.25f;
		float B = 0.25f;
		float windings = 14.8f;
		float maxAngle = (float) (2.0 * Math.PI * windings);
		// How far stars may be away from spiral arm centres.
		float drift = 0.375f;

		for (int i = 0; i < starCount; i++) {
			float angle = maxAngle * Maths.RANDOM.nextFloat();

			float x = (float) (A * Math.exp(B * angle) * Math.cos(angle));
			x = x + (drift * x * Maths.RANDOM.nextFloat()) - (drift * x * Maths.RANDOM.nextFloat());
			float y = 10.0f;
			y = y + (drift * y * Maths.RANDOM.nextFloat()) - (drift * y * Maths.RANDOM.nextFloat());
			float z = (float) (A * Math.exp(B * angle) * Math.sin(angle));
			z = z + (drift * z * Maths.RANDOM.nextFloat()) - (drift * z * Maths.RANDOM.nextFloat());

			// 2 Spiral Arms.
			if (!Maths.RANDOM.nextBoolean()) {
				x = -x;
				z = -z;
			}

			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, new Vector3f(x, y, z), new ArrayList<>());
			starsStructure.add(star);

			if (i == 0) {
				star.loadChildren();
			}
		}

		/*int numArms = 2;
		double armSeparationDistance = 2.0 * Math.PI / numArms;
		double armOffsetMax = 75.0;
		double rotationFactor = 0.0075;
		double randomOffsetXYZ = 0.0750;

		for (int i = 0; i < starCount; i++) {
			// Choose a distance from the center of the galaxy.
			double distance = Maths.RANDOM.nextDouble() * 15.0;
			distance = Math.pow(distance, 2.0);

			// Choose an angle between 0 and 2 * PI.
			double angle = Maths.RANDOM.nextDouble() * 2.0 * Math.PI;
			double armOffset = Maths.RANDOM.nextDouble() * armOffsetMax;
			armOffset = armOffset - armOffsetMax / 2.0;
			armOffset = armOffset * (1.0 / distance);

			double squaredArmOffset = Math.pow(armOffset, 2.0);

			if (armOffset < 0.0) {
				squaredArmOffset = squaredArmOffset * -1.0;
			}

			armOffset = squaredArmOffset;

			double rotation = distance * rotationFactor;

			angle = (int) (angle / armSeparationDistance) * armSeparationDistance + armOffset + rotation;

			// Convert polar coordinates to 2D cartesian coordinates.
			double starX = Math.cos(angle) * distance;
			double starY = 1.0;
			double starZ = Math.sin(angle) * distance;

			double randomOffsetX = Maths.RANDOM.nextDouble() * randomOffsetXYZ;
			double randomOffsetY = Maths.randomInRange(-1.0, 1.0) * randomOffsetXYZ;
			double randomOffsetZ = Maths.RANDOM.nextDouble() * randomOffsetXYZ;

			starX += randomOffsetX;
			starY += randomOffsetY;
			starZ += randomOffsetZ;

			// Now it can assign the xyz coords.
			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, new Vector3f((float) starX, (float) starY, (float) starZ), new ArrayList<>());
			starsStructure.add(star);

			if (i == 0) {
				star.loadChildren();
			}
		}*/
	}

	private static Vector3f randomSpiralPoint(double radius) {
		double random = Maths.RANDOM.nextDouble();

		if (random < 0.50) {
			return randomClusterPoint((float) radius, (float) radius / 10.0f, (float) radius);
		} else if (random < 0.95) {
			return randomClusterPoint((float) radius / 10.0f, (float) radius / 10.0f, (float) radius / 10.0f);
		}

		return randomSpherePoint((float) radius);
	}

	private static Vector3f randomSpherePoint(float radius) {
		Vector3f spawnPosition = new Vector3f();
		Maths.generateRandomUnitVector(spawnPosition);
		spawnPosition.scale(radius);
		double a = Maths.RANDOM.nextDouble();
		double b = Maths.RANDOM.nextDouble();

		if (a > b) {
			double temp = a;
			a = b;
			b = temp;
		}

		double randX = b * Math.cos(6.283185307179586 * (a / b));
		double randY = b * Math.sin(6.283185307179586 * (a / b));
		float distance = new Vector2f((float) randX, (float) randY).length();
		spawnPosition.scale(distance);
		return spawnPosition;
	}

	private static Vector3f randomCirclePoint(float radius) {
		return Maths.randomPointOnCircle(new Vector3f(), new Vector3f(0.0f, 1.0f, 0.0f), radius);
	}

	private static Vector3f randomClusterPoint(float deviationX, float deviationY, float deviationZ) {
		return new Vector3f(Maths.normallyDistributedSingle(deviationX, 0.0f), Maths.normallyDistributedSingle(deviationY, 0.0f), Maths.normallyDistributedSingle(deviationZ, 0.0f));
	}
}
