package game.celestial.manager;

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
		float maxRadius = 0.0f;
		AABB g = new AABB();

		for (Star star : starsList) {
			Vector3f s = star.getPosition();

			if (star.getSolarRadius() > maxRadius) {
				maxRadius = (float) star.getSolarRadius();
			}

			if (s.x < g.getMinExtents().x) {
				g.getMinExtents().x = s.x;
			} else if (s.x > g.getMaxExtents().x) {
				g.getMaxExtents().x = s.x;
			}

			if (s.y < g.getMinExtents().y) {
				g.getMinExtents().y = s.y;
			} else if (s.y > g.getMaxExtents().y) {
				g.getMaxExtents().y = s.y;
			}

			if (s.z < g.getMinExtents().z) {
				g.getMinExtents().z = s.z;
			} else if (s.z > g.getMaxExtents().z) {
				g.getMaxExtents().z = s.z;
			}
		}

		Vector3f.subtract(g.getMinExtents(), new Vector3f(maxRadius, maxRadius, maxRadius), g.getMinExtents());
		Vector3f.add(g.getMaxExtents(), new Vector3f(maxRadius, maxRadius, maxRadius), g.getMaxExtents());

		int dustCount = starCount / dustRatio;

		for (int k = 0; k < dustCount; k++) {
			for (int j = 0; j < dustCount; j++) {
				for (int i = 0; i < dustCount; i++) {
					Vector3f minInnerAABB = new Vector3f(
							g.getMinExtents().getX() + i * (float) (g.getWidth() / dustCount),
							g.getMinExtents().getY() + j * (float) (g.getHeight() / dustCount),
							g.getMinExtents().getZ() + k * (float) (g.getDepth() / dustCount)
					);

					Vector3f maxInnerAABB = new Vector3f(
							minInnerAABB.getX() + (float) (g.getWidth() / dustCount),
							minInnerAABB.getY() + (float) (g.getHeight() / dustCount),
							minInnerAABB.getZ() + (float) (g.getDepth() / dustCount)
					);

					Dust dust = new Dust(minInnerAABB, maxInnerAABB, 0, new Colour(1, 1, 1));
					dustStructure.add(dust);
				}
			}
		}

		for (Dust dust : dustStructure.getAll(new ArrayList<>())) {
			Colour averageColour = new Colour();
			int dustStarCount = 0;

			for (Star star : starsStructure.queryInBounding(new ArrayList<>(), dust.getBounding())) {
				Colour.add(averageColour, star.getSurfaceColour(), averageColour);
				dustStarCount++;
			}

			Colour.divide(averageColour, new Colour(dustStarCount, dustStarCount, dustStarCount), averageColour);
			dust.setAverageColour(averageColour);
			dust.setStarCount(dustStarCount);
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
