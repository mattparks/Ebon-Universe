package game.entities;

import flounder.helpers.*;
import game.editors.entity.*;
import game.entities.loading.*;

import javax.swing.*;

/**
 * Base class for all components that can be attached to engine.entities. (Have a constructor that takes in '(Entity entity, EntityTemplate template)' for the entity loader).
 */
public abstract class IEntityComponent {
	private Entity entity;
	private int id;

	/**
	 * Creates a component attached to a specific entity.
	 *
	 * @param entity The entity this component is attached to.
	 * @param id The id identifying the type of component. This should be unique to the subclass, but not unique to the object.
	 */
	public IEntityComponent(Entity entity, int id) {
		this.id = id;
		this.entity = entity;
		entity.addComponent(this);
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
	 * Gets the entity this is attached to.
	 *
	 * @return The entity this is attached to.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @return Returns a list of values that are saved with the component.
	 */
	public abstract Pair<String[], EntitySaverFunction[]> getSavableValues();

	/**
	 * Updates this component.
	 */
	public abstract void update();
}
