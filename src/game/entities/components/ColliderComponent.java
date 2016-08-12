package game.entities.components;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.physics.*;
import game.entities.*;
import game.entities.loading.*;

/**
 * Gives an object a collider for spatial interaction. Note that a collider doesn't necessarily need to be used for collision. A collider component can be used for any spatial interaction.
 * <p>
 * For example, a checkpoint can use a ColliderComponent to detect when the player has reached it.
 */
public class ColliderComponent extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private AABB aabb;

	/**
	 * Creates a new ColliderComponent.
	 *
	 * @param entity The entity this component is attached to.
	 */
	public ColliderComponent(Entity entity) {
		super(entity, ID);
		this.aabb = new AABB();
	}

	/**
	 * Creates a new ColliderComponent. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public ColliderComponent(Entity entity, EntityTemplate template) {
		this(entity);
	}

	/**
	 * @return Returns a AABB representing the basic collision range.
	 */
	public AABB getAABB() {
		ModelComponent modelComponent = (ModelComponent) getEntity().getComponent(ModelComponent.ID);

		if (modelComponent != null) {
			if (modelComponent.getModel() != null) {
				modelComponent.getModel().getAABB().recalculate(aabb, super.getEntity().getPosition(), super.getEntity().getRotation(), modelComponent.getScale());
				FlounderEngine.getAABBs().addAABBRender(aabb);
				return aabb;
			}
		}

		return null;
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		return new Pair<>(new String[]{}, new EntitySaverFunction[]{});
	}

	@Override
	public void update() {
	}
}
