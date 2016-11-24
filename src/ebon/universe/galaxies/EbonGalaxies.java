package ebon.universe.galaxies;

import ebon.*;
import ebon.universe.celestials.*;
import ebon.universe.stars.*;
import flounder.camera.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.inputs.*;
import flounder.loaders.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.physics.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import flounder.shaders.*;
import flounder.space.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A class that creates and manages a galaxy and its processes.
 */
public class EbonGalaxies extends IModule {
	private static final EbonGalaxies instance = new EbonGalaxies();

	public static final int GALAXY_STARS = 12800;

	private Sphere starView;
	private Ray starViewRay;
	private Waypoint waypoint;
	private CompoundButton waypointSetButton;

	private Star inSystemStar;
	private Celestial inSystemCelestial;

	private Vector3f lastPosition;
	private String playerVelocity;

	private Galaxy galaxy;

	/**
	 * Creates a new galaxy with a galaxy manager.
	 */
	public EbonGalaxies() {
		super(ModuleUpdate.UPDATE_POST, FlounderLogger.class, FlounderProfiler.class, FlounderLoader.class, FlounderModels.class, FlounderBounding.class, FlounderShaders.class);
	}

	@Override
	public void init() {
		starView = new Sphere(256.0f);
		starViewRay = new Ray(false, new Vector2f(0.0f, 0.0f));
		waypoint = new Waypoint();
		waypointSetButton = new CompoundButton(new KeyButton(GLFW_KEY_TAB), new MouseButton(GLFW_MOUSE_BUTTON_3));

		inSystemStar = null;
		inSystemCelestial = null;

		lastPosition = new Vector3f();
		playerVelocity = "0 ly/s";
	}

	public static void generate() {
		instance.galaxy = new Galaxy(
				FauxGenerator.getFauxSentance(1, 3, 7), new Vector3f(),
				GALAXY_STARS / 20.0,    // Radius of the galaxy.
				GALAXY_STARS            // Total number of stars.
		);
	}

	@Override
	public void run() {
		// Nulls old values.
		Star lastInStarSystem = inSystemStar;
		inSystemStar = null;
		inSystemCelestial = null;

		// Updates stars if present.
		if (galaxy != null && FlounderCamera.getCamera() != null) {
			//for (AABB aabb : starsStructure.getAABBs()) {
			//	FlounderBounding.addShapeRender(aabb);
			//}

			galaxy.update();

			// Updates and recalculations.
			Vector3f currentPosition = FlounderCamera.getCamera().getPosition();
			float distanceLastCurrent = Vector3f.getDistance(currentPosition, lastPosition);
			boolean selectingWaypoint = waypointSetButton.wasDown();

			Sphere.recalculate(starView, FlounderCamera.getCamera().getPosition(), 1.0f, starView);
			starViewRay.update(FlounderCamera.getCamera().getPosition());
			List<Star> selectedStars = null;

			// Checks all stars if inside the star view, and updates them.
			for (Star star : galaxy.getStars().queryInBounding(new ArrayList<>(), starView)) {
				// If the stars sphere contains the camera.
				if (star.getBounding().contains(currentPosition)) {
					// Then this star is the current system.
					FlounderBounding.addShapeRender(star.getBounding());
					inSystemStar = star;

					if (!star.equals(lastInStarSystem)) {
						((EbonGuis) FlounderGuis.getGuiMaster()).getOverlayStatus().addMessage("Entering Star " + star.getStarName());
					}

					// Loads star children if not loaded.
					if (!star.isChildrenLoaded()) {
						star.loadChildren();
					}
				}

				// If the user is selecting a waypoint and this star is hit by the ray,
				if (selectingWaypoint && star.getBounding().intersects(starViewRay).isIntersection()) {
					// Create a temp selected list.
					if (selectedStars == null) {
						selectedStars = new ArrayList<>();
					}

					selectedStars.add(star);
				}
			}

			// If the player is in no star system render bounding spheres for stars in view.
			if (inSystemStar == null) {
				if (lastInStarSystem != null) {
					((EbonGuis) FlounderGuis.getGuiMaster()).getOverlayStatus().addMessage("Exiting Star " + lastInStarSystem.getStarName());
				}

				for (Star star : galaxy.getStars().queryInBounding(new ArrayList<>(), starView)) {
					FlounderBounding.addShapeRender(star.getBounding());
				}
			}

			// If there are selected stars in the ray...
			if (selectedStars != null) {
				// If there is only one object set the waypoint to it,
				if (selectedStars.size() == 0) {
					waypoint.setTargetStar(selectedStars.get(0));
				} else { // Otherwise select the closet, preferring non current system stars.
					if (selectedStars.size() > 1 && inSystemStar != null) {
						selectedStars.remove(inSystemStar);
					}

					ArraySorting.heapSort(selectedStars);

					if (!selectedStars.get(0).equals(waypoint.getTargetStar())) {
						waypoint.setTargetStar(selectedStars.get(0));
					}
				}
			}

			// Updates the waypoint / last position.
			waypoint.update(starViewRay);
			lastPosition.set(currentPosition);

			// Updates the star system currently in.
			if (inSystemStar != null) {
				inSystemStar.update();
			}

			// Sets the player velocity text depending on speed units.
			distanceLastCurrent = Maths.roundToPlace(distanceLastCurrent, 5);

			if (inSystemCelestial != null) {
				if (distanceLastCurrent >= 10000) {
					playerVelocity = distanceLastCurrent + " MM-km/s";
				} else {
					playerVelocity = distanceLastCurrent + " km/s";
				}
			} else if (inSystemStar != null) {
				playerVelocity = distanceLastCurrent + " au/s";
			} else {
				playerVelocity = distanceLastCurrent + " ly/s";
			}
		}
	}

