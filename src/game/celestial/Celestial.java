package game.celestial;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;

import java.util.*;

/**
 * A realistic celestial object, like a planet / moon.
 */
public class Celestial {
	public static double PLANET_MASS = 5.9723 * Math.pow(10, 24);
	public static double PLANET_RADIUS = 6378.137;

	private String celestialType;

	private String planetName;
	private Vector3f position;
	private Vector3f rotation;

	private Optional<Star> parentStar;
	private Optional<Celestial> parentCelestial;
	private Orbit orbit;

	private List<Celestial> childObjects;

	private double earthMasses; // The planets earth mass.
	private double earthRadius; // The planets earth radius.
	private double density; // The planets density (g/cm^3).
	private double gravity; // The planets gravity (m/s^2).

	private double axialTilt; // How tilted over the planet is, rotates prograde 0<i<90, rotates retrograde 90<i<180.
	private double axialTropics; // The positions of the tropics, +/-.
	private double axialPolar; // The positions of the polar caps, +/-.

	private double minRingSpawns; // The ring rule min bounds (Earth radius) += 0.2.
	private double maxRingSpawns; // The ring rule max bounds (Earth radius) += 0.2.

	private double sphereOfInfluence; // The max radius distance the object can influence (in AU).
	private double escapeVelocity; // The planets escape velocity (km/s).

	/**
	 * Creates a new celestial object from earth masses and radius. Then calculates characteristics.
	 *
	 * @param celestialType The type of celestial body.
	 * @param planetName The celestial objects name.
	 * @param parentTypes The celestial objects parent star / celestial.
	 * @param orbit The orbit for the celestial object to follow.
	 * @param earthMasses The mass of the object in earth masses.
	 * @param earthRadius The radius of the object in earth radius.
	 * @param axialTilt How tilted over the planet is, rotates prograde 0<i<90, rotates retrograde 90<i<180.
	 * @param childObjects The list of objects orbiting the star.
	 */
	public Celestial(String celestialType, String planetName, Pair<Star, Celestial> parentTypes, Orbit orbit, double earthMasses, double earthRadius, double axialTilt, List<Celestial> childObjects) {
		this.celestialType = celestialType;

		this.planetName = planetName;
		this.position = new Vector3f();
		this.rotation = new Vector3f();

		this.parentStar = Optional.ofNullable(parentTypes.getFirst());
		this.parentCelestial = Optional.ofNullable(parentTypes.getSecond());
		this.orbit = orbit;

		this.childObjects = childObjects;

		this.earthMasses = earthMasses;
		this.earthRadius = earthRadius;
		this.density = (earthMasses * PLANET_MASS * Math.pow(10, -12)) / ((4.0 * Math.PI * Math.pow(earthRadius * PLANET_RADIUS, 3)) / 3.0);
		this.gravity = earthMasses / (earthRadius * earthRadius) * 9.798;

		this.axialTilt = axialTilt;
		this.axialTropics = axialTilt;
		this.axialPolar = 90.0 - axialTilt;

		this.minRingSpawns = 1.34 * earthRadius;
		this.maxRingSpawns = 2.44 * earthRadius;

		this.sphereOfInfluence = orbit.getSemiMajorAxis() * (1.0 - orbit.getEccentricity()) * Math.pow((earthMasses * PLANET_MASS) / (getParentStar().getSolarMasses() * Star.SOL_MASS), 2.0 / 5.0);
		this.escapeVelocity = Math.sqrt(earthMasses / earthRadius) * 11.186;
	}

	public void update() {
		// Position is calculated around the parent star and using the amount of seconds sense an arbitrary date.
		childObjects.forEach(Celestial::update);
	}

	public String getPlanetName() {
		return planetName;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Orbit getOrbit() {
		return orbit;
	}

	public double getEarthMasses() {
		return earthMasses;
	}

	public Star getParentStar() {
		if (parentStar.isPresent()) {
			return parentStar.get();
		} else if (parentCelestial.isPresent()) {
			return parentCelestial.get().getParentStar();
		}

		return null;
	}

	public double getEarthRadius() {
		return earthRadius;
	}

	public double getDensity() {
		return density;
	}

	public double getGravity() {
		return gravity;
	}

	public double getAxialTilt() {
		return axialTilt;
	}

	public double getAxialTropics() {
		return axialTropics;
	}

	public double getAxialPolar() {
		return axialPolar;
	}

	public double getMinRingSpawns() {
		return minRingSpawns;
	}

	public double getMaxRingSpawns() {
		return maxRingSpawns;
	}

	/**
	 * Calculates the roche limit (rigid) for a second object (Earth radius.)
	 *
	 * @param secondDensity The density (in g/cm^3) for the second object.
	 *
	 * @return The roche limit for the second object.
	 */
	public double getRocheLimit(float secondDensity) {
		return (1.26 * earthRadius * 6378137.0 * Math.cbrt(this.density / secondDensity)) / 6371000.0;
	}

	public double getEscapeVelocity() {
		return escapeVelocity;
	}

	public boolean supportsLife() {
		if (!PlanetType.getType(density, gravity).equals(PlanetType.WATERY)) {
			return false;
		}

		if (parentStar.isPresent()) {
			Star star = parentStar.get();
			return orbit.getSemiMajorAxis() >= star.getHabitableMin() && orbit.getSemiMajorAxis() <= star.getHabitableMax();
		} else if (parentCelestial.isPresent()) {
			Celestial celestial = parentCelestial.get();
			return celestial.supportsLife();
		}

		return false;
	}

	@Override
	public String toString() {
		return celestialType + "(" + planetName + " | " + PlanetType.getType(density, gravity).name() + ") [ \n    " +
				"earthMasses=" + earthMasses +
				", earthRadius=" + earthRadius +
				", density=" + density +
				", gravity=" + gravity +
				", supportsLife=" + supportsLife() +
				", axialTilt=" + axialTilt +
				", axialTropics=" + axialTropics +
				", axialPolar=" + axialPolar +
				", minRingSpawns=" + minRingSpawns +
				", maxRingSpawns=" + maxRingSpawns +
				", rocheLimit(Lun)=" + getRocheLimit(3.34f) +
				", escapeVelocity=" + escapeVelocity +
				", sphereOfInfluence=" + sphereOfInfluence +
				", \n" + orbit.toString() + "\n]";
	}

	public enum PlanetType {
		GASEOUS(0.687, 2.21, 29.0),
		WATERY(2.21, 5.52, 5.0),
		ROCKY(5.52, 13.56, 0.0);

		public double minDensity;
		public double maxDensity;
		public double minGravity;

		PlanetType(double minDensity, double maxDensity, double minGravity) {
			this.minDensity = minDensity;
			this.maxDensity = maxDensity;
			this.minGravity = minGravity;
		}

		public static PlanetType getType(double density, double gravity) {
			for (PlanetType type : PlanetType.values()) {
				if (density > type.minDensity && density <= type.maxDensity || gravity > type.minGravity) {
					return type;
				}
			}

			return ROCKY;
		}
	}
}
