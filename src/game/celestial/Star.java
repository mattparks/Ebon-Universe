package game.celestial;

import flounder.maths.*;
import flounder.maths.vectors.*;

import java.util.*;

public class Star extends ICelestial {
	private String starName;
	private List<Planet> planets;

	private float solarMasses; // The stars solar mass.
	private float solarRadius; // The stars solar radius.
	private float solarLumino; // The stars solar luminosity.
	private float surfaceTemp; // The stars surface temp in kelvin.

	private float solarLifetime; // The stars lifetime.

	private Colour surfColour; // The stars surface colour.

	private float planetInnerLimit;
	private float planetOuterLimit;
	private float planetFrostLine;

	private float habitableMin; // The habitable min distance in AU for carbon based life.
	private float habitableMax; // The habitable max distance in AU for carbon based life.

	public Star(String starName, float solarMasses, Vector3f position, List<Planet> planets) {
		super(position, new Vector3f());
		this.starName = starName;
		this.solarMasses = solarMasses;
		this.planets = planets;

		this.solarRadius = (float) Math.pow(solarMasses, solarMasses < 1.0f ? 0.8f : 0.5f);
		this.solarLumino = (float) Math.pow(solarMasses, 3.5f);
		this.surfaceTemp = (float) Math.pow(solarLumino / (solarRadius * solarRadius), 0.25f) * 5778.0f;

		this.solarLifetime = (float) Math.pow(solarMasses, -2.5f);

		this.surfColour = getColour();

		this.planetInnerLimit = 0.1f * solarMasses;
		this.planetOuterLimit = 40.0f * solarMasses;
		this.planetFrostLine = 4.85f * (float) Math.sqrt(solarLumino);

		this.habitableMin = (float) Math.sqrt(solarLumino / 1.10f);
		this.habitableMax = (float) Math.sqrt(solarLumino / 0.53f);
	}

	/**
	 * Generates a colour for the star by its surface temp, used this paper:
	 * http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/
	 *
	 * @return Generated colour for a star.
	 */
	private Colour getColour() {
		float temperature = surfaceTemp / 100.0f;
		float red;
		float green;
		float blue;

		// Calculate Red.
		if (temperature <= 66.0f) {
			red = 255;
		} else {
			red = temperature - 60.0f;
			red = 329.698727446f * (float) Math.pow(red, -0.1332047592f);

			if (red < 0.0f) {
				red = 0.0f;
			}

			if (red > 255.0f) {
				red = 255.0f;
			}
		}

		// Calculate Green.
		if (temperature <= 66.0f) {
			green = temperature;
			green = 99.4708025861f * (float) (Math.log(green) / Math.log(10.0f)) - 161.1195681661f;

			if (green < 0.0f) {
				green = 0.0f;
			}

			if (green > 255.0f) {
				green = 255.0f;
			}
		} else {
			green = temperature - 60.0f;
			green = 288.1221695283f * (float) Math.pow(green, -0.0755148492f);

			if (green < 0.0f) {
				green = 0.0f;
			}

			if (green > 255.0f) {
				green = 255.0f;
			}
		}

		// Calculate Blue.
		if (temperature >= 66.0f) {
			blue = 255.0f;
		} else {
			if (temperature <= 19.0f) {
				blue = 0.0f;
			} else {
				blue = temperature - 10.0f;
				blue = 138.5177312231f * (float) (Math.log(blue) / Math.log(10.0f)) - 305.0447927307f;

				if (blue < 0.0f) {
					blue = 0.0f;
				}
				if (blue > 255.0f) {
					blue = 255.0f;
				}
			}
		}

		return new Colour(red, green, blue, true);
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	@Override
	public String toString() {
		return "Star(" + starName + " | " + StarType.getTypeMass(solarMasses).name() + ")[ " +
				"solarMasses=" + solarMasses +
				", radius=" + solarRadius +
				", luminosity=" + solarLumino +
				", temperature=" + surfaceTemp +
				", solarLifetime=" + solarLifetime +
				", colour=" + surfColour.toString() +
				", planetInnerLimit=" + planetInnerLimit +
				", planetOuterLimit=" + planetOuterLimit +
				", planetFrostLine=" + planetFrostLine +
				", habitableMin=" + habitableMin +
				", habitableMax=" + habitableMax + "]";
	}

	@Override
	public void update() {
		planets.forEach(Planet::update);
	}

	public enum StarType {
		O(0.01f, 60.0f),
		B(0.19f, 18.0f),
		A(0.5f, 3.2f),
		F(9.0f, 1.7f),
		G(16.3f, 1.1f),
		K(33.9f, 0.8f),
		M(40.1f, 0.3f);

		public float universeMakeup; // How much of the universe if made up of this star type.
		public float solarMasses; // The stars solar mass.

		StarType(float universeMakeup, float solarMasses) {
			this.universeMakeup = universeMakeup;
			this.solarMasses = solarMasses;
		}

		public static StarType getTypeMakeup(float solarMakeup) {
			float currentMakeup = 0.0f;

			for (StarType type : StarType.values()) {
				if (solarMakeup <= currentMakeup) {
					return type;
				}

				currentMakeup += type.universeMakeup;
			}

			return G;
		}

		public static StarType getTypeMass(float solarMasses) {
			for (StarType type : StarType.values()) {
				if (solarMasses >= type.solarMasses) {
					return type;
				}
			}

			return G;
		}

		@Override
		public String toString() {
			return "StarType(" + name() + ")[ " +
					"universeMakeup=" + universeMakeup +
					", solarMasses=" + solarMasses + " ]";
		}
	}
}
