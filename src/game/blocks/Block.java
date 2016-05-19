package game.blocks;

import flounder.devices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.sounds.*;
import game.blocks.components.*;

import java.util.*;

/**
 * A generic block object in the game.
 */
public class Block extends AABB {
	private final String type;
	private final Vector3f position;
	private List<IBlockComponent> components;

	/**
	 * Creates a new block with minimum necessary construction.
	 *
	 * @param type The game.blocks {@link game.blocks.BlockTypes}.
	 * @param position The location of the block in the world.
	 */
	public Block(final String type, final Vector3f position) {
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
		ManagerDevices.getSound().play3DSound(PlayRequest.new3dSoundPlayRequest(BlockTypes.get(type).getPlaceSound(), 1.0f, position, 16.0f, 64.0f));
	}

	/**
	 * Plays the game.blocks type break sound.
	 */
	protected void playBreakNoise() {
		ManagerDevices.getSound().play3DSound(PlayRequest.new3dSoundPlayRequest(BlockTypes.get(type).getBreakSound(), 1.0f, position, 16.0f, 64.0f));
	}

	/**
	 * Updates all the components attached to this block.
	 */
	protected void update(final Chunk chunk) {
		if (components.size() < 1) {
			return;
		}

		for (final IBlockComponent blockComponent : components) {
			blockComponent.update(chunk);
		}
	}

	protected boolean isCovered(final Chunk chunk) {
		for (int s = 0; s < 6; s++) {
			final int currZ = Chunk.inverseBlock(chunk.getPosition().z, position.z) + ((s == 0) ? 1 : (s == 1) ? -1 : 0); // Front / Back
			final int currX = Chunk.inverseBlock(chunk.getPosition().x, position.x) + ((s == 2) ? -1 : (s == 3) ? 1 : 0); // Left / Right
			final int currY = Chunk.inverseBlock(chunk.getPosition().y, position.y) + ((s == 4) ? 1 : (s == 5) ? -1 : 0); // Up / Down
			final float cz = Chunk.calculateBlock(chunk.getPosition().z, currZ);
			final float cx = Chunk.calculateBlock(chunk.getPosition().x, currX);
			final float cy = Chunk.calculateBlock(chunk.getPosition().y, currY);

			if (!WorldManager.blockExists(cx, cy, cz)) {
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
