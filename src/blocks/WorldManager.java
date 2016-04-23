package blocks;

import flounder.maths.vectors.*;
import flounder.noise.*;

import java.util.*;

public class WorldManager {
	private static final List<Chunk> CHUNK_LIST = new ArrayList<>();
	private static final NoisePerlin NOISE_PERLIN = new NoisePerlin((int) GameSeed.getSeed());

	public static void init() {
		for (int x = 0; x < 1; x++) {
			for (int z = 0; z < 1; z++) {
				CHUNK_LIST.add(new Chunk(new Vector3f(x * (Chunk.CHUNK_LENGTH * 2.0f), 0, z * (Chunk.CHUNK_LENGTH * 2.0f)), NOISE_PERLIN));
			}
		}
	}

	public static void update() {
		CHUNK_LIST.forEach(Chunk::update);
	}

	public static int renderableChunkFaces() {
		int count = 0;

		for (final Chunk chunk : CHUNK_LIST) {
			if (chunk.renderable()) {
				count += chunk.getFaceCount();
			}
		}

		return count;
	}

	public static List<Chunk> getChunkList() {
		return CHUNK_LIST;
	}

	public static Block getWorldBlock(final float positionX, final float positionY, final float positionZ) {
		// TODO: Make this work!

		for (final Chunk chunk : CHUNK_LIST) {
			if (positionX >= chunk.getPosition().x && positionX <= chunk.getPosition().x + Chunk.CHUNK_LENGTH) {
				if (positionZ >= chunk.getPosition().y && positionZ <= chunk.getPosition().y + Chunk.CHUNK_LENGTH) {
					return chunk.getBlock(Math.round(positionX - chunk.getPosition().x), Math.round(positionY), Math.round(positionZ - chunk.getPosition().y));
				}
			}
		}

		return null;
	}
}
