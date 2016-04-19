package game.blocks;

import flounder.maths.vectors.*;

import java.util.*;

public class ChunkManager {
	private static final List<Chunk> chunkList = new ArrayList<>();

	public static void init() {
		chunkList.add(new Chunk(new Vector2f(0, 0)));

	//	for (int x = 0; x < 9; x++) {
	//		for (int z = 0; z < 9; z++) {
	//			chunkList.add(new Chunk(new Vector2f(x, z)));
	//		}
	//	}
	}

	public static List<Chunk> getChunkList() {
		return chunkList;
	}

	public static void update() {
		chunkList.forEach(Chunk::update);
		// TODO: Sort chunks back to front.
	}
}
