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
		}*/

		int numArms = 2;
		double armSeparationDistance = 2.0 * Math.PI / numArms;
		double armOffsetMax = 0.5;
		double rotationFactor = 0.025;
		double randomOffsetXY = 0.025;

			for(int i = 0; i < starCount; i++) {
				// Choose a distance from the center of the galaxy.
				double distance = Maths.RANDOM.nextDouble() * 25.0;
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
				double starY = Math.sin(angle) * distance;

				double randomOffsetX = Maths.RANDOM.nextDouble() * randomOffsetXY;
				double randomOffsetY = Maths.RANDOM.nextDouble() * randomOffsetXY;

				starX += randomOffsetX;
				starY += randomOffsetY;

				// Now we can assign xy coords.
				Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
				double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
				Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, new Vector3f((float) starX, (float) 0.0, (float) starY), new ArrayList<>());
				starsStructure.add(star);
			}
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
		Vector3f spawnPosition = new Vector3f(Maths.normallyDistributedSingle(deviationX, 0), Maths.normallyDistributedSingle(deviationY, 0), Maths.normallyDistributedSingle(deviationZ, 0));
		return spawnPosition;
	}
}