	@Override
	public void profile() {
		FlounderProfiler.add("Galaxies", "Stars", GALAXY_STARS);
	}

	/**
	 * Called when the galaxy is needed to be created.
	 */
	public static void generateGalaxy() {
		instance.galaxy.reset();
		instance.waypoint.setTargetStar(instance.galaxy.getStars().getAll(new ArrayList<>()).get(GALAXY_STARS - 1));
	}

	/**
	 * Clears the instance of all stars.
	 */
	public static void clear() {
		if (instance.galaxy != null) {
			instance.galaxy.reset();
		}
	}

	public static Ray getStarViewRay() {
		return instance.starViewRay;
	}

	/**
	 * Gets a galactic waypoint set by the player.
	 *
	 * @return A galactic waypoint set by the player.
	 */
	public static Waypoint getWaypoint() {
		return instance.waypoint;
	}

	/**
	 * Gets the current system star system the player is in.
	 *
	 * @return The current system star system the player is in.
	 */
	public static Star getInSystemStar() {
		return instance.inSystemStar;
	}

	/**
	 * Gets the current system celestial the player is in.
	 *
	 * @return The current system celestial the player is in.
	 */
	public static Celestial getInSystemCelestial() {
		return instance.inSystemCelestial;
	}

	/**
	 * Gets a string for the players velocity.
	 *
	 * @return A string for the players velocity.
	 */
	public static String getPlayerVelocity() {
		return instance.playerVelocity;
	}

	/**
	 * Gets a list of stars in the galaxy.
	 *
	 * @return A list of stars in the galaxy.
	 */
	public static ISpatialStructure<Star> getStars() {
		return instance.galaxy.getStars();
	}

	/**
	 * gets if the star renderer should be used.
	 *
	 * @return If the star renderer should be used.
	 */
	public static boolean renderStars() {
		return instance.inSystemStar == null;
	}

	@Override
	public IModule getInstance() {
		return instance;
	}

	@Override
	public void dispose() {
		clear();
	}
}
