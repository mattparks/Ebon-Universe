package game.celestial;

/**
 * A realistic orbit object.
 */
public class Orbit {
	private float eccentricity; // The orbits deviation, 0<e<1.

	private float semiMajorAxis; // The semi-major axis (AU).
	private float semiMinorAxis; // The semi-minor axis (AU).

	private float periapsis; // The orbits size periapsis (AU).
	private float apoapsis; // The orbits size apoapsis (AU).
	private float period; // The orbital period (Earth Days).
	private float velocity; // The objects velocity (km/s).

	private float pitch; // The orbits inclination, orbits prograde 0<i<90, orbits retrograde 90<i<180.
	private float yaw; // The orbits longitude of the ascending node from 0-360 degrees.
	private float roll; // The orbits argument of periapsis from 0-360 degrees.

	/**
	 * A object that represents a realistic orbit.
	 *
	 * @param eccentricity The orbits deviation, 0<e<1.
	 * @param semiMajorAxis The semi-major axis (AU), the average separation between the two bodys.
	 * @param solarMasses The solar mass of the object that will be orbited.
	 * @param pitch The orbits inclination, orbits prograde 0<i<90, orbits retrograde 90<i<180.
	 * @param yaw The orbits longitude of the ascending node from 0-360 degrees.
	 * @param roll The orbits argument of periapsis from 0-360 degrees.
	 */
	public Orbit(float eccentricity, float semiMajorAxis, float solarMasses, float pitch, float yaw, float roll) {
		this.eccentricity = eccentricity;

		this.semiMajorAxis = semiMajorAxis;
		this.semiMinorAxis = semiMajorAxis * (float) Math.sqrt(1 - Math.pow(eccentricity, 2.0f));

		this.periapsis = semiMajorAxis * (float) Math.sqrt(1 - eccentricity);
		this.apoapsis = semiMajorAxis * (float) Math.sqrt(1 + eccentricity);
		this.period = (float) Math.sqrt(Math.pow(semiMajorAxis, 3.0f) / solarMasses) * 365.25f;
		this.velocity = (float) Math.sqrt(solarMasses / semiMajorAxis) * 29.78f;

		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	public float getEccentricity() {
		return eccentricity;
	}

	public float getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public float getSemiMinorAxis() {
		return semiMinorAxis;
	}

	public float getPeriapsis() {
		return periapsis;
	}

	public float getApoapsis() {
		return apoapsis;
	}

	public float getPeriod() {
		return period;
	}

	public float getVelocity() {
		return velocity;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	@Override
	public String toString() {
		return "    Orbit[ \n        " +
				"eccentricity=" + eccentricity +
				", semiMajorAxis=" + semiMajorAxis +
				", semiMinorAxis=" + semiMinorAxis +
				", periapsis=" + periapsis +
				", apoapsis=" + apoapsis +
				", period=" + period +
				", velocity=" + velocity +
				", pitch=" + pitch +
				", yaw=" + yaw +
				", roll=" + roll + "\n    ]";
	}
}
