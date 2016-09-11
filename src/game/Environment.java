package game;

import flounder.helpers.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.space.*;
import game.celestial.*;
import game.entities.*;

import java.util.*;

public class Environment {
	private static Fog fog;
	private static List<Light> lights;
	private static StructureBasic<Entity> entityQuadtree;
	private static StructureBasic<Star> starsQuadtree;

	public static final int GALAXY_STARS = 32000;
	private static final double GALAXY_RADIUS = 1024;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 * @param sun The games main sunlight emitter.
	 */
	public static void init(Fog fog, Light sun) {
		Environment.fog = fog;
		Environment.lights = new ArrayList<>();
		Environment.lights.add(sun);
		Environment.entityQuadtree = new StructureBasic<>();
		Environment.starsQuadtree = new StructureBasic<>();

		generateGalaxy();
	}

	private static void generateGalaxy() {
		for (int i = 0; i < GALAXY_STARS; i++) {
			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, randomSpiralPoint(), new ArrayList<>());
			starsQuadtree.add(star);
		}
	}

	private static Vector3f randomSpiralPoint() {
		double random = Maths.RANDOM.nextDouble();

		if (random < 0.82) {
			return randomClusterPoint((float) GALAXY_RADIUS, (float) GALAXY_RADIUS / 10.0f, (float) GALAXY_RADIUS);
		} else if (random < 0.95) {
			return randomClusterPoint((float) GALAXY_RADIUS / 10.0f, (float) GALAXY_RADIUS / 10.0f, (float) GALAXY_RADIUS / 10.0f);
		}

		return randomSpherePoint((float) GALAXY_RADIUS * 10.0f);
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

	public static void update() {
		if (entityQuadtree != null) {
			for (Entity entity : entityQuadtree.getAll(new ArrayList<>())) {
				entity.update();
			}
		}

		//if (starsQuadtree != null) {
		//	for (Star star : starsQuadtree.getAll(new ArrayList<>())) {
		//		star.update();
		//	}
		//}
	}

	public static Fog getFog() {
		return fog;
	}

	public static List<Light> getLights() {
		return lights;
	}

	public static ISpatialStructure<Entity> getEntities() {
		return entityQuadtree;
	}

	public static StructureBasic<Star> getStars() {
		return starsQuadtree;
	}

	public static void destroy() {
		fog = null;

		if (lights != null) {
			lights.clear();
			lights = null;
		}

		if (entityQuadtree != null) {
			entityQuadtree.clear();
			entityQuadtree = null;
		}

		if (starsQuadtree != null) {
			starsQuadtree.clear();
			starsQuadtree = null;
		}
	}
}
