package game.celestial;

import flounder.maths.*;
import flounder.maths.vectors.*;

import java.util.*;

/**
 * A realistic star object.
 */
public class Star {
	private String starName;
	private Vector3f position;

	private List<Celestial> childObjects;

	private float solarMasses; // The stars solar mass.
	private float solarRadius; // The stars solar radius.
	private float solarLuminosity; // The stars solar luminosity.
	private float surfaceTemperature; // The stars surface temp in kelvin.

	private float solarLifetime; // The stars lifetime.

	private Colour surfColour; // The stars surface colour.

	private float escapeVelocity; // The stars escape velocity (km/s).

	private float planetInnerLimit; // The inner limit for planet formation.
	private float planetOuterLimit; // The outer limit for planet formation.
	private float planetFrostLine; // The planetary frost line for childObjects.

	private float habitableMin; // The habitable min distance in AU for carbon based life.
	private float habitableMax; // The habitable max distance in AU for carbon based life.

	/**
	 * Creates a new star from a solar mass and calculated characteristics.
	 *
	 * @param starName The stars name.
	 * @param solarMasses The stars mass in solar masses.
	 * @param position The position for the static star.
	 * @param childObjects The list of objects orbiting the star.
	 */
	public Star(String starName, float solarMasses, Vector3f position, List<Celestial> childObjects) {
		this.starName = starName;
		this.solarMasses = solarMasses;
		this.childObjects = childObjects;

		this.solarRadius = (float) Math.pow(solarMasses, solarMasses < 1.0f ? 0.8f : 0.5f);
		this.solarLuminosity = (float) Math.pow(solarMasses, 3.5f);
		this.surfaceTemperature = (float) Math.pow(solarLuminosity / (solarRadius * solarRadius), 0.25f) * 5778.0f;

		this.solarLifetime = (float) Math.pow(solarMasses, -2.5f);

		this.surfColour = getColour();

		this.escapeVelocity = (float) Math.sqrt(solarMasses / solarRadius) * 617.7f;

		this.planetInnerLimit = 0.1f * solarMasses;
		this.planetOuterLimit = 40.0f * solarMasses;
		this.planetFrostLine = 4.85f * (float) Math.sqrt(solarLuminosity);

		this.habitableMin = (float) Math.sqrt(solarLuminosity / 1.10f);
		this.habitableMax = (float) Math.sqrt(solarLuminosity / 0.53f);
	}

	/**
	 * Generates a colour for the star by its surface temp, used this paper:
	 * http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/
	 *
	 * @return Generated colour for a star.
	 */
	private Colour getColour() {
		float temperature = Maths.clamp(surfaceTemperature, 1000.0f, 40000.0f) / 100.0f;
		float red;
		float green;
		float blue;

		// Calculate Red.
		if (temperature <= 66.0f) {
			red = 255.0f;
		} else {
			red = temperature - 60.0f;
			red = 329.698727446f * (float) Math.pow(red, -0.1332047592f);
			red = Maths.clamp(red, 0.0f, 255.0f);
		}

		// Calculate Green.
		if (temperature <= 66.0f) {
			green = temperature;
			green = 99.4708025861f * (float) Math.log(green) - 161.1195681661f;
			green = Maths.clamp(green, 0.0f, 255.0f);
		} else {
			green = temperature - 60.0f;
			green = 288.1221695283f * (float) Math.pow(green, -0.0755148492f);
			green = Maths.clamp(green, 0.0f, 255.0f);
		}

		// Calculate Blue.
		if (temperature >= 66.0f) {
			blue = 255.0f;
		} else {
			if (temperature <= 19.0f) {
				blue = 0.0f;
			} else {
				blue = temperature - 10.0f;
				blue = 138.5177312231f * (float) Math.log(blue) - 305.0447927307f;
				blue = Maths.clamp(blue, 0.0f, 255.0f);
			}
		}

		return new Colour(red, green, blue, true);
	}

	public List<Celestial> getChildObjects() {
		return childObjects;
	}

	@Override
	public String toString() {
		return "Star(" + starName + " | " + StarType.getTypeMass(solarMasses).name() + ") [ \n   " +
				"solarMasses=" + solarMasses +
				", radius=" + solarRadius +
				", luminosity=" + solarLuminosity +
				", temperature=" + surfaceTemperature +
				", lifetime=" + solarLifetime +
				", escapeVelocity=" + escapeVelocity +
				", planetInnerLimit=" + planetInnerLimit +
				", planetOuterLimit=" + planetOuterLimit +
				", planetFrostLine=" + planetFrostLine +
				", habitableMin=" + habitableMin +
				", habitableMax=" + habitableMax +
				", " + surfColour.toString() + "\n]";
	}

	public void update() {
		childObjects.forEach(Celestial::update);
	}

	public String getStarName() {
		return starName;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getSolarMasses() {
		return solarMasses;
	}

	public float getSolarRadius() {
		return solarRadius;
	}

	public float getSolarLuminosity() {
		return solarLuminosity;
	}

	public float getSurfaceTemperature() {
		return surfaceTemperature;
	}

	public float getSolarLifetime() {
		return solarLifetime;
	}

	public Colour getSurfColour() {
		return surfColour;
	}

	public float getEscapeVelocity() {
		return escapeVelocity;
	}

	public float getPlanetInnerLimit() {
		return planetInnerLimit;
	}

	public float getPlanetOuterLimit() {
		return planetOuterLimit;
	}

	public float getPlanetFrostLine() {
		return planetFrostLine;
	}

	public float getHabitableMin() {
		return habitableMin;
	}

	public float getHabitableMax() {
		return habitableMax;
	}

	public enum StarType {
		O(0.003f, 60.0f),
		B(0.13f, 18.0f),
		A(0.6f, 3.2f),
		F(3.0f, 1.7f),
		G(7.717f, 1.1f),
		K(12.1f, 0.8f),
		M(76.45f, 0.3f);

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
