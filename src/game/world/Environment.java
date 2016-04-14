package game.world;

import flounder.engine.*;
import flounder.space.*;
import game.entities.*;
import game.entities.components.*;
import game.lights.*;

import java.util.*;

public class Environment {
	public static final float GRAVITY = -50;

	private static Fog fog;
	private static ISpatialStructure<Entity> entities;

	/**
	 * Initializes the start game environment.
	 *
	 * @param fog The fog to be used in the world.
	 */
	public static void init(final Fog fog) {
		Environment.fog = fog;
		Environment.entities = new StructureBasic<>();
	}

	public static Fog getFog() {
		return fog;
	}

	public static ISpatialStructure<Entity> getStructure() {
		return entities;
	}

	/**
	 * Updates all engine.entities in the viewable terrain range.
	 */
	public static void updateEntities() {
		entities.getAll().forEach(Entity::update);
	}

	/**
	 * Creates a list of renderable engine.entities.
	 *
	 * @return Returns a list of sorted Textured Models and entities.
	 */
	public static List<Entity> getEntityObjects() {
		final List<Entity> result = new ArrayList<>();

		for (Entity e : entities.queryInFrustum(FlounderEngine.getCamera().getViewFrustum())) {
			final ModelComponent mc = (ModelComponent) e.getComponent(ModelComponent.ID);

			if (mc != null) {
				result.add(e);
			}
		}

		return result;
	}

	/**
	 * Creates a list of viewable engine.lights.
	 *
	 * @param camera The games camera to query engine.lights from.
	 *
	 * @return Returns a list of engine.lights.
	 */
	public static List<Light> getLightObjects(ICamera camera) {
		final List<Light> result = new ArrayList<>();

		for (Entity e : entities.getAll()) {
			final LightComponent lc = (LightComponent) e.getComponent(LightComponent.ID);

			if (lc != null) {
				result.add(lc.getLight());
			}
		}

		return result;
	}
}
