package game.celestial;

import flounder.maths.vectors.*;

public abstract class ICelestial {
	private Vector3f position;
	private Vector3f rotation;

	public ICelestial(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	public abstract void update();

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
}
