package game.celestial;

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

	private float hillSphere; // The region in which it dominates the attraction of satellites, in earth radius.
	// TODO: Roche limits.

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
		this.density = (float) (earthMasses * 5.9723f * Math.pow(10.0f, 12.0f)) / sphereVolume;
		this.gravity = earthMasses / (earthRadius * earthRadius) * 9.798f;

		this.axialTilt = axialTilt;
		this.axialTropics = axialTilt;
		this.axialPolar = 90.0f - axialTilt;

		this.minRingSpawns = 1.34f * earthRadius;
		this.maxRingSpawns = 2.44f * earthRadius;

		float parentSolarMasses = 0;

		if (parentStar.isPresent()) {
			Star star = parentStar.get();
			parentSolarMasses = star.getSolarMasses();
		} else if (parentCelestial.isPresent()) {
			Celestial celestial = parentCelestial.get();
			parentSolarMasses = celestial.getParentStar().getSolarMasses();
		}

		this.hillSphere = orbit.getSemiMajorAxis() * (float) Math.pow(earthMasses / parentSolarMasses, (1.0f / 3.0f)) * 235.0f;

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

	public float getHillSphere() {
		return hillSphere;
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
			return star.getHabitableMin() <= orbit.getSemiMajorAxis() && star.getHabitableMax() <= orbit.getSemiMajorAxis();
		} else if (parentCelestial.isPresent()) {
			Celestial celestial = parentCelestial.get();
			return celestial.supportsLife();
		}

		return false;
	}

	@Override
	public String toString() {
		return celestialType + "(" + planetName + " | " + PlanetType.getType(density, gravity).name() + ")[ " +
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
				", hillSphere=" + hillSphere +
				", escapeVelocity=" + escapeVelocity +
				", " + orbit.toString() + "]";
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
