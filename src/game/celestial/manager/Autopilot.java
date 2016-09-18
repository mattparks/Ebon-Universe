package game.celestial.manager;

import flounder.engine.*;
import flounder.maths.vectors.*;
import game.*;

/**
 * A class that autopilots a ship/player.
 */
public class Autopilot {
	private static final float ACCELERATION = 0.05f;
	private static final float DECELERATION = 0.08f;

	private boolean autopilotEnabled;
	private boolean autopilotForceStop;
	private float autopilotStopSpeed;
	private Vector3f autopilotWaypoint;

	private float speedMagnitude;
	private Vector3f velocity;

	private Waypoint nextWaypoint;

	/**
	 * Creates a new autopilot.
	 */
	public Autopilot() {
		this.autopilotEnabled = false;
		this.autopilotForceStop = false;
		this.autopilotStopSpeed = 0.0f;
		this.autopilotWaypoint = new Vector3f();

		this.speedMagnitude = 0.0f;
		this.velocity = new Vector3f();

		this.nextWaypoint = null;
	}

	/**
	 * Updates the autopilot.
	 *
	 * @param position The ship/players current position.
	 */
	public void update(Vector3f position) {
		float arrivalTime = ((Vector3f.getDistance(position, autopilotWaypoint) - (float) Environment.getGalaxyManager().getWaypoint().getStellarRadii()) / speedMagnitude) / 60.0f;
		float stopTime = speedMagnitude / DECELERATION;

		if (arrivalTime <= stopTime || autopilotForceStop) {
			speedMagnitude -= DECELERATION * FlounderEngine.getDelta();

			if (speedMagnitude < 0.0f && autopilotStopSpeed >= 0.0f) {
				if (nextWaypoint != null) {
					autopilot(true, nextWaypoint.getPosition());
					return;
				} else {
					autopilot(false, position);
				}
			}
		} else {
			speedMagnitude += ACCELERATION * FlounderEngine.getDelta() * (float) Math.abs(Math.log(speedMagnitude + 0.5f));
		}

		Vector3f.subtract(position, autopilotWaypoint, velocity);

		if (velocity.lengthSquared() > 0.02f) {
			velocity.normalize();
		} else {
			autopilot(false, position);
		}

		velocity.scale(speedMagnitude);
		velocity.negate();
	}

	/**
	 * Sets a new waypoint, before / during autopilot.
	 *
	 * @param waypoint The new waypoint.
	 * @param stopShip If the waypoint will redirect the current autopilot.
	 */
	public void setWaypoint(Waypoint waypoint, boolean stopShip) {
		if (waypoint == null || waypoint.getPosition().equals(autopilotWaypoint)) {
			return;
		}

		if (stopShip && autopilotEnabled) {
			nextWaypoint = waypoint;
			toggleAutopilot();
		} else {
			autopilotWaypoint.set(waypoint.getPosition());
		}
	}

	/**
	 * Gets if the autopilot is enabled.
	 *
	 * @return If the autopilot is enabled.
	 */
	public boolean isEnabled() {
		return autopilotEnabled;
	}

	/**
	 * Toggles on/off the autopilot.
	 */
	public void toggleAutopilot() {
		if (!autopilotEnabled) {
			autopilot(true, Environment.getGalaxyManager().getWaypoint().getPosition());
		} else {
			autopilotForceStop = true;
			autopilotStopSpeed = speedMagnitude;
		}
	}

	private void autopilot(boolean enable, Vector3f waypoint) {
		speedMagnitude = 0.0f;
		autopilotEnabled = enable;
		autopilotForceStop = false;
		autopilotStopSpeed = 0.0f;
		autopilotWaypoint.set(waypoint);
		nextWaypoint = null;
	}

	/**
	 * Gets the current autopilot velocity.
	 *
	 * @return The current autopilot velocity.
	 */
	public Vector3f getVelocity() {
		return velocity;
	}
}
