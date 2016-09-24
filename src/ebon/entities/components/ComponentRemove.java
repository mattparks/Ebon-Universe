package ebon.entities.components;

import ebon.entities.*;

/**
 * Performs some function when an entity is removed.
 */
public abstract class ComponentRemove extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private boolean activated;

	/**
	 * Creates a new ComponentRemove.
	 *
	 * @param entity The entity this component is attached to.
	 */
	public ComponentRemove(Entity entity) {
		super(entity, ID);
		activated = false;
	}

	public void deactivate() {
		activated = false;
	}

	/**
	 * Activates this component. Calls the onActivate function, and begins calling the removeUpdate function on every update.
	 */
	public void activate() {
		activated = true;
		onActivate();
	}

	/**
	 * Called when the entity is first removed.
	 */
	public abstract void onActivate();

	@Override
	public void update() {
		if (!activated) {
			return;
		}

		removeUpdate();
	}

	/**
	 * Called every update after the entity is removed.
	 */
	public abstract void removeUpdate();
}
