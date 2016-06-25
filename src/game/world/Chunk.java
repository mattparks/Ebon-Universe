package game.world;

import flounder.engine.*;
import flounder.physics.*;
import flounder.space.*;
import game.*;
import game.terrains.*;
import game.waters.*;

public class Chunk implements ISpatialObject {
	private Terrain terrain;
	private Water water;
	private boolean inPlayerRange;

	public Chunk(int gridX, int gridZ) {
		terrain = new Terrain(gridX, gridZ, Environment.TERRAIN_TEXTURE_PACK);
		water = new Water(terrain.getPosition().x + (Environment.TERRAIN_SIZE / 2.0f), terrain.getPosition().z + (Environment.TERRAIN_SIZE / 2.0f));
		inPlayerRange = true;
	}

	public void update() {
		FlounderEngine.getAABBs().addAABBRender(this.getAABB());
	}

	@Override
	public AABB getAABB() {
		return terrain.getAABB();
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public Water getWater() {
		return water;
	}

	public boolean inPlayerRange() {
		return inPlayerRange;
	}

	public void setInPlayerRange(boolean inPlayerRange) {
		this.inPlayerRange = inPlayerRange;
	}

	public void dispose() {

	}
}
