package game.celestial;

import flounder.maths.*;
import flounder.maths.vectors.*;

import java.util.*;

public class Star extends ICelestial {
	private StarType starType;
	private List<Planet> planets;

	public Star(StarType starType, Vector3f position, List<Planet> planets) {
		super(position, new Vector3f());
		this.starType = starType;
		this.planets = planets;
	}

	public StarType getStarType() {
		return starType;
	}

	public List<Planet> getPlanets() {
		return planets;
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

		public float universeMakeup; // Dobermans how much of the universe if made up of this star type.

		public float solarMasses; // The stars solar mass.
		public float solarRadius; // The stars solar radius.
		public float solarLumino; // The stars solar luminosity.
		public float surfaceTemp; // The stars surface temp in kelvin.
		public Colour surfColour; // The stars surface colour.

		public float habitableMin; // The habitable min distance in AU for carbon based life.
		public float habitableMax; // The habitable max distance in AU for carbon based life.

		StarType(float universeMakeup, float solarMasses) {
			this.universeMakeup = universeMakeup;

			this.solarMasses = solarMasses;
			this.solarRadius = (float) Math.pow(solarMasses, solarMasses < 1.0f ? 0.8f : 0.5f);
			this.solarLumino = (float) Math.pow(solarMasses, 3.5f);
			this.surfaceTemp = (float) Math.pow(solarLumino / (solarRadius * solarRadius), 0.25f) * 5778.0f;
			this.surfColour = getColour(surfaceTemp);

			this.habitableMin = (float) Math.sqrt(solarLumino / 1.10f);
			this.habitableMax = (float) Math.sqrt(solarLumino / 0.53f);
		}

		public static Colour getColour(float temperature) {
			if (temperature >= 30000) {
				// #9BB0FF
			} else if (temperature >= 10000) {
				// #AABFF
			} else if (temperature >= 7500) {
				// #CAD7FF
			} else if (temperature >= 5200) {
				// #FFF4EA
			} else if (temperature >= 3700) {
				// #FFD2A1
			} else if (temperature >= 2400) {
				// #FFCC6F
			} else {
				// Not hot enough to be a star!
			}

			return new Colour(1,1,1);
		}
	}
}
