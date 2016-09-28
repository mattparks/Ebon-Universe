package ebon.entities;

import flounder.engine.*;
import flounder.logger.*;
import flounder.models.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import flounder.shaders.*;
import flounder.space.*;
import flounder.textures.*;

import java.util.*;

/**
 * A class that manages game entities.
 */
public class EbonEntities extends IModule {
	private static final EbonEntities instance = new EbonEntities();

	private StructureBasic<Entity> entityStructure;

	/**
	 * Creates a new game manager for entities.
	 */
	public EbonEntities() {
		super(ModuleUpdate.AFTER_ENTRANCE, FlounderLogger.class, FlounderProfiler.class, FlounderModels.class, FlounderBounding.class, FlounderShaders.class, FlounderTextures.class);
	}

	@Override
	public void init() {
		entityStructure = new StructureBasic<>();
	}

	@Override
	public void run() {
		if (entityStructure != null) {
			entityStructure.getAll(new ArrayList<>()).forEach(Entity::update);
		}
	}

	@Override
	public void profile() {
		FlounderProfiler.add("Entities", "Count", entityStructure.getSize());
	}

	/**
	 * Clears the world of all entities.
	 */
	public static void clear() {
		instance.entityStructure.getAll(new ArrayList<>()).forEach(Entity::forceRemove);
	}

	/**
	 * Gets a list of entities.
	 *
	 * @return A list of entities.
	 */
	public static StructureBasic<Entity> getEntities() {
		return instance.entityStructure;
	}

	@Override
	public IModule getInstance() {
		return instance;
	}

	@Override
	public void dispose() {
		if (entityStructure != null) {
			entityStructure.clear();
			entityStructure = null;
		}
	}
}
