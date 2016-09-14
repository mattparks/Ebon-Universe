package game;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.particles.loading.*;
import flounder.space.*;
import game.celestial.*;
import game.celestial.dust.*;
import game.entities.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class Environment {
	private static Fog fog;
	private static List<Light> lights;
	private static StructureBasic<Entity> entityQuadtree;
	private static StructureOctree<Star> starsQuadtree;
	private static StructureOctree<Dust> dustQuadtree;

	private static final Vector3f VECTOR_2_2_2 = new Vector3f(2.0f, 2.0f, 2.0f);

	public static final int GALAXY_STARS = 25600;
	public static final double GALAXY_RADIUS = 1792.0;

	private static boolean RENDER_STARS = true;

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
		Environment.starsQuadtree = new StructureOctree<>(1024);
		Environment.dustQuadtree = new StructureOctree<>(512);
	}

	public static void generateGalaxy() {
		for (int i = 0; i < GALAXY_STARS; i++) {
			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, randomSpiralPoint(), new ArrayList<>());
			starsQuadtree.add(star);
		}

		List<Star> galaxyStars = starsQuadtree.getAll(new ArrayList<>());
		galaxyStars = ArraySorting.quickSort(galaxyStars);
		Vector3f minPosition = new Vector3f();
		Vector3f maxPosition = new Vector3f();
		int starCount = 0;

		for (int i = 0; i < galaxyStars.size(); i++) {
			if (starCount >= 25) {
				dustQuadtree.add(new Dust(Vector3f.subtract(maxPosition,minPosition, maxPosition).length(), Vector3f.divide(Vector3f.subtract(minPosition, maxPosition, maxPosition), VECTOR_2_2_2, null), starCount));
				minPosition.set(0.0f, 0.0f, 0.0f);
				maxPosition.set(0.0f, 0.0f, 0.0f);
				starCount = 0;
			}

			if (galaxyStars.get(i).getPosition().lengthSquared() < minPosition.lengthSquared()) {
				minPosition.set(galaxyStars.get(i).getPosition());
			}

			if (galaxyStars.get(i).getPosition().lengthSquared() > maxPosition.lengthSquared()) {
				maxPosition.set(galaxyStars.get(i).getPosition());
			}

			starCount++;
		}
	}

	private static Vector3f randomSpiralPoint() {
		double random = Maths.RANDOM.nextDouble();

		if (random < 0.75) {
			return randomClusterPoint((float) GALAXY_RADIUS, (float) GALAXY_RADIUS / 10.0f, (float) GALAXY_RADIUS);
		} else if (random < 0.82) {
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

		if (FlounderEngine.getDevices().getKeyboard().getKey(GLFW_KEY_L)) {
			RENDER_STARS = false;
		} else {
			RENDER_STARS = true;
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

	public static ISpatialStructure<Star> getStars() {
		return starsQuadtree;
	}

	public static ISpatialStructure<Dust> getDusts() {
		return dustQuadtree;
	}

	public static boolean renderStars() {
		return RENDER_STARS;
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

		if (dustQuadtree != null) {
			dustQuadtree.clear();
			dustQuadtree = null;
		}
	}
}
