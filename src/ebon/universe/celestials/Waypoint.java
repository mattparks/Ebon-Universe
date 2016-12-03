package ebon.universe.celestials;

import ebon.universe.stars.*;
import flounder.maths.vectors.*;
import flounder.physics.*;

/**
 * A class that represents a waypoint to a star / celestial object.
 */
public class Waypoint {
	private Star targetStar;
	private Celestial targetCelestial;
	private Vector3f screenPosition;

	/**
	 * Creates a new empty waypoint.
	 */
	public Waypoint() {
		this.screenPosition = new Vector3f();
	}

	/**
	 * Creates a new waypoint to a star.
	 *
	 * @param targetStar The star to set a waypoint to.
	 */
	public Waypoint(Star targetStar) {
		this.targetStar = targetStar;
		this.screenPosition = new Vector3f();
	}

	/**
	 * Creates a new waypoint to a celestial.
	 *
	 * @param targetCelestial The celestial to set a waypoint to.
	 */
	public Waypoint(Celestial targetCelestial) {
		this.targetCelestial = targetCelestial;
		this.screenPosition = new Vector3f();
	}

	/**
	 * Updates the waypoint.
	 *
	 * @param ray The cameras ray to update from.
	 */
	public void update(Ray ray) {
		Vector3f targetPosition = getPosition();

		if (targetPosition != null) {
			ray.convertToScreenSpace(targetPosition, screenPosition);
		}
	}

	public Star getTargetStar() {
		return targetStar;
	}

	public void setTargetStar(Star targetStar) {
		this.targetStar = targetStar;
		this.targetCelestial = null;
	}

	public Celestial getTargetCelestial() {
		return targetCelestial;
	}

	public void setTargetCelestial(Celestial targetCelestial) {
		this.targetStar = null;
		this.targetCelestial = targetCelestial;
	}

	public Vector3f getScreenPosition() {
		return screenPosition;
	}

	/**
	 * Gets the waypoints position.
	 *
	 * @return The waypoints position.
	 */
	public Vector3f getPosition() {
		if (targetStar != null) {
			return targetStar.getPosition();
		} else if (targetCelestial != null) {
			return targetCelestial.getPosition();
		}

		return null;
	}

	/**
	 * Gets the waypoints stellar radii.
	 *
	 * @return The waypoints stellar radii.
	 */
	public double getStellarRadii() {
		if (targetStar != null) {
			return targetStar.getSolarRadius();
		} else if (targetCelestial != null) {
			return (targetCelestial.getEarthRadius() * Celestial.EARTH_RADIUS) / Star.SOL_RADIUS;
		}

		return 0.0;
	}

	/**
	 * Gets the waypoints name.
	 *
	 * @return The waypoints name.
	 */
	public String getName() {
		if (targetStar != null) {
			return targetStar.getStarName();
		} else if (targetCelestial != null) {
			return targetCelestial.getCelestialName();
		}

		return null;
	}
}
