package game.entities;

import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;
import game.entities.components.*;

import java.util.*;

/**
 * A generic object in the game.
 */
public class Entity implements ISpatialObject {
	private final List<IEntityComponent> components;
	private final Vector3f position;
	private final Vector3f rotation;
	private final Matrix4f transformation;
	private ISpatialStructure<Entity> structure;
	private boolean isRemoved;

	/**
	 * Creates a new Entity with minimum necessary construction.
	 *
	 * @param structure The spatial structure this entity will be contained in.
	 * @param position The location of the entity.
	 * @param rotation The rotation of the entity.
	 */
	public Entity(final ISpatialStructure<Entity> structure, final Vector3f position, final Vector3f rotation) {
		this.structure = structure;
		this.components = new ArrayList<>();
		this.position = position;
		this.rotation = rotation;
		this.transformation = new Matrix4f();
		this.isRemoved = false;
		this.structure.add(this);
	}

	/**
	 * Adds a new component to the entity.
	 *
	 * @param component The component to add.
	 */
	public void addComponent(final IEntityComponent component) {
		components.add(component);
	}

	/**
	 * Removes a component to the entity.
	 *
	 * @param component The component to remove.
	 */
	public void removeComponent(final IEntityComponent component) {
		components.remove(component);
	}

	public List<IEntityComponent> getComponents() {
		return components;
	}

	/**
	 * Removes a component from this entity by id. If more than one is found, the first component in the list is removed. If none are found, nothing is removed.
	 *
	 * @param id The id of the component. This is typically found with ComponentClass.ID.
	 */
	public void removeComponent(final int id) {
		for (IEntityComponent c : components) {
			if (c.getId() == id) {
				components.remove(c);
				return;
			}
		}
	}

	/**
	 * Visits every entity with a particular component within a certain range of space.
	 *
	 * @param id The id of the component. This is typically found with ComponentClass.ID. If no particular component is desired, specify -1.
	 * @param range The range of space to be visited.
	 * @param visitor The visitor that will be executed for every entity visited.
	 */
	public void visitInRange(final int id, final AABB range, final IEntityVisitor visitor) {
		for (Entity entity : structure.queryInAABB(range)) {
			if (entity.isRemoved) {
				continue;
			}

			final IEntityComponent component = id == -1 ? null : entity.getComponent(id);

			if (component != null || id == -1) {
				visitor.visit(entity, component);
			}
		}
	}

	/**
	 * Finds and returns a component attached to this entity by id. If more than one is found, the first component in the list is returned. If none are found, returns null.
	 *
	 * @param id The id of the component. This is typically found with ComponentClass.ID.
	 *
	 * @return The first component found with the given id, or null if none are found.
	 */
	public IEntityComponent getComponent(final int id) {
		for (IEntityComponent component : components) {
			if (component.getId() == id) {
				return component;
			}
		}

		return null;
	}

	/**
	 * Updates all the components attached to this entity.
	 */
	public void update() {
		components.forEach(IEntityComponent::update);
	}

	/**
	 * Moves this entity by a certain amount. If this entity is a colliding entity and it hits another colliding entity when it moves, then this will only move the entity as far as it can without intersecting a colliding entity. This function only works on one axis at a time; one of the parameters must be 0.
	 *
	 * @param move The amount to move.
	 * @param rotate The amount to rotate.
	 */
	public void move(final Vector3f move, final Vector3f rotate) {
		structure.remove(this);
		float moveAmountX = move.getX();
		float moveAmountY = move.getY();
		float moveAmountZ = move.getZ();

		float rotateAmountX = rotate.getX();
		float rotateAmountY = rotate.getY();
		float rotateAmountZ = rotate.getZ();

		CollisionComponent collision = (CollisionComponent) getComponent(CollisionComponent.ID);

		if (collision != null) {
			final Vector3f amounts = collision.resolveAABBCollisions(new Vector3f(moveAmountX, moveAmountY, moveAmountZ));
			moveAmountX = amounts.getX();
			moveAmountY = amounts.getY();
			moveAmountZ = amounts.getZ();
		}

		position.set(position.getX() + moveAmountX, position.getY() + moveAmountY, position.getZ() + moveAmountZ);
		rotation.set(rotation.getX() + rotateAmountX, rotation.getY() + rotateAmountY, rotation.getZ() + rotateAmountZ);
		structure.add(this);
	}

	public Matrix4f getModelMatrix() {
		float scale = (getComponent(ModelComponent.ID) != null) ? ((ModelComponent) getComponent(ModelComponent.ID)).getScale() : 1.0f;
		return Matrix4f.transformationMatrix(position, rotation, scale, transformation);
	}

	/**
	 * Changes the structure this object is contained in.
	 *
	 * @param structure The new structure too be contained in.
	 */
	public void switchStructure(final ISpatialStructure<Entity> structure) {
		structure.remove(this);
		this.structure = structure;
		structure.add(this);
	}

	public ISpatialStructure<Entity> getStructure() {
		return structure;
	}

	/**
	 * Removes this entity from the spatial structure, and triggers any remove actions specified for this entity.
	 */
	public void remove() {
		if (isRemoved) {
			return;
		}

		// AudioComponent audioComponent = (AudioComponent) getComponent(AudioComponent.ID);
		// if (audioComponent != null) {
		// audioComponent.play("remove");
		// }

		isRemoved = true;
		final RemoveComponent removeComponent = (RemoveComponent) getComponent(RemoveComponent.ID);

		if (removeComponent != null) {
			removeComponent.activate();
		} else {
			forceRemove();
		}
	}

	/**
	 * Forcibly removes this entity from the spatial structure without triggering remove actions. Use with caution; this function may fail or cause errors if used inappropriately.
	 */
	public void forceRemove() {
		isRemoved = true;
		structure.remove(this);
	}

	/**
	 * Gets whether or not this entity has been removed from the spatial structure.
	 *
	 * @return Whether or not this entity has been removed from the spatial structure.
	 */
	public boolean isRemoved() {
		return isRemoved;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(final Vector3f position) {
		this.position.set(position);
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(final Vector3f rotation) {
		this.rotation.set(rotation);
	}

	@Override
	public AABB getAABB() {
		final ColliderComponent ac = (ColliderComponent) getComponent(ColliderComponent.ID);

		if (ac != null) {
			return ac.getAABB();
		} else {
			return null; // new AABB()
		}
	}
}
