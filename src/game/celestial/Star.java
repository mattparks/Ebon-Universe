package game.celestial;

import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;

import java.util.*;

/**
 * A realistic star object.
 */
public class Star {
	public static double SOL_MASS = 1.989e+30; // Our suns mass (kg).
	public static double SOL_RADIUS = 696300.0; // Our suns radius (km).
	public static double SOL_ESCAPE_VELOCITY = 617.7; // Our suns escape velocity (km/s).
	public static double SOL_SURFACE_TEMP = 5778.0; // Our suns surface temp (kelvin).

	private String starName;
	private Vector3f position;

	private List<Celestial> childObjects;

	private double solarMasses; // The stars solar mass.
	private double solarRadius; // The stars solar radius.
	private double solarLuminosity; // The stars solar luminosity.
	private double surfaceTemperature; // The stars surface temp (kelvin).

	private double solarLifetime; // The stars lifetime.

	private Colour surfaceColour; // The stars surface colour.

	private double escapeVelocity; // The stars escape velocity (km/s).

	private double planetInnerLimit; // The inner limit for planet formation.
	private double planetOuterLimit; // The outer limit for planet formation.
	private double planetFrostLine; // The planetary frost line for childObjects.

	private double habitableMin; // The habitable min distance in AU for carbon based life.
	private double habitableMax; // The habitable max distance in AU for carbon based life.

	/**
	 * Creates a new star from a solar mass and calculated characteristics.
	 *
	 * @param starName The stars name.
	 * @param solarMasses The stars mass in solar masses.
	 * @param position The position for the static star.
	 * @param childObjects The list of objects orbiting the star.
	 */
	public Star(String starName, double solarMasses, Vector3f position, List<Celestial> childObjects) {
		this.starName = starName;
		this.position = position;

		this.childObjects = childObjects;

		this.solarMasses = solarMasses;
		this.solarRadius = Math.pow(solarMasses, solarMasses < 1.0 ? 0.8 : 0.5);
		this.solarLuminosity = Math.pow(solarMasses, 3.5);
		this.surfaceTemperature = Math.pow(solarLuminosity / (solarRadius * solarRadius), 0.25) * SOL_SURFACE_TEMP;

		this.solarLifetime = Math.pow(solarMasses, -2.5);

		this.surfaceColour = getColour();

		this.escapeVelocity = Math.sqrt(solarMasses / solarRadius) * SOL_ESCAPE_VELOCITY;

		this.planetInnerLimit = 0.1 * solarMasses;
		this.planetOuterLimit = 40.0 * solarMasses;
		this.planetFrostLine = 4.85 * Math.sqrt(solarLuminosity);

		this.habitableMin = Math.sqrt(solarLuminosity / 1.11);
		this.habitableMax = Math.sqrt(solarLuminosity / 0.53);
	}

	/**
	 * Generates a colour for the star by its surface temp, used this paper:
	 * http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/
	 *
	 * @return Generated colour for a star.
	 */
	private Colour getColour() {
		double temperature = Maths.clamp(surfaceTemperature, 1000.0, 40000.0) / 100.0;
		double red;
		double green;
		double blue;

		// Calculate Red.
		if (temperature <= 66.0) {
			red = 255.0;
		} else {
			red = temperature - 60.0;
			red = 329.698727446 * Math.pow(red, -0.1332047592);
			red = Maths.clamp(red, 0.0, 255.0);
		}

		// Calculate Green.
		if (temperature <= 66.0) {
			green = temperature;
			green = 99.4708025861 * Math.log(green) - 161.1195681661;
			green = Maths.clamp(green, 0.0, 255.0);
		} else {
			green = temperature - 60.0;
			green = 288.1221695283 * Math.pow(green, -0.0755148492);
			green = Maths.clamp(green, 0.0, 255.0);
		}

		// Calculate Blue.
		if (temperature >= 66.0) {
			blue = 255.0;
		} else {
			if (temperature <= 19.0) {
				blue = 0.0;
			} else {
				blue = temperature - 10.0;
				blue = 138.5177312231 * Math.log(blue) - 305.0447927307;
				blue = Maths.clamp(blue, 0.0, 255.0);
			}
		}

		return new Colour((float) (red / 255.0), (float) (green / 255.0), (float) (blue / 255.0));
	}

