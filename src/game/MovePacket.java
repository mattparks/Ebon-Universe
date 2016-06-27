package game;


import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.networking.*;
import flounder.networking.packets.*;
import flounder.physics.*;

import java.net.*;

public class MovePacket extends Packet {
	private String username;
	private Vector3f position;
	private Vector3f rotation;

	public MovePacket(byte[] data) {
		this.username = readData(data).split("#")[0];
		String position = readData(data).split("#")[1];
		position = position.substring(1, position.length() - 1);
		String[] positionComps = position.trim().split(",");
		this.position = new Vector3f(Float.parseFloat(positionComps[0]), Float.parseFloat(positionComps[1]), Float.parseFloat(positionComps[2]));
		String rotation = readData(data).split("#")[2];
		rotation = rotation.substring(1, rotation.length() - 1);
		String[] rotationComps = rotation.trim().split(",");
		this.rotation = new Vector3f(Float.parseFloat(rotationComps[0]), Float.parseFloat(rotationComps[1]), Float.parseFloat(rotationComps[2]));
	}

	public MovePacket(String username, Vector3f position, Vector3f rotation) {
		this.username = username;
		this.position = position;
		this.rotation = rotation;
	}

	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(Server server) {
		server.sentDataToOtherClient(getData(), username);
	}

	@Override
	public void clientHandlePacket(Client client, InetAddress address, int port) {
		Pair<Vector3f, AABB> player = PlayerManager.PLAYERS.get(username);

		if (player == null) {
			player = new Pair<>(new Vector3f(), new AABB());
			PlayerManager.PLAYERS.put(username, player);
		}

		player.getFirst().set(position);
		player.getSecond().getMinExtents().set(position.x - 1.0f, position.y - 1.0f, position.z - 1.0f);
		player.getSecond().getMaxExtents().set(position.x + 1.0f, position.y + 1.0f, position.z + 1.0f);
		player.getSecond().getRotation().set(rotation);
	}

	@Override
	public void serverHandlePacket(Server server, InetAddress address, int port) {
		writeData(server);
	}

	@Override
	public byte[] getData() {
		return (getDataPrefix() + username + "#[" + position.x + ","+ position.y + "," + position.z + "]" + "#[" + rotation.x + ","+ rotation.y + "," + rotation.z + "]").getBytes();
	}

	public String getUsername() {
		return username;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
}
