package ebon.celestial;

/**
 * A realistic orbit object.
 */
public class Orbit {
	private double eccentricity; // The orbits deviation, 0<e<1.

	private double semiMajorAxis; // The semi-major axis (AU).
	private double semiMinorAxis; // The semi-minor axis (AU).

	private double periapsis; // The orbits size periapsis (AU).
	private double apoapsis; // The orbits size apoapsis (AU).
	private double period; // The orbital period (Earth Days).
	private double velocity; // The objects velocity (km/s).

	private double pitch; // The orbits inclination, orbits prograde 0<i<90, orbits retrograde 90<i<180.
	private double yaw; // The orbits longitude of the ascending node from 0-360 degrees.
	private double roll; // The orbits argument of periapsis from 0-360 degrees.

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
	public Orbit(double eccentricity, double semiMajorAxis, double solarMasses, double pitch, double yaw, double roll) {
		this.eccentricity = eccentricity;

		this.semiMajorAxis = semiMajorAxis;
		this.semiMinorAxis = semiMajorAxis * Math.sqrt(1.0 - Math.pow(eccentricity, 2.0));

		this.periapsis = semiMajorAxis * Math.sqrt(1.0 - eccentricity);
		this.apoapsis = semiMajorAxis * Math.sqrt(1.0 + eccentricity);
		this.period = Math.sqrt(Math.pow(semiMajorAxis, 3.0) / solarMasses) * 365.25;
		this.velocity = Math.sqrt(solarMasses / semiMajorAxis) * 29.78;

		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	public double getEccentricity() {
		return eccentricity;
	}

	public double getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public double getSemiMinorAxis() {
		return semiMinorAxis;
	}

	public double getPeriapsis() {
		return periapsis;
	}

	public double getApoapsis() {
		return apoapsis;
	}

	public double getPeriod() {
		return period;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getPitch() {
		return pitch;
	}

	public double getYaw() {
		return yaw;
	}

	public double getRoll() {
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