	public List<Celestial> getChildObjects() {
		return childObjects;
	}

	@Override
	public String toString() {
		return "Star(" + starName + " | " + StarType.getTypeMass(solarMasses).name() + ") [ \n   " +
				"minSolarMasses=" + solarMasses +
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
				", " + surfaceColour.toString() + "\n]";
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

	public double getSolarMasses() {
		return solarMasses;
	}

	public double getSolarRadius() {
		return solarRadius;
	}

	public double getSolarLuminosity() {
		return solarLuminosity;
	}

	public double getSurfaceTemperature() {
		return surfaceTemperature;
	}

	public double getSolarLifetime() {
		return solarLifetime;
	}

	public Colour getSurfaceColour() {
		return surfaceColour;
	}

	public double getEscapeVelocity() {
		return escapeVelocity;
	}

	public double getPlanetInnerLimit() {
		return planetInnerLimit;
	}

	public double getPlanetOuterLimit() {
		return planetOuterLimit;
	}

	public double getPlanetFrostLine() {
		return planetFrostLine;
	}

	public double getHabitableMin() {
		return habitableMin;
	}

	public double getHabitableMax() {
		return habitableMax;
	}

	public static void printSystem(Star star) {
		System.out.println("");
		System.out.println(FlounderLogger.ANSI_RED + "===== Star " + star.getStarName() + ". =====" + FlounderLogger.ANSI_RESET);
		System.out.println(FlounderLogger.ANSI_RED + star.toString() + FlounderLogger.ANSI_RESET);

		ArraySorting.heapSort(star.childObjects);

		star.childObjects.forEach(celestial -> {
			if (celestial.supportsLife()) {
				System.out.println(FlounderLogger.ANSI_PURPLE + celestial.toString() + FlounderLogger.ANSI_RESET);
			} else {
				System.out.println(FlounderLogger.ANSI_GREEN + celestial.toString() + FlounderLogger.ANSI_RESET);
			}

			ArraySorting.heapSort(celestial.getChildObjects());

			celestial.getChildObjects().forEach(moon -> {
				System.out.println(FlounderLogger.ANSI_BLUE + moon.toString() + FlounderLogger.ANSI_RESET);
			});
		});

		System.out.println(FlounderLogger.ANSI_RED + "===== End of star " + star.getStarName() + ". =====\n" + FlounderLogger.ANSI_RESET);
	}

	public enum StarType {
		O(0.0003, 30.0, 60.0),
		B(0.1327, 18.0, 30.0),
		A(0.6, 3.2, 18.0),
		F(3.0, 1.7, 3.2),
		G(7.717, 1.1, 1.7),
		K(12.1, 0.8, 1.1),
		M(76.45, 0.3, 0.8);

		public double universeMakeup; // How much of the universe if made up of this star type.
		public double minSolarMasses; // The stars min solar mass.
		public double maxSolarMasses; // The stars max solar mass.

		StarType(double universeMakeup, double minSolarMasses, double maxSolarMasses) {
			this.universeMakeup = universeMakeup;
			this.minSolarMasses = minSolarMasses;
			this.maxSolarMasses = maxSolarMasses;
		}

		public static StarType getTypeMakeup(double solarMakeup) {
			double currentMakeup = 0.0;

			for (StarType type : StarType.values()) {
				if (solarMakeup <= currentMakeup) {
					return type;
				}

				currentMakeup += type.universeMakeup;
			}

			return M;
		}

		public static StarType getTypeMass(double solarMasses) {
			for (StarType type : StarType.values()) {
				if (solarMasses <= type.maxSolarMasses && solarMasses > type.minSolarMasses) {
					return type;
				}
			}

			return M;
		}

		@Override
		public String toString() {
			return "StarType(" + name() + ")[ " +
					"universeMakeup=" + universeMakeup +
					", minSolarMasses=" + minSolarMasses +
					", maxSolarMasses=" + maxSolarMasses + " ]";
		}
	}
}
