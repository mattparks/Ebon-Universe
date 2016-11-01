package ebon.universe.orbits;

import flounder.maths.*;
import flounder.maths.vectors.*;

import static org.lwjgl.opengl.GL15.*;

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

	private LineSegment[] segments;
	private int segmentsFBO;
	private int vertices;

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

		this.segments = new LineSegment[2];
		// TODO: Generate segments.
		segmentsFBO = glGenBuffers();
		vertices = segments.length * 3;

		//	glBindVertexArray( segmentsFBO ); // setup for the layout of LineSegment_t
		//	glBindBuffer(GL_ARRAY_BUFFER, LineBufferObject);
		///	glBufferData(GL_ARRAY_BUFFER, segments.length * segmentsFBO, &lines[0], GL_DYNAMIC_DRAW);
		//glDrawArrays(GL_LINES, 0, vertices );
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

	public static class LineSegment {
		public Vector3f position1;
		public Colour colour1;

		public Vector3f position2;
		public Colour colour2;

		public LineSegment(Vector3f position1, Colour colour1, Vector3f posision2, Colour colour2) {
			this.position1 = position1;
			this.colour1 = colour1;
			this.position2 = posision2;
			this.colour2 = colour2;
		}
	}
}
