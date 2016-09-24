package ebon.entities;

import ebon.entities.components.*;
import flounder.engine.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;

import java.util.*;

/**
 * A generic object in the game.
 */
public class Entity implements ISpatialObject {
	private ISpatialStructure<Entity> structure;
	private List<IEntityComponent> components;
	private Vector3f position;
	private Vector3f rotation;
	private Matrix4f modelMatrix;
	private boolean hasMoved;
	private boolean isRemoved;

	/**
	 * Creates a new Entity with minimum necessary construction.
	 *
	 * @param structure The spatial structure this entity will be contained in.
	 * @param position The location of the entity.
	 * @param rotation The rotation of the entity.
	 */
	public Entity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		this.structure = structure;
		components = new ArrayList<>();
		this.position = position;
		this.rotation = rotation;
		this.modelMatrix = new Matrix4f();
		this.hasMoved = true;
		this.isRemoved = false;
		this.structure.add(this);
	}

	/**
	 * Adds a new component to the entity.
	 *
	 * @param component The component to add.
	 */
	public void addComponent(IEntityComponent component) {
		components.add(component);
	}

	/**
	 * Removes a component to the entity.
	 *
	 * @param component The component to remove.
	 */
	public void removeComponent(IEntityComponent component) {
		component.dispose();
		components.remove(component);
	}

	/**
	 * Gets a list of all entity components.
	 *
	 * @return All entity components in this entity.
	 */
	public List<IEntityComponent> getComponents() {
		return components;
	}

	/**
	 * Removes a component from this entity by id. If more than one is found, the first component in the list is removed. If none are found, nothing is removed.
	 *
	 * @param id The id of the component. This is typically found with ComponentClass.ID.
	 */
	public void removeComponent(int id) {
		for (IEntityComponent c : components) {
			if (c.getId() == id) {
				c.dispose();
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
	public void visitInRange(int id, AABB range, IEntityVisitor visitor) {
		for (Entity entity : structure.queryInBounding(new ArrayList<>(), range)) {
			if (entity.isRemoved) {
				continue;
			}

			IEntityComponent component = id == -1 ? null : entity.getComponent(id);

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
	public IEntityComponent getComponent(int id) {
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
		try {
			components.forEach(IEntityComponent::update);
		} catch (ConcurrentModificationException e) {
			FlounderEngine.getLogger().exception(e);
		}

		hasMoved = false;
	}

	/**
	 * Moves this entity by a certain amount. If this entity is a colliding entity and it hits another colliding entity when it moves, then this will only move the entity as far as it can without intersecting a colliding entity. This function only works on one axis at a time; one of the parameters must be 0.
	 *
	 * @param move The amount to move.
	 * @param rotate The amount to rotate.
	 */
	public void move(Vector3f move, Vector3f rotate) {
		structure.remove(this);
		float moveAmountX = move.getX();
		float moveAmountY = move.getY();
		float moveAmountZ = move.getZ();

		float rotateAmountX = rotate.getX();
		float rotateAmountY = rotate.getY();
		float rotateAmountZ = rotate.getZ();

		ComponentCollision collision = (ComponentCollision) getComponent(ComponentCollision.ID);

		if (collision != null) {
			Vector3f amounts = collision.resolveAABBCollisions(new Vector3f(moveAmountX, moveAmountY, moveAmountZ));
			moveAmountX = amounts.getX();
			moveAmountY = amounts.getY();
			moveAmountZ = amounts.getZ();
		}

		hasMoved = true;

		position.set(position.getX() + moveAmountX, position.getY() + moveAmountY, position.getZ() + moveAmountZ);
		rotation.set(rotation.getX() + rotateAmountX, rotation.getY() + rotateAmountY, rotation.getZ() + rotateAmountZ);
		structure.add(this);
	}

	/**
	 * Gets the entitys model matrix.
	 *
	 * @return The entitys model matrix.
	 */
	public Matrix4f getModelMatrix() {
		modelMatrix.setIdentity();
		float scale = (getComponent(ComponentModel.ID) != null) ? ((ComponentModel) getComponent(ComponentModel.ID)).getScale() : 1.0f;
		Matrix4f.transformationMatrix(position, rotation, scale, modelMatrix);
		return modelMatrix;
	}

	/**
	 * Changes the structure this object is contained in.
	 *
	 * @param structure The new structure too be contained in.
	 */
	public void switchStructure(ISpatialStructure<Entity> structure) {
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

		isRemoved = true;
		ComponentRemove removeComponent = (ComponentRemove) getComponent(ComponentRemove.ID);

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

		for (IEntityComponent component : components) {
			component.dispose();
		}
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

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public boolean hasMoved() {
		return hasMoved;
	}

	@Override
	public AABB getBounding() {
		ComponentCollider ac = (ComponentCollider) getComponent(ComponentCollider.ID);

		if (ac != null) {
			return ac.getAABB();
		} else {
			return null;
		}
	}
}
