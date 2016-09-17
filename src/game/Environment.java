package game;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;
import game.celestial.*;
import game.celestial.dust.*;
import game.entities.*;
import org.lwjgl.glfw.*;

import java.util.*;

import static java.awt.SystemColor.window;
import static org.lwjgl.glfw.GLFW.*;

public class Environment {
	private static Fog fog;
	private static List<Light> lights;
	private static ISpatialStructure<Entity> entityQuadtree;
	private static ISpatialStructure<Star> starsQuadtree;
	private static ISpatialStructure<Dust> dustQuadtree;

	private static final Vector3f VECTOR_2_2_2 = new Vector3f(2.0f, 2.0f, 2.0f);

	public static final int GALAXY_STARS = 25600;
	public static final double GALAXY_RADIUS = GALAXY_STARS / 10.0;

	private static final Sphere STAR_VIEW = new Sphere((float) (GALAXY_STARS / 100.0));
	private static final Ray STAR_VIEW_RAY = new Ray(false, new Vector2f(0.0f, 0.0f));
	private static Star STAR_WAYPOINT = null;
	public static final Vector3f STAR_SCREEN_POS = new Vector3f();

	public static Star IN_SYSTEM_STAR = null;
	public static Celestial IN_SYSTEM_CELESTIAL = null;

	private static Vector3f LAST_POSIITON = new Vector3f();
	public static String PLAYER_VELOCITY = "0 km/s";

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
		Environment.dustQuadtree = new StructureBasic<>();
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
		Colour currentColour = new Colour();
		int starCount = 0;

		for (int i = 0; i < galaxyStars.size(); i++) {
			if (starCount >= 25) {
				dustQuadtree.add(new Dust(Vector3f.subtract(maxPosition, minPosition, maxPosition).length(), Vector3f.divide(Vector3f.subtract(minPosition, maxPosition, maxPosition), VECTOR_2_2_2, null), starCount, Colour.divide(currentColour, new Colour(starCount, starCount, starCount), null)));
				minPosition.set(0.0f, 0.0f, 0.0f);
				maxPosition.set(0.0f, 0.0f, 0.0f);
				currentColour.set(0.0f, 0.0f, 0.0f);
				starCount = 0;
			}

			if (galaxyStars.get(i).getPosition().lengthSquared() < minPosition.lengthSquared()) {
				minPosition.set(galaxyStars.get(i).getPosition());
			}

			if (galaxyStars.get(i).getPosition().lengthSquared() > maxPosition.lengthSquared()) {
				maxPosition.set(galaxyStars.get(i).getPosition());
			}

			Colour.add(currentColour, galaxyStars.get(i).getSurfaceColour(), currentColour);

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

		Vector3f currentPosition = FlounderEngine.getCamera().getPosition();
		float distanceLastCurrent = Vector3f.getDistance(currentPosition, LAST_POSIITON);

		IN_SYSTEM_STAR = null;
		IN_SYSTEM_CELESTIAL = null;

		if (starsQuadtree != null) {
			Sphere.recalculate(STAR_VIEW, FlounderEngine.getCamera().getPosition(), 1.0f, STAR_VIEW);
			STAR_VIEW_RAY.update(FlounderEngine.getCamera().getPosition());
			List<Star> selectedStars = null;

			for (Star star : Environment.getStars().queryInBounding(new ArrayList<>(), STAR_VIEW)) {
				if (star.getBounding().contains(currentPosition)) {
					IN_SYSTEM_STAR = star;
					FlounderEngine.getBounding().addShapeRender(star.getBounding());
				}

				if (FlounderEngine.getDevices().getMouse().getMouse(GLFW_MOUSE_BUTTON_1)) {
					if (star.getBounding().intersectsRay(STAR_VIEW_RAY)) {
						if (selectedStars == null) {
							selectedStars = new ArrayList<>();
						}

						selectedStars.add(star);
					}
				}
			}

			if (IN_SYSTEM_STAR == null) {
			//	FlounderEngine.getBounding().addShapeRender(STAR_VIEW);

				for (Star star : Environment.getStars().queryInBounding(new ArrayList<>(), STAR_VIEW)) {
					FlounderEngine.getBounding().addShapeRender(star.getBounding());
				}
			}

			if (selectedStars != null) {
				if (selectedStars.size() > 1 && IN_SYSTEM_STAR != null) {
					selectedStars.remove(IN_SYSTEM_STAR);
				}

				ArraySorting.heapSort(selectedStars);

				if (!selectedStars.isEmpty()) {
					if (!selectedStars.get(0).equals(STAR_WAYPOINT)) {
						FlounderEngine.getLogger().log("Camera ray hit star: " + selectedStars.get(0));
						STAR_WAYPOINT = selectedStars.get(0);
					}

					if (!selectedStars.get(0).isChildrenLoaded()) {
						selectedStars.get(0).loadChildren();
						//	Star.printSystem(star);
					}
				}
			}

			if (STAR_WAYPOINT != null) {
				STAR_VIEW_RAY.convertToScreenSpace(STAR_WAYPOINT.getPosition(), STAR_SCREEN_POS);
			} else {
				STAR_SCREEN_POS.set(-0.5f, -0.5f, 0.0f);
			}

			if (IN_SYSTEM_STAR != null) {
				IN_SYSTEM_STAR.update();
			}

			if (IN_SYSTEM_CELESTIAL != null) {
				if (distanceLastCurrent >= 10000) {
					PLAYER_VELOCITY = distanceLastCurrent + " MM-km/s";
				} else {
					PLAYER_VELOCITY = distanceLastCurrent + " km/s";
				}
			} else if (IN_SYSTEM_STAR != null) {
				PLAYER_VELOCITY = distanceLastCurrent + " au/s";
			} else {
				PLAYER_VELOCITY = distanceLastCurrent + " ly/s";
			}

			LAST_POSIITON.set(currentPosition);
		}
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
		return IN_SYSTEM_STAR == null;
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
