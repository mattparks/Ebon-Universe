package game.entities.components;

import flounder.physics.*;
import game.entities.*;

/**
 * Gives an object a collider for spatial interaction. Note that a collider doesn't necessarily need to be used for collision. A collider component can be used for any spatial interaction.
 * <p>
 * For example, a checkpoint can use a ColliderComponent to detect when the player has reached it.
 */
public class ColliderComponent extends IEntityComponent {
	public static final int ID = EntityAssignerID.getId();

	// private AABBMesh aabbMesh;
	private AABB aabb;

	/**
	 * Creates a new ColliderComponent.
	 *
	 * @param entity The entity this component is attached to.
	 */
	public ColliderComponent(final Entity entity) {
		super(entity, ID);
		// aabbMesh = null;
		aabb = null;
	}

	/**
	 * Ensures the bounds of this entity are at least big enough to contain {@code newAABB}.
	 *
	 * @param newAABB The AABB this entity must be able to contain.
	 */
	public void fitAABB(final AABB newAABB) { // AABBMesh newAABBMesh, // 	 * @param newAABBMesh The AABB Mesh that surrounds this entity's model.
		//if (aabbMesh == null) {
		//	aabbMesh = newAABBMesh;
		//}
		// else {
		// aabbMesh = aabb.combine(newAABBMesh); // TODO
		//}

		if (aabb == null) {
			aabb = newAABB;
		} else {
			aabb = aabb.combine(newAABB);
		}
	}

	/**
	 * @return Returns a AABB Mesh translated with the entity.
	 */
	//public AABBMesh getAABBMesh() {
	//	return aabbMesh; // .move(super.createEntity().getPosition(), super.createEntity().getRotation())
	//}

	/**
	 * @return Returns a AABB representing the basic collision range.
	 */
	public AABB getAABB() {
		return aabb.recalculate(super.getEntity().getPosition(), super.getEntity().getRotation());
	}

	@Override
	public void update() {
	}
}
