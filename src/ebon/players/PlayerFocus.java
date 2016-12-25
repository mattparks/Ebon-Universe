package ebon.players;

import flounder.camera.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.maths.vectors.*;

public class PlayerFocus extends IExtension implements IPlayer {
	private Vector3f position;
	private Vector3f rotation;

	public PlayerFocus() {
		super(FlounderGuis.class);
	}

	@Override
	public void init() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
	}

	@Override
	public void update() {

	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation.set(rotation);
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
