package game.world;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;
import game.*;

import java.util.*;

public class World {
	private StructureBasic<Chunk> chunks;
	private AABB playerRange;

	public World() {
		chunks = new StructureBasic<>();
		playerRange = new AABB();

		for (int i = -3; i < 3; i++) {
			for (int j = -3; j < 3; j++) {
				chunks.add(new Chunk(i, j));
			}
		}
	}

	public void update() {
		Vector3f playerPosition = FlounderEngine.getCamera().getPosition();
		float halfTerrainSpan = Environment.TERRAIN_SIZE * 3.0f;
		playerRange.getMinExtents().set(playerPosition.x - halfTerrainSpan, playerPosition.y - halfTerrainSpan, playerPosition.z - halfTerrainSpan);
		playerRange.getMaxExtents().set(playerPosition.x + halfTerrainSpan, playerPosition.y + halfTerrainSpan, playerPosition.z + halfTerrainSpan);

		List<Chunk> chunkList = new ArrayList<>();
		chunks.getAll(chunkList).forEach(chunk -> {
			chunk.update();
			chunk.setInPlayerRange(playerRange.contains(chunk.getAABB()) || chunk.getAABB().intersects(playerRange).isIntersection());
		});
	}

	public List<Chunk> getChunks(ICamera camera) {
		List<Chunk> chunkList = new ArrayList<>();
		return chunks.queryInFrustum(chunkList, camera.getViewFrustum());
	}

	public void dispose() {
		List<Chunk> chunkList = new ArrayList<>();
		chunks.getAll(chunkList).forEach(Chunk::dispose);
	}
}
