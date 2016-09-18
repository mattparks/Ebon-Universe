package game.celestial.manager;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.inputs.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;
import game.celestial.*;
import game.celestial.dust.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A class that creates and manages a galaxy and its processes.
 */
public class GalaxyManager {
	public static final int GALAXY_STARS = 25600;
	public static final double GALAXY_RADIUS = GALAXY_STARS / 10.0;

	private Sphere starView;
	private Ray starViewRay;
	private Waypoint waypoint;
	private CompoundButton waypointSetButton;

	private Star inSystemStar;
	private Celestial inSystemCelestial;

	private Vector3f lastPosition;
	private String playerVelocity;

	private ISpatialStructure<Star> starsQuadtree;
	private ISpatialStructure<Dust> dustQuadtree;

	/**
	 * Creates a new galaxy with a galaxy manager.
	 */
	public GalaxyManager() {
		starView = new Sphere((float) (GALAXY_STARS / 100.0));
		starViewRay = new Ray(false, new Vector2f(0.0f, 0.0f));
		waypoint = new Waypoint();
		waypointSetButton = new CompoundButton(new KeyButton(GLFW_KEY_TAB), new MouseButton(GLFW_MOUSE_BUTTON_3));

		inSystemStar = null;
		inSystemCelestial = null;

		lastPosition = new Vector3f();
		playerVelocity = "0 ly/s";

		starsQuadtree = new StructureBasic<>();
		dustQuadtree = new StructureBasic<>();
		GalaxyGenerator.generateGalaxy(GALAXY_STARS, GALAXY_RADIUS, starsQuadtree, dustQuadtree);

		waypoint.setTargetStar(starsQuadtree.getAll(new ArrayList<>()).get(GALAXY_STARS - 1));
	}

	/**
	 * Updates the galaxy manager.
	 */
	public void update() {
		// Nulls old values.
		inSystemStar = null;
		inSystemCelestial = null;

		// Updates stars if present.
		if (starsQuadtree != null) {
			// Updates and recalculations.
			Vector3f currentPosition = FlounderEngine.getCamera().getPosition();
			float distanceLastCurrent = Vector3f.getDistance(currentPosition, lastPosition);
			boolean selectingWaypoint = waypointSetButton.wasDown();

			Sphere.recalculate(starView, FlounderEngine.getCamera().getPosition(), 1.0f, starView);
			starViewRay.update(FlounderEngine.getCamera().getPosition());
			List<Star> selectedStars = null;

			// Checks all stars if inside the star view, and updates them.
			for (Star star : starsQuadtree.queryInBounding(new ArrayList<>(), starView)) {
				// If the stars sphere contains the camera.
				if (star.getBounding().contains(currentPosition)) {
					// Then this star is the current system.
					FlounderEngine.getBounding().addShapeRender(star.getBounding());
					inSystemStar = star;

					// Loads star children if not loaded.
					if (!star.isChildrenLoaded()) {
						star.loadChildren();
					}
				}

				// If the user is selecting a waypoint and this star is hit by the ray,
				if (selectingWaypoint && star.getBounding().intersectsRay(starViewRay)) {
					// Create a temp selected list.
					if (selectedStars == null) {
						selectedStars = new ArrayList<>();
					}

					selectedStars.add(star);
				}
			}

			// If the player is in no star system render bounding spheres for stars in view.
			if (inSystemStar == null) {
				for (Star star : starsQuadtree.queryInBounding(new ArrayList<>(), starView)) {
					FlounderEngine.getBounding().addShapeRender(star.getBounding());
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

	/**
	 * Gets a galactic waypoint set by the player.
	 *
	 * @return A galactic waypoint set by the player.
	 */
	public Waypoint getWaypoint() {
		return waypoint;
	}

	/**
	 * Gets the current system star system the player is in.
	 *
	 * @return The current system star system the player is in.
	 */
	public Star getInSystemStar() {
		return inSystemStar;
	}

	/**
	 * Gets the current system celestial the player is in.
	 *
	 * @return The current system celestial the player is in.
	 */
	public Celestial getInSystemCelestial() {
		return inSystemCelestial;
	}

	/**
	 * Gets a string for the players velocity.
	 *
	 * @return A string for the players velocity.
	 */
	public String getPlayerVelocity() {
		return playerVelocity;
	}

	/**
	 * Gets a list of stars in the galaxy.
	 *
	 * @return A list of stars in the galaxy.
	 */
	public ISpatialStructure<Star> getStars() {
		return starsQuadtree;
	}

	/**
	 * Gets a list of galactic dusts.
	 *
	 * @return A list of galactic dusts.
	 */
	public ISpatialStructure<Dust> getDusts() {
		return dustQuadtree;
	}

	/**
	 * gets if the star renderer should be used.
	 *
	 * @return If the star renderer should be used.
	 */
	public boolean renderStars() {
		return inSystemStar == null;
	}

	/**
	 * Destroys the galaxy.
	 */
	public void destroy() {
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
