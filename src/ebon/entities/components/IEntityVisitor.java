package ebon.entities.components;

import ebon.entities.*;

/**
 * Defines a function to be called when visiting engine.entities.
 */
public interface IEntityVisitor {
	/**
	 * A function that will be called every time an entity is visited.
	 *
	 * @param entity The entity being visited.
	 * @param component The component of the entity being visited, if relevant, or null otherwise.
	 */
	void visit(Entity entity, IEntityComponent component);
}
