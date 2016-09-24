package ebon.players;

import flounder.maths.vectors.*;

public interface IPlayer {
	void init();

	void update(boolean paused);

	Vector3f getPosition();

	Vector3f getRotation();

	void dispose();
}
