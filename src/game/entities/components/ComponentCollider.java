package game.entities.components;

import flounder.engine.*;
import flounder.physics.*;
import game.entities.*;
import game.entities.loading.*;

/**
 * Gives an object a collider for spatial interaction. Note that a collider doesn't necessarily need to be used for collision. A collider component can be used for any spatial interaction.
 * <p>
 * For example, a checkpoint can use a ComponentCollider to detect when the player has reached it.
 */
public class ComponentCollider extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private AABB aabb;
	private QuickHull hull;

	/**
	 * Creates a new ComponentCollider.
	 *
	 * @param entity The entity this component is attached to.
	 */
	public ComponentCollider(Entity entity) {
		super(entity, ID);
		this.aabb = new AABB();
		this.hull = new QuickHull();
	}

	/**
	 * Creates a new ComponentCollider. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public ComponentCollider(Entity entity, EntityTemplate template) {
		super(entity, ID);
		this.aabb = new AABB();
		this.hull = new QuickHull();
	}

	/**
	 * @return Returns a AABB representing the basic collision range.
	 */
	public AABB getAABB() {
		return aabb;
	}

	/**
	 * Gets the models convex hull.
	 *
	 * @return The models convex hull.
	 */
	public QuickHull getHull() {
		return hull;
	}

	@Override
	public void update() {
		if (super.getEntity().hasMoved()) {
			ComponentModel modelComponent = (ComponentModel) getEntity().getComponent(ComponentModel.ID);

			if (modelComponent != null && modelComponent.getModel() != null) {
				AABB.recalculate(modelComponent.getModel().getAABB(), aabb, super.getEntity().getPosition(), super.getEntity().getRotation(), modelComponent.getScale());
				QuickHull.recalculate(modelComponent.getModel().getHull(), hull, super.getEntity().getPosition(), super.getEntity().getRotation(), modelComponent.getScale());
			}
		}

		FlounderEngine.getAABBs().addAABBRender(aabb);
	}

	@Override
	public void dispose() {
	}
}
