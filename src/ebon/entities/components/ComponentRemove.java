package ebon.entities.components;

import flounder.entities.*;
import flounder.entities.components.*;
import flounder.physics.*;

/**
 * Performs some function when an entity is removed.
 */
public abstract class ComponentRemove extends IComponentEntity {
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
	 * Activates this component, then removes the entity. Calls the onActivate function, and begins calling the removeUpdate function on every update.
	 */
	public void activate() {
		super.getEntity().setRemoved(true);
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

	@Override
	public IBounding getBounding() {
		return null;
	}

	/**
	 * Called every update after the entity is removed.
	 */
	public abstract void removeUpdate();
}
