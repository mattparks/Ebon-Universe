package game.celestial;

import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;

import java.util.*;

/**
 * A realistic star object.
 */
public class Star implements Comparable<Star>, ISpatialObject {
	public static double SOL_MASS = 1.989e+30; // Our suns mass (kg).
	public static double SOL_RADIUS = 696300.0; // Our suns radius (km).
	public static double SOL_ESCAPE_VELOCITY = 617.7; // Our suns escape velocity (km/s).
	public static double SOL_SURFACE_TEMP = 5778.0; // Our suns surface temp (kelvin).

	private String starName;
	private Vector3f position;

	private List<Celestial> childObjects;

	private StarType starType; // The type of this star.
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

	private AABB starAABB;

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

		this.starType = StarType.getTypeMass(solarMasses);
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

		this.starAABB = new AABB();
		float size = (float) (0.5f * this.solarRadius);
		starAABB.getMinExtents().set(position.getX() - size, position.getY() - size, position.getZ() - size);
		starAABB.getMaxExtents().set(position.getX() + size, position.getY() + size, position.getZ() + size);
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

	public void loadChildren() {
		double currentOrbit = planetFrostLine;

		while (currentOrbit >= planetInnerLimit) {
			if ((currentOrbit >= habitableMin && currentOrbit <= habitableMax) || Maths.RANDOM.nextBoolean()) {
				generateCelestial("Planet", new Pair<>(this, null), currentOrbit);
			}

			currentOrbit /= Maths.randomInRange(1.4, 2.0);
		}

		currentOrbit = planetFrostLine + 1.0;

		while (currentOrbit <= planetOuterLimit) {
			if (Maths.RANDOM.nextBoolean()) {
				generateCelestial("Planet", new Pair<>(this, null), currentOrbit);
			}

			currentOrbit *= Maths.randomInRange(1.4, 2.0);
		}
	}

	private static void generateCelestial(String celestialName, Pair<Star, Celestial> parentTypes, double semiMajorAxis) {
		Star star;
		String parentName;
		double parentSolarMasses;
		double earthMasses;
		double eccentricity;

		if (parentTypes.getFirst() != null) {
			star = parentTypes.getFirst();
			parentName = star.getStarName();
			parentSolarMasses = star.getSolarMasses();
			earthMasses = Maths.logRandom(0.1, 1000.0);
			eccentricity = 0.584 * Math.pow(Math.max(star.getChildObjects().size(), 2), -1.2);
		} else if (parentTypes.getSecond() != null) {
			star = parentTypes.getSecond().getParentStar();
			parentName = parentTypes.getSecond().getPlanetName();
			parentSolarMasses = (parentTypes.getSecond().getEarthMasses() * Celestial.EARTH_MASS) / Star.SOL_MASS;
			earthMasses = Maths.randomInRange(0.1, parentTypes.getSecond().getEarthMasses());
			eccentricity = Maths.randomInRange(0.0, 0.2);
		} else {
			return;
		}

		double earthRadius = 1.0;

		Orbit orbit = new Orbit(
				eccentricity, semiMajorAxis, parentSolarMasses,
				Maths.randomInRange(0.0, 180.0), Maths.randomInRange(0.0, 360.0), Maths.randomInRange(0.0, 360.0)
		);

		Celestial celestial = new Celestial(celestialName, parentName + " " + FauxGenerator.getFauxSentance(1, 4, 12),
				parentTypes, orbit, earthMasses,
				earthRadius, Maths.randomInRange(0.0, 40.0) * (Maths.RANDOM.nextBoolean() ? 1.0 : -1.0), new ArrayList<>()
		);

		/*Orbit moonOrbit = new Orbit(
				0.0549006, 0.00257188153, (float) (celestial.getEarthMasses() * 5.9723 * Math.pow(10, 24)) / (float) (1.989 * Math.pow(10, 30)), // Maths.RANDOM.nextInt(6000) / 10000.0f
				0, 0, 0//Maths.RANDOM.nextInt(1800) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f, Maths.RANDOM.nextInt(3600) / 10.0f
		);

		Celestial moon = new Celestial(celestialName, celestial.getPlanetName() + " " + FauxGenerator.getFauxSentance(1, 2, 4),
				parentTypes, moonOrbit, 0.01230743469,
				0.27264165751, Maths.RANDOM.nextInt(400) * (Maths.RANDOM.nextBoolean() ? 1 : -1) / 10.0, new ArrayList<>()
		);*/

		/*if (parentTypes.getFirst() != null) {
			System.out.println(FlounderLogger.ANSI_BLUE + celestial.toString() + FlounderLogger.ANSI_RESET);

			float currentOrbit = celestial.getMinRingSpawns();//star.getPlanetInnerLimit();

			while (currentOrbit < celestial.getMaxRingSpawns()) {
				if (Maths.RANDOM.nextBoolean()) {
					generateCelestial("Moon", new Pair<>(null, celestial), currentOrbit);
				}

				currentOrbit += Maths.randomInRange(2.4, 3.0);
			}
		}*/

		star.getChildObjects().add(celestial);
	}

	public List<Celestial> getChildObjects() {
		return childObjects;
	}

	@Override
	public String toString() {
		return "Star(" + starName + " | " + StarType.getTypeMass(solarMasses).name() + " | " + position.toString() + ") [ \n   " +
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
				", surfaceColour(" + Maths.roundToPlace(surfaceColour.r * 255.0f, 0) + ", " + Maths.roundToPlace(surfaceColour.g * 255.0f, 0) + ", " + Maths.roundToPlace(surfaceColour.b * 255.0f, 0) + ", " + ")\n]";
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

	public StarType getStarType() {
		return starType;
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

	@Override
	public AABB getAABB() {
		return starAABB;
	}

	@Override
	public int compareTo(Star o) {
		return starType.compareTo(o.starType);
	}

	public enum StarType {
		O(0.004, 30.0, 60.0),
		B(0.116, 18.0, 30.0),
		A(0.600, 3.2, 18.0),
		F(3.000, 1.7, 3.2),
		G(7.600, 1.1, 1.7),
		K(12.23, 0.8, 1.1),
		M(76.45, 0.3, 0.8);

		private static StarType[] VALUES = StarType.values();

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

			for (StarType type : VALUES) {
				currentMakeup += type.universeMakeup;

				if (solarMakeup <= currentMakeup) {
					return type;
				}
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
