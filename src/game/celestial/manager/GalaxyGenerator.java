package game.celestial.manager;

import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.space.*;
import game.celestial.*;
import game.celestial.dust.*;

import java.util.*;

/**
 * A class that generates a list of stars to create a galaxy.
 */
public class GalaxyGenerator {
	public static void generateGalaxy(int starCount, double galaxyRadius, ISpatialStructure<Star> starsQuadtree, ISpatialStructure<Dust> dustQuadtree) {
		for (int i = 0; i < starCount; i++) {
			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, randomSpiralPoint(galaxyRadius), new ArrayList<>());
			starsQuadtree.add(star);
		}

		List<Star> galaxyStars = starsQuadtree.getAll(new ArrayList<>());
		galaxyStars = ArraySorting.quickSort(galaxyStars);
		Vector3f minPosition = new Vector3f();
		Vector3f maxPosition = new Vector3f();
		Colour currentColour = new Colour();
		int currentCount = 0;

		for (int i = 0; i < galaxyStars.size(); i++) {
			if (currentCount >= 25) {
				dustQuadtree.add(new Dust(Vector3f.subtract(maxPosition, minPosition, maxPosition).length(), Vector3f.divide(Vector3f.subtract(minPosition, maxPosition, maxPosition), new Vector3f(2.0f, 2.0f, 2.0f), null), starCount, Colour.divide(currentColour, new Colour(starCount, starCount, starCount), null)));
				minPosition.set(0.0f, 0.0f, 0.0f);
				maxPosition.set(0.0f, 0.0f, 0.0f);
				currentColour.set(0.0f, 0.0f, 0.0f);
				currentCount = 0;
			}

			if (galaxyStars.get(i).getPosition().lengthSquared() < minPosition.lengthSquared()) {
				minPosition.set(galaxyStars.get(i).getPosition());
			}

			if (galaxyStars.get(i).getPosition().lengthSquared() > maxPosition.lengthSquared()) {
				maxPosition.set(galaxyStars.get(i).getPosition());
			}

			Colour.add(currentColour, galaxyStars.get(i).getSurfaceColour(), currentColour);

			currentCount++;
		}
	}

	private static Vector3f randomSpiralPoint(double radius) {
		double random = Maths.RANDOM.nextDouble();

		if (random < 0.75) {
			return randomClusterPoint((float) radius, (float) radius / 10.0f, (float) radius);
		} else if (random < 0.82) {
			return randomClusterPoint((float) radius / 10.0f, (float) radius / 10.0f, (float) radius / 10.0f);
		}

		return randomSpherePoint((float) radius * 10.0f);
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
