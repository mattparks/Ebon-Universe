package game.blocks;

import flounder.devices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.sounds.*;
import game.*;
import game.blocks.components.*;

import java.util.*;

/**
 * A generic block object in the game.
 */
public class Block extends AABB {
	private String type;
	private Vector3f position;
	private List<IBlockComponent> components;

	/**
	 * Creates a new block with minimum necessary construction.
	 *
	 * @param type The game.blocks {@link game.blocks.BlockTypes}.
	 * @param position The location of the block in the world.
	 */
	public Block(String type, Vector3f position) {
		this.components = new ArrayList<>();
		this.type = type;
		this.position = position;

		super.setMinExtents(position.x - BlockTypes.BLOCK_EXTENT, position.y - BlockTypes.BLOCK_EXTENT, position.z - BlockTypes.BLOCK_EXTENT);
		super.setMaxExtents(position.x + BlockTypes.BLOCK_EXTENT, position.y + BlockTypes.BLOCK_EXTENT, position.z + BlockTypes.BLOCK_EXTENT);
	}

	/**
	 * Plays the game.blocks type place sound.
	 */
	protected void playPlaceNoise() {
		FlounderDevices.getSound().play3DSound(PlayRequest.new3dSoundPlayRequest(BlockTypes.get(type).getPlaceSound(), 1.0f, position, 16.0f, 64.0f));
	}

	/**
	 * Plays the game.blocks type break sound.
	 */
	protected void playBreakNoise() {
		FlounderDevices.getSound().play3DSound(PlayRequest.new3dSoundPlayRequest(BlockTypes.get(type).getBreakSound(), 1.0f, position, 16.0f, 64.0f));
	}

	/**
	 * Updates all the components attached to this block.
	 *
	 * @param chunk The blocks parent chunk.
	 */
	protected void update(Chunk chunk) {
		if (components.size() < 1) {
			return;
		}

		if (components.size() > 0) {
			for (IBlockComponent blockComponent : components) {
				blockComponent.update(chunk);
			}
		}
	}

	protected boolean isCovered(Chunk chunk) {
		for (int s = 0; s < 6; s++) {
			int currZ = Chunk.inverseBlock(chunk.getPosition().z, position.z) + ((s == 0) ? 1 : (s == 1) ? -1 : 0); // Front / Back
			int currX = Chunk.inverseBlock(chunk.getPosition().x, position.x) + ((s == 2) ? -1 : (s == 3) ? 1 : 0); // Left / Right
			int currY = Chunk.inverseBlock(chunk.getPosition().y, position.y) + ((s == 4) ? 1 : (s == 5) ? -1 : 0); // Up / Down
			float cz = Chunk.calculateBlock(chunk.getPosition().z, currZ);
			float cx = Chunk.calculateBlock(chunk.getPosition().x, currX);
			float cy = Chunk.calculateBlock(chunk.getPosition().y, currY);

			if (!Environment.getBlocksManager().blockExists(cx, cy, cz)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Removes a component from this block by id. If more than one is found, the first component in the list is removed. If none are found, nothing is removed.
	 *
	 * @param id The id of the component. This is typically found with ComponentClass.ID.
	 */
	public void removeComponent(int id) {
		for (IBlockComponent c : components) {
			if (c.getId() == id) {
				components.remove(c);
				return;
			}
		}
	}

	/**
	 * Finds and returns a component attached to this block by id. If more than one is found, the first component in the list is returned. If none are found, returns null.
	 *
	 * @param id The id of the component. This is typically found with ComponentClass.ID.
	 *
	 * @return The first component found with the given id, or null if none are found.
	 */
	public IBlockComponent getComponent(int id) {
		for (IBlockComponent component : components) {
			if (component.getId() == id) {
				return component;
			}
		}

		return null;
	}

	/**
	 * Adds a new component to the block
	 *
	 * @param component The component to add.
	 */
	public void addComponent(IBlockComponent component) {
		components.add(component);
	}

	/**
	 * Removes a component to the block.
	 *
	 * @param component The component to remove.
	 */
	public void removeComponent(IBlockComponent component) {
		components.remove(component);
	}

	public String getType() {
		return type;
	}

	public Vector3f getPosition() {
		return position;
	}
}
