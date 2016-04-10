package game.planets;

import flounder.engine.*;
import flounder.maths.vectors.*;

import java.util.*;

public class Planet {
	private final Vector3f position;
	private PlanetChunk motherChunk;
	private PlanetChunk lastUpdateChunk;

	public Planet() {
		motherChunk = new PlanetChunk(null, 0, Vector3f.subtract(new Vector3f(0, 0, 0), new Vector3f(400 / 2, 0, 400 / 2), null), new Vector3f(0, 0, 0), 400, 4);
		motherChunk.subdivide();
		motherChunk.children[0].subdivide();
		motherChunk.children[0].children[0].subdivide();
		motherChunk.children[0].children[0].children[0].subdivide();
		position = new Vector3f(0, 0, 0);
	}

	public void update(final ICamera camera) {
		//	motherChunk.splitToPosition(camera.getPosition().getX(), camera.getPosition().getZ());
		//	PlanetChunk chunk = motherChunk.getAtPoint(camera.getPosition().getX(), camera.getPosition().getZ());
		//	motherChunk.resolveChildrenSplits(lastUpdateChunk, chunk);
		//	lastUpdateChunk = chunk;

		//if (lastUpdateChunk != null && chunk != null) {
		//if (!chunk.equals(lastUpdateChunk)) {
		//PlanetChunk lowestRelative = chunk.getParentLastRelatance(lastUpdateChunk);
		//if (lowestRelative != null && !motherChunk.equals(lowestRelative)) {
		//	if (lowestRelative.isDivided()) {
		//	}
		// }
		//if (lowestRelative != null) {
		//	if (lowestRelative.isDivided()) {
		//		lowestRelative.submerge();
		//	}
		//}
		//}
		//}
		//if (chunk != null && !chunk.isDivided()) {
		//	chunk.subdivide();
		//	lastUpdateChunk = chunk;
		//}
	}

	public List<PlanetChunk> getChunks() {
		return motherChunk.getChunks();
	}

	public Vector3f getPosition() {
		return position;
	}
}
