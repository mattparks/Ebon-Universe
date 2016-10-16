package ebon.universe.galaxies;

import ebon.universe.stars.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.space.*;

import java.util.*;

/**
 * A class that contains a list of stars that create a galaxy.
 */
public class Galaxy {
	private String galaxyName;
	private Vector3f position;

	private double radius;
	private int starCount;

	private ISpatialStructure<Star> stars;

	public Galaxy(String galaxyName, Vector3f position, double radius, int starCount) {
		this.galaxyName = galaxyName;
		this.position = position;

		this.radius = radius;
		this.starCount = starCount;
		this.stars = new StructureBasic<>();

		//	System.out.println();
		//	System.out.println(FlounderLogger.ANSI_RED + toString() + FlounderLogger.ANSI_RESET);
		//	System.out.println();

		generate();
	}

	private void generate() {
		/*int numArms = 2;
		double armSeparationDistance = (2.0 * Math.PI) / numArms;
		double armOffsetMax = 100.0;
		double rotationFactor = 0.05;
		double randomOffsetXYZ = 1.0;
		double maxGalacticRadius = 0.0;

		for (int i = 0; i < starCount; i++) {
			Vector3f starPosition;

			if (i < starCount * 0.925) {
				// Choose a distance from the center of the galaxy.
				double distance = Maths.RANDOM.nextDouble() * 15.0;
				distance = Math.pow(distance, 2.0);

				// Choose an angle between 0 and 2 * PI.
				double angle = Maths.RANDOM.nextDouble() * 2.0 * Math.PI;
				double armOffset = Maths.RANDOM.nextDouble() * armOffsetMax;
				armOffset = armOffset - armOffsetMax / 2.0;
				armOffset = armOffset * (1.0 / distance);

				double squaredArmOffset = Math.pow(armOffset, 2.0);

				if (armOffset < 0.0) {
					squaredArmOffset = squaredArmOffset * -1.0;
				}

				armOffset = squaredArmOffset;

				double rotation = distance * rotationFactor;

				angle = (int) (angle / armSeparationDistance) * armSeparationDistance + armOffset + rotation;

				// Convert polar coordinates to 2D cartesian coordinates.
				double starX = Math.cos(angle) * distance;
				double starY = 0.0;
				double starZ = Math.sin(angle) * distance;

				double randomOffsetX = Maths.RANDOM.nextDouble() * randomOffsetXYZ;
				double randomOffsetY = 10.0 * Maths.randomInRange(-1.0, 1.0) * randomOffsetXYZ;
				double randomOffsetZ = Maths.RANDOM.nextDouble() * randomOffsetXYZ;

				starX += randomOffsetX;
				starY += randomOffsetY;
				starZ += randomOffsetZ;

				starPosition = new Vector3f((float) starX, (float) starY, (float) starZ);

				double distanceFromCentre = Vector3f.getDistance(position, starPosition);

				if (distanceFromCentre > maxGalacticRadius) {
					maxGalacticRadius = distanceFromCentre;
				}
			} else {
				starPosition = randomClusterPoint(maxGalacticRadius * Math.E, maxGalacticRadius * Math.E, maxGalacticRadius * Math.E);
			}

			// Now it can assign the xyz coords.
			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(this, FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, starPosition, new ArrayList<>());
			stars.add(star);

			if (i == 0) {
				star.loadChildren();
			}
		}*/

		for (int i = 0; i < starCount; i++) {
			// Now it can assign the xyz coords.
			Star.StarType starType = Star.StarType.getTypeMakeup(Maths.randomInRange(0.0, 100.0));
			double solarMasses = Maths.randomInRange(starType.minSolarMasses, starType.maxSolarMasses);
			Star star = new Star(this, FauxGenerator.getFauxSentance(1, 6, 17), solarMasses, randomClusterPoint(radius, radius, radius), new ArrayList<>());
			stars.add(star);

			//	if (i == 0) {
			//		star.loadChildren();
			//	}
		}
	}

	private Vector3f randomSpherePoint(double radius) {
		Vector3f spawnPosition = new Vector3f();
		Maths.generateRandomUnitVector(spawnPosition);
		spawnPosition.scale((float) radius);
		double a = Maths.RANDOM.nextDouble();
		double b = Maths.RANDOM.nextDouble();

		if (a > b) {
			double temp = a;
			a = b;
			b = temp;
		}

		double randX = b * Math.cos(2.0 * Math.PI * (a / b));
		double randY = b * Math.sin(2.0 * Math.PI * (a / b));
		float distance = new Vector2f((float) randX, (float) randY).length();
		spawnPosition.scale(distance);
		return spawnPosition;
	}

	private Vector3f randomCirclePoint(double radius) {
		return Maths.randomPointOnCircle(new Vector3f(), new Vector3f(0.0f, 1.0f, 0.0f), (float) radius);
	}

	private Vector3f randomClusterPoint(double deviationX, double deviationY, double deviationZ) {
		return new Vector3f(Maths.normallyDistributedSingle((float) deviationX, 0.0f), Maths.normallyDistributedSingle((float) deviationY, 0.0f), Maths.normallyDistributedSingle((float) deviationZ, 0.0f));
	}

	public void update() {
		/*Vector3f angle = new Vector3f();

		for (Star star : stars.getAll(new ArrayList<>())) {
			double distance = Vector3f.getDistance(star.getPosition(), position);
			double rotation = Math.log(distance);
			angle.y = (float) rotation * FlounderEngine.getDelta();//10.0f * FlounderEngine.getDelta() * (Vector3f.getDistance(star.getPosition(), position) / (float) radius);
			Vector3f.rotate(star.getPosition(), angle, star.getPosition());
		}*/
	}

	public void reset() {
		//	stars.clear();
		//	generate();
	}

	public String getGalaxyName() {
		return galaxyName;
	}

	public ISpatialStructure<Star> getStars() {
		return stars;
	}

	@Override
	public String toString() {
		return "Galaxy(" + galaxyName + " | " + position.toString() + ") [ \n   " +
				"radius=" + radius +
				", starCount=" + starCount + ")\n]";
	}
}
