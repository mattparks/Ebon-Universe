package game.celestial.manager;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;
import game.celestial.*;
import game.celestial.dust.*;

import java.util.*;

/**
 * A class that generates a list of stars to create a galaxy.
 */
public class GalaxyGenerator {
	/**
	 * Generates a galaxy of stars and dusts.
	 *
	 * @param starCount The amount of stars to create.
	 * @param dustRatio The amount of stars per dust clump.
	 * @param galaxyRadius The radius of the galaxy, (LY).
	 * @param starsStructure The stars structure.
	 * @param dustStructure The dust structure.
	 */
	public static void generateGalaxy(int starCount, int dustRatio, double galaxyRadius, ISpatialStructure<Star> starsStructure, ISpatialStructure<Dust> dustStructure) {
		for (int i = 0; i < starCount; i++) {
			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, randomSpiralPoint(galaxyRadius), new ArrayList<>());
			starsStructure.add(star);
		}

		List<Star> starsList = starsStructure.getAll(new ArrayList<>());
		AABB g = new AABB();

		for (Star star : starsList) {
			Vector3f s = star.getPosition();

			if (s.x < g.getMinExtents().x) {
				g.getMinExtents().x = s.x;
			}

			if (s.y < g.getMinExtents().y) {
				g.getMinExtents().y = s.y;
			}

			if (s.z < g.getMinExtents().z) {
				g.getMinExtents().z = s.z;
			}

			if (s.x > g.getMaxExtents().x) {
				g.getMaxExtents().x = s.x;
			}

			if (s.y > g.getMaxExtents().y) {
				g.getMaxExtents().y = s.y;
			}

			if (s.z > g.getMaxExtents().z) {
				g.getMaxExtents().z = s.z;
			}
		}

		int dustCount = starCount / dustRatio;
		float lengthX = (float) g.getWidth() / dustCount;
		float lengthY = (float) g.getHeight() / dustCount;
		float lengthZ = (float) g.getDepth() / dustCount;

		float xPos = -lengthX;

		for (int x = 0; x < dustCount / 2; x++) {
			Vector3f min = new Vector3f(xPos, -lengthY, -lengthZ);
			xPos += lengthX;
			Vector3f max = new Vector3f(xPos, lengthY, lengthZ);

			Dust dust = new Dust(min, max, 0, new Colour(1, 1, 1));
			dustStructure.add(dust);
		}
	}

	private static Vector3f randomSpiralPoint(double radius) {
		double random = Maths.RANDOM.nextDouble();

		if (random < 0.75) {
			return randomClusterPoint((float) radius, (float) radius / 10.0f, (float) radius);
		} else if (random < 0.82) {
			return randomClusterPoint((float) radius / 10.0f, (float) radius / 10.0f, (float) radius / 10.0f);
		}

		return randomSpherePoint((float) radius * 2.0f);
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
