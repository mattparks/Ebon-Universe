package game;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.physics.*;

import java.util.*;

public class PlayerManager {
	public static Map<String, Pair<Vector3f, AABB>> PLAYERS = new HashMap<>();

	public static void update() {
		PLAYERS.forEach((s, pair) -> FlounderEngine.getAABBs().addAABBRender(pair.getSecond()));
	}

	public static void movePlayer(Vector3f position, Vector3f rotation) {
		new MovePacket(FlounderEngine.getNetwork().getUsername(), position, rotation).writeData(FlounderEngine.getNetwork().getSocketClient());
	}
}
