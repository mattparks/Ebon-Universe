package game.blocks;

import flounder.maths.vectors.*;

import java.util.*;

public class ChunkManager {
	private final List<Chunk> chunkList;

	public ChunkManager() {
		this.chunkList = new ArrayList<>();
		this.chunkList.add(new Chunk(new Vector2f(0.0f, 0.0f)));
	}

	public void update() {
		chunkList.forEach(Chunk::update);
	}
}
