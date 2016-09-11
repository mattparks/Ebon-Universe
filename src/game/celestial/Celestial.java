package game.celestial;

import flounder.helpers.*;
import flounder.maths.vectors.*;

import java.util.*;

/**
 * A realistic celestial object, like a planet / moon.
 */
public class Celestial implements Comparable<Celestial> {
	public static double AU_TO_KM = 1.496e+8; // The conversion from AU to KM.
	public static double EARTH_MASS = 5.9723e+24; // The earths mass (kg).
	public static double EARTH_RADIUS = 6378.137; // The earths radius (km).
	public static double EARTH_ESCAPE_VELOCITY = 11.186; // The earths escape velocity (km/s).
	public static double EARTH_DENSITY = 5.4950; // The earths density (g/cm^3).
	public static double EARTH_GRAVITY = 9.798; // The earths gravity (m/s/s).

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

	private double escapeVelocity; // The planets escape velocity (km/s).
	private double hillSphere; // The planets hill sphere (Planetary radii).

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
		this.density = (earthMasses * EARTH_MASS * 1.0e-12) / ((4.0 * Math.PI * Math.pow(earthRadius * EARTH_RADIUS, 3)) / 3.0);
		this.gravity = earthMasses / (earthRadius * earthRadius) * EARTH_GRAVITY;

		this.axialTilt = axialTilt;
		this.axialTropics = axialTilt;
		this.axialPolar = 90.0 - axialTilt;

		this.minRingSpawns = 1.34 * earthRadius;
		this.maxRingSpawns = 2.44 * earthRadius;

		this.escapeVelocity = Math.sqrt(earthMasses / earthRadius) * EARTH_ESCAPE_VELOCITY;
		this.hillSphere = (orbit.getSemiMajorAxis() * AU_TO_KM * (1.0 - orbit.getEccentricity()) * Math.cbrt((earthMasses * EARTH_MASS) / (3.0 * getParentMass()))) / (earthRadius * EARTH_RADIUS);
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

	public List<Celestial> getChildObjects() {
		return childObjects;
	}

	public double getEarthMasses() {
		return earthMasses;
	}

	/**
	 * Gets the parent star.
	 *
	 * @return The parent star.
	 */
	public Star getParentStar() {
		if (parentStar.isPresent()) {
			return parentStar.get();
		} else if (parentCelestial.isPresent()) {
			return parentCelestial.get().getParentStar();
		}

		return null;
	}

	/**
	 * Gets the parent celestial object.
	 *
	 * @return The parent celestial object.
	 */
	public Celestial getParentCelestial() {
		if (parentCelestial.isPresent()) {
			return parentCelestial.get().getParentCelestial();
		}

		return this;
	}

	/**
	 * Gets the mass of the parent object (Kg).
	 *
	 * @return The mass of the parent object.
	 */
	public double getParentMass() {
		if (parentStar.isPresent()) {
			return parentStar.get().getSolarMasses() * Star.SOL_MASS;
		} else if (parentCelestial.isPresent()) {
			return parentCelestial.get().getEarthMasses() * EARTH_MASS;
		}

		return 0.0;
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

	public double getEscapeVelocity() {
		return escapeVelocity;
	}

	public double getHillSphere() {
		return hillSphere;
	}

	/**
	 * Calculates the roche limit (rigid) for a second object (Earth radius.)
	 *
	 * @param secondDensity The density (in g/cm^3) for the second object.
	 *
	 * @return The roche limit for the second object.
	 */
	public double getRocheLimit(double secondDensity) {
		return 1.26 * earthRadius * Math.cbrt(this.density / secondDensity);
	}

	/**
	 * Gets if the object could support life.
	 *
	 * @return If the object could support life.
	 */
	public boolean supportsLife() {
		// Gets the parent star.
		Star star = getParentStar();

		// If the parent object is not habitable, this will not be either.
		if (parentCelestial.isPresent()) {
			Celestial celestial = getParentCelestial();

			if (!orbitHabitable(celestial.orbit, star)) {
				return false;
			}
		}

		//if (!PlanetType.getType(density, gravity).equals(PlanetType.WATERY)) {
		//	return false;
		//}

		// Calculates if this object is habitable.
		return orbitHabitable(orbit, star) && (earthMasses >= 0.1 && earthMasses <= 3.5) && (earthRadius >= 0.5 && earthRadius <= 1.5) && (gravity >= 3.923 && gravity <= 15.691);
	}

	private boolean orbitHabitable(Orbit orbit, Star star) {
		return true; // orbit.getSemiMajorAxis() >= star.getHabitableMin() && orbit.getSemiMajorAxis() <= star.getHabitableMax() && orbit.getEccentricity() > 0.0 && orbit.getEccentricity() <= 0.3; // 0.2 is a better max eccentricity.
	}

	@Override
	public String toString() {
		return celestialType + "(" + planetName + " | " + PlanetType.getType(earthMasses, earthRadius).name() + ") [ \n    " +
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
				", rocheLimit(Lun)=" + getRocheLimit(3.34) +
				", hillSphere=" + hillSphere +
				", escapeVelocity=" + escapeVelocity +
				", \n" + orbit.toString() + "\n]";
	}

	@Override
	public int compareTo(Celestial o) {
		return ((Double) orbit.getSemiMajorAxis()).compareTo(o.orbit.getSemiMajorAxis());
	}

	public enum PlanetType {
		BROWN_DWARF(0.75, 317.816, 25425.318, 15.0, 20.0),
		DWARF(10.0, 0.0001, 0.1, 0.03, 0.125),
		GASEOUS(44.625, 10.0, 317.816, 1.6, 15.0),
		TERRESTRIAL(44.625, 0.1, 10.0, 0.125, 1.6);

		public double universeMakeup; // How much of the universe if made up of this celestial type.
		public double minMass;
		public double maxMass;
		public double minRadius;
		public double maxRadius;

		PlanetType(double universeMakeup, double minMass, double maxMass, double minRadius, double maxRadius) {
			this.universeMakeup = universeMakeup;
			this.minMass = minMass;
			this.maxMass = maxMass;
			this.minRadius = minRadius;
			this.maxRadius = maxRadius;
		}

		public static PlanetType getTypeMakeup(double celestialMakeup) {
			double currentMakeup = 0.0;

			for (PlanetType type : PlanetType.values()) {
				if (celestialMakeup <= currentMakeup) {
					return type;
				}

				currentMakeup += type.universeMakeup;
			}

			return TERRESTRIAL;
		}

		public static PlanetType getType(double mass, double radius) {
			for (PlanetType type : PlanetType.values()) {
				if (mass >= type.minMass && mass < type.maxMass && radius >= type.minRadius && radius < type.maxRadius) {
					return type;
				}
			}

			return TERRESTRIAL;
		}
	}
}
