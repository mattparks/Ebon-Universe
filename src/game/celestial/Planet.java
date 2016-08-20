package game.celestial;

import flounder.maths.vectors.*;

public class Planet extends ICelestial {
	private Star parentStar;

	private float earthMasses; // The planets earth mass.
	private float earthRadius; // The planets earth radius.
	private float density; // The planets density.
	private float gravity; // The planets gravity.

	private float escapeVelocity; // The planets escape velocity.

	public Planet(Star parentStar, float earthMasses, float earthRadius) {
		super(new Vector3f(), new Vector3f());
		this.parentStar = parentStar;
		this.earthMasses = earthMasses;
		this.earthRadius = earthRadius;
		this.density = density;
		this.gravity = earthMasses / (earthRadius * earthRadius);

		this.escapeVelocity = (float) Math.sqrt(earthMasses / earthRadius);
	}

	@Override
	public void update() {
	}
}
