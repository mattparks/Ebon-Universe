package game.celestial;

import flounder.maths.*;
import flounder.maths.vectors.*;

import java.util.*;

public class StarGenerator {
	public static Star generateStar(Vector3f position) {
		float spawnKey = Maths.RANDOM.nextFloat() * (100.0f) + 100.0f; // TODO: Perlin noise.
		float currentMakeup = 0;
		Star.StarType starType = null;

		for (Star.StarType type : Star.StarType.values()) {
			if (spawnKey <= type.universeMakeup) {
				starType = type;
				break;
			}

			currentMakeup += type.universeMakeup;
		}

		List<Planet> planets = new ArrayList<>();

		return new Star(starType, position, planets);
	}
}
