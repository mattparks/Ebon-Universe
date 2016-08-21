package game.celestial;

public class Orbit {
	private float eccentricity; // The orbits deviation, 0<e<1.

	private float semiMajorAxis; // The semi-major axis (AU).
	private float semiMinorAxis; // The semi-minor axis (AU).

	private float periapsis; // The orbits size periapsis (AU).
	private float apoapsis; // The orbits size apoapsis (AU).
	private float period; // The orbital period.

	private float pitch; // The orbits inclination from 0-180 degrees.
	private float yaw; // The orbits longitude of the ascending node from 0-360 degrees.
	private float roll; // The orbits argument of periapsis from 0-360 degrees.

	private float plot; // The position of an object in orbit with reference to the periapsis, true anomaly, from 0-360 degrees.

	/**
	 *
	 * @param eccentricity The orbits deviation, 0<e<1.
	 * @param semiMajorAxis The semi-major axis (AU), the average separation between the two bodys.
	 * @param solarMasses The solar mass of the object that will be orbited.
	 * @param pitch The orbits inclination from 0-180 degrees.
	 * @param yaw The orbits longitude of the ascending node from 0-360 degrees.
	 * @param roll The orbits argument of periapsis from 0-360 degrees.
	 * @param plot The position of an object in orbit with reference to the periapsis, true anomaly, from 0-360 degrees.
	 */
	public Orbit(float eccentricity, float semiMajorAxis, float solarMasses, float pitch, float yaw, float roll, float plot) {
		this.eccentricity = eccentricity;

		this.semiMajorAxis = semiMajorAxis;
		this.semiMinorAxis = semiMajorAxis * (float) Math.sqrt(1 - Math.pow(eccentricity, 2.0f));

		this.periapsis = semiMajorAxis * (float) Math.sqrt(1 - eccentricity);
		this.apoapsis = semiMajorAxis * (float) Math.sqrt(1 + eccentricity);
		this.period = (float) Math.sqrt(Math.pow(semiMajorAxis, 3.0f) / solarMasses);

		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;

		this.plot = plot;
	}

	@Override
	public String toString() {
		return "Orbit[ " +
				"eccentricity=" + eccentricity +
				", semiMajorAxis=" + semiMajorAxis +
				", semiMinorAxis=" + semiMinorAxis +
				", periapsis=" + periapsis +
				", apoapsis=" + apoapsis +
				", period=" + period +
				", pitch=" + pitch +
				", yaw=" + yaw +
				", roll=" + roll +
				", plot=" + plot + "]";
	}
}
