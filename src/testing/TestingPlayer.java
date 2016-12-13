package testing;

import flounder.camera.*;
import flounder.framework.*;
import flounder.maths.vectors.*;

public class TestingPlayer extends IExtension implements IPlayer {
	private Vector3f position;
	private Vector3f rotation;

	public TestingPlayer() {
		super(FlounderCamera.class);
	}

	@Override
	public void init() {
		position = new Vector3f(0.0f, 5.0f, 0.0f);
		rotation = new Vector3f(40.0f, 0.0f, 0.0f);
	}

	@Override
	public void update() {

	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
