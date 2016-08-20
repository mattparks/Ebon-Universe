package game.celestial;

import flounder.engine.*;
import flounder.maths.vectors.*;

public class Planet extends ICelestial {
	private Star parentStar;

	private float startPeriapsis;
	private float starApoapsis;
	private float inclination;

	private float orbitTime;
	private float progression;

	public Planet(Star parentStar, float startPeriapsis, float starApoapsis, float inclination, float orbitTime) {
		super(new Vector3f(), new Vector3f());

		this.parentStar = parentStar;

		this.startPeriapsis = startPeriapsis;
		this.starApoapsis = starApoapsis;
		this.inclination = inclination;

		this.orbitTime = orbitTime;
		this.progression = 0.0f;
	}

	@Override
	public void update() {
		Vector3f starPosition = parentStar.getPosition();
		progression += FlounderEngine.getDelta() * orbitTime;
	}
}
