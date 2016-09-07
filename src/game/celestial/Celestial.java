package game.celestial;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;

import java.util.*;

/**
 * A realistic celestial object, like a planet / moon.
 */
public class Celestial {
	private String celestialType;

	private String planetName;
	private Vector3f position;
	private Vector3f rotation;

	private Optional<Star> parentStar;
	private Optional<Celestial> parentCelestial;
	private Orbit orbit;

	private List<Celestial> childObjects;

	private float earthMasses; // The planets earth mass.
	private float earthRadius; // The planets earth radius.
	private float density; // The planets density (g/cm^3).
	private float gravity; // The planets gravity (m/s^2).

	private float axialTilt; // How tilted over the planet is, rotates prograde 0<i<90, rotates retrograde 90<i<180.
	private float axialTropics; // The positions of the tropics, +/-.
	private float axialPolar; // The positions of the polar caps, +/-.

	private float minRingSpawns; // The ring rule min bounds (Earth radius) += 0.2.
	private float maxRingSpawns; // The ring rule max bounds (Earth radius) += 0.2.

	private float sphereOfInfluence; // The max radius distance the object can influence (in AU).
	private float escapeVelocity; // The planets escape velocity (km/s).

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
	public Celestial(String celestialType, String planetName, Pair<Star, Celestial> parentTypes, Orbit orbit, float earthMasses, float earthRadius, float axialTilt, List<Celestial> childObjects) {
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
		float sphereVolume = (float) (4.0f * Math.PI * Math.pow(earthRadius * 6378.137f, 3)) / 3.0f;
		this.density = (float) (earthMasses * 5.9723f * Math.pow(10, 12)) / sphereVolume;
		this.gravity = earthMasses / (earthRadius * earthRadius) * 9.798f;

		this.axialTilt = axialTilt;
		this.axialTropics = axialTilt;
		this.axialPolar = 90.0f - axialTilt;

		this.minRingSpawns = 1.34f * earthRadius;
		this.maxRingSpawns = 2.44f * earthRadius;

		this.sphereOfInfluence = (float) (orbit.getSemiMajorAxis() * (1.0f - orbit.getEccentricity()) * Math.pow((earthMasses * 5.9723f * Math.pow(10, 24)) / (getParentStar().getSolarMasses() * 1.989f * Math.pow(10, 30)), 2.0f / 5.0f));
		this.escapeVelocity = (float) Math.sqrt(earthMasses / earthRadius) * 11.186f;
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

	public float getEarthMasses() {
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

	public float getEarthRadius() {
		return earthRadius;
	}

	public float getDensity() {
		return density;
	}

	public float getGravity() {
		return gravity;
	}

	public float getAxialTilt() {
		return axialTilt;
	}

	public float getAxialTropics() {
		return axialTropics;
	}

	public float getAxialPolar() {
		return axialPolar;
	}

	public float getMinRingSpawns() {
		return minRingSpawns;
	}

	public float getMaxRingSpawns() {
		return maxRingSpawns;
	}

	/**
	 * Calculates the roche limit (rigid) for a second object (Earth radius.)
	 *
	 * @param secondDensity The density (in g/cm^3) for the second object.
	 *
	 * @return The roche limit for the second object.
	 */
	public float getRocheLimit(float secondDensity) {
		return (float) (1.26f * earthRadius * 6378137.0f * Math.cbrt(this.density / secondDensity)) / 6371000.0f;
	}

	public float getEscapeVelocity() {
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
		GASEOUS(0.687f, 2.21f, 29.0f),
		WATERY(2.21f, 5.52f, 5.0f),
		ROCKY(5.52f, 13.56f, 0.0f);

		public float minDensity;
		public float maxDensity;
		public float minGravity;

		PlanetType(float minDensity, float maxDensity, float minGravity) {
			this.minDensity = minDensity;
			this.maxDensity = maxDensity;
			this.minGravity = minGravity;
		}

		public static PlanetType getType(float density, float gravity) {
			for (PlanetType type : PlanetType.values()) {
				if (density > type.minDensity && density <= type.maxDensity || gravity > type.minGravity) {
					return type;
				}
			}

			return ROCKY;
		}
	}
}
