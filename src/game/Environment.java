package game;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.particles.loading.*;
import flounder.space.*;
import game.celestial.*;
import game.entities.*;

import java.util.*;

public class Environment {
	private static Fog fog;
	private static List<Light> lights;
	private static StructureBasic<Entity> entityQuadtree;
	private static StructureBasic<Star> starsQuadtree;

	private static final int GALAXY_STARS = 27500;
	private static final double GALAXY_RADIUS = 512;

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
			Vector3f spawnPosition = randomSpiralPoint();
			Star star = StarGenerator.generateStar(spawnPosition);
			starsQuadtree.add(star);
			//	FlounderEngine.getParticles().addParticle(ParticleLoader.load("starW"), spawnPosition, new Vector3f(), Float.POSITIVE_INFINITY, 0.0f, (float) star.getSolarRadius(), 0.0f);
		}

		List<Star> stars = starsQuadtree.getAll(new ArrayList<>());
		ArraySorting.heapSort(stars);

		if (GALAXY_STARS < 10) {
			for (Star star : stars) {
				Star.printSystem(star);
			}
		}

		Star.StarType currentType = null;
		int currentTypeCount = 0;
		int currentTypePlanets = 0;
		int currentTypeHabitable = 0;

		int totalCount = 0;
		int totalPlanets = 0;
		int totalHabitable = 0;

		for (int i = 0; i <= stars.size(); i++) {
			if (i >= stars.size() || currentType != stars.get(i).getStarType()) {
				if (currentType != null) {
					System.err.println(currentType.name() + ": Stars=" + currentTypeCount + ", Planets=" + currentTypePlanets + ", Habitability=" + (Maths.roundToPlace(((double) currentTypeHabitable) / ((double) currentTypePlanets) * 100.0, 3)) + "%");
					totalCount += currentTypeCount;
					totalPlanets += currentTypePlanets;
					totalHabitable += currentTypeHabitable;
				}

				currentTypeCount = 0;
				currentTypePlanets = 0;
				currentTypeHabitable = 0;
			}

			if (i < stars.size()) {
				currentType = stars.get(i).getStarType();
				currentTypeCount++;
				currentTypePlanets += stars.get(i).getChildObjects().size();

				for (Celestial celestial : stars.get(i).getChildObjects()) {
					if (celestial.supportsLife()) {
						currentTypeHabitable++;
					}
				}
			}
		}

		System.err.println("Total Stars=" + totalCount + ", Total Planets=" + totalPlanets + ", Total Habitability: " + (Maths.roundToPlace(((double) totalHabitable) / ((double) totalPlanets) * 100.0, 3)) + "%");
	}

	private static Vector3f randomSpiralPoint() {
		double random = Maths.RANDOM.nextDouble();

		if (random < 0.40) {
			return randomSpherePoint((float) GALAXY_RADIUS * 5.0f);
		} else if (random < 0.30) {
			return randomClusterPoint((float) GALAXY_RADIUS / 8.0f, (float) GALAXY_RADIUS / 20.0f, (float) GALAXY_RADIUS / 8.0f);
		}

		Vector3f spawnPosition = randomClusterPoint((float) GALAXY_RADIUS, (float) GALAXY_RADIUS, (float) GALAXY_RADIUS);
		return spawnPosition;
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
