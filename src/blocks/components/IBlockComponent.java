package blocks.components;

import blocks.*;

/**
 * Base class for all components that can be attached to block.Block.
 */
public abstract class IBlockComponent {
	private Block block;
	private int id;

	/**
	 * Creates a component attached to a specific block.
	 *
	 * @param block The block this component is attached to.
	 * @param id The id identifying the type of component. This should be unique to the subclass, but not unique to the object.
	 */
	public IBlockComponent(Block block, int id) {
		this.id = id;
		this.block = block;
		block.addComponent(this);
	}

	/**
	 * Gets the id of this component.
	 *
	 * @return The id of this component.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the block this is attached to.
	 *
	 * @return The block this is attached to.
	 */
	public Block getEntity() {
		return block;
	}

	/**
	 * Updates this component.
	 *
	 * @param chunk The blocks parent chunk.
	 */
	public abstract void update(final Chunk chunk);
}
