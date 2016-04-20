package game.blocks;

import flounder.maths.vectors.*;
import flounder.noise.*;

import java.util.*;

public class ChunkManager {
	private static final List<Chunk> chunkList = new ArrayList<>();
	private static final NoisePerlin perlinNoise = new NoisePerlin((int)GameSeed.getSeed());

	public static void init() {
	//	chunkList.add(new Chunk(new Vector2f(0, 0), perlinNoise));

		for (int x = 0; x < 1; x++) {
			for (int z = 0; z < 1; z++) {
				chunkList.add(new Chunk(new Vector2f(x * (Chunk.CHUNK_LENGTH * 2.0f), z * (Chunk.CHUNK_LENGTH * 2.0f)), perlinNoise));
			}
		}
	}

	public static List<Chunk> getChunkList() {
		return chunkList;
	}

	public static void update() {
		chunkList.forEach(Chunk::update);
		// TODO: Sort chunks back to front.
	}
}
