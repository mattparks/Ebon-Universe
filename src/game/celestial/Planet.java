package game.celestial;

import flounder.maths.vectors.*;

public class Planet extends ICelestial {
	private String planetName;
	private Star parentStar;
	private Orbit orbit;

	private float earthMasses; // The planets earth mass.
	private float earthRadius; // The planets earth radius.
	private float density; // The planets density (g/cm^3).
	private float gravity; // The planets gravity (m/s^2).

	private float escapeVelocity; // The planets escape velocity (km/s).

	/**
	 * Creates a new planet from earth masses and radius. Then calculates characteristics.
	 *
	 * @param planetName The planets name.
	 * @param parentStar The planets parent star.
	 * @param orbit The orbit for the planet to follow.
	 * @param earthMasses The mass of the object in earth masses.
	 * @param earthRadius The radius of the object in earth radius.
	 */
	public Planet(String planetName, Star parentStar, Orbit orbit, float earthMasses, float earthRadius) {
		super(new Vector3f(), new Vector3f());
		this.planetName = planetName;
		this.parentStar = parentStar;
		this.orbit = orbit;

		this.earthMasses = earthMasses;
		this.earthRadius = earthRadius;
		float sphereVolume = (float) (4.0f * Math.PI * Math.pow(earthRadius * 6371.0f, 3)) / 3.0f;
		this.density = (float) (earthMasses * 5.972f * Math.pow(10.0f, 12.0f)) / sphereVolume;
		this.gravity = earthMasses / (earthRadius * earthRadius) * 9.807f;

		this.escapeVelocity = (float) Math.sqrt(earthMasses / earthRadius) * 11.186f;
	}

	@Override
	public void update() {
		// Position is calculated around the parent star and using the amount of seconds sense July 1 2016.
	}

	@Override
	public String toString() {
		return "Planet(" + planetName + " | " + PlanetType.getType(density).name() + ")[ " +
				"orbit=" + orbit.toString() +
				", earthMasses=" + earthMasses +
				", earthRadius=" + earthRadius +
				", density=" + density +
				", gravity=" + gravity +
				", escapeVelocity=" + escapeVelocity + "]";
	}

	public enum PlanetType {
		GASEOUS(0.687f, 2.21f),
		ICY(2.21f, 5.52f),
		ROCKY(5.52f, 13.56f);

		public float minDensity;
		public float maxDensity;

		PlanetType(float minDensity, float maxDensity) {
			this.minDensity = minDensity;
			this.maxDensity = maxDensity;
		}

		public static PlanetType getType(float density) {
			for (PlanetType type : PlanetType.values()) {
				if (density > type.minDensity && density <= type.maxDensity) {
					return type;
				}
			}

			return ROCKY;
		}
	}
}
