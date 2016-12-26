package ebon.entities.components;

import ebon.entities.*;
import ebon.entities.loading.*;
import flounder.physics.*;
import flounder.physics.bounding.*;

/**
 * Gives an object a collider for spatial interaction. Note that a collider doesn't necessarily need to be used for collision. A collider component can be used for any spatial interaction.
 * <p>
 * For example, a checkpoint can use a ComponentCollider to detect when the player has reached it.
 */
public class ComponentCollider extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private AABB aabb;
	private QuickHull hull;
	private boolean renderAABB;

	/**
	 * Creates a new ComponentCollider.
	 *
	 * @param entity The entity this component is attached to.
	 */
	public ComponentCollider(Entity entity) {
		super(entity, ID);
		this.aabb = new AABB();
		this.hull = new QuickHull();
		this.renderAABB = true;
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
		this.renderAABB = true;
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

	/**
	 * Gets if the AABB should be rendered.
	 *
	 * @return If the AABB should be rendered.
	 */
	public boolean renderAABB() {
		return renderAABB;
	}

	/**
	 * Sets if the AABB should be rendered.
	 *
	 * @param renderAABB If the AABB should be rendered.
	 */
	public void setRenderAABB(boolean renderAABB) {
		this.renderAABB = renderAABB;
	}

	@Override
	public void update() {
		if (super.getEntity().hasMoved()) {
			ComponentModel componentModel = (ComponentModel) getEntity().getComponent(ComponentModel.ID);

			if (componentModel != null && componentModel.getModel() != null && componentModel.getModel().getMeshData() != null && componentModel.getModel().getMeshData().getAABB() != null) {
				AABB.recalculate(componentModel.getModel().getMeshData().getAABB(), super.getEntity().getPosition(), super.getEntity().getRotation(), componentModel.getScale(), aabb);
				QuickHull.recalculate(componentModel.getModel().getMeshData().getHull(), hull, super.getEntity().getPosition(), super.getEntity().getRotation(), componentModel.getScale());
			}

			// TODO: Calculate the AABBs and Hulls from animated models.
			/*ComponentAnimation componentAnimation = (ComponentAnimation) getEntity().getComponent(ComponentAnimation.ID);

			if (componentAnimation != null && componentAnimation.getModel() != null && componentAnimation.getModel().getAABB() != null) {
				AABB.recalculate(componentAnimation.getModel().getAABB(), super.getEntity().getPosition(), super.getEntity().getRotation(), componentAnimation.getScale(), aabb);
				QuickHull.recalculate(componentAnimation.getModel().getHull(), hull, super.getEntity().getPosition(), super.getEntity().getRotation(), componentAnimation.getScale());
			}*/
		}

		if (renderAABB) {
			FlounderBounding.addShapeRender(aabb);
		}
	}

	@Override
	public void dispose() {
	}
}
