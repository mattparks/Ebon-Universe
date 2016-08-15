package game.entities.loading;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.space.*;
import game.entities.*;
import game.entities.components.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Class that represents loaded .entity data.
 */
public class EntityTemplate {
	public static final MyFile ENTITIES_FOLDER = new MyFile(MyFile.RES_FOLDER, "entities");

	private String entityName;
	private Map<EntityLoader.IndividualData, List<EntityLoader.SectionData>> componentsData;

	/**
	 * Creates a new template from loaded data.
	 *
	 * @param entityName The name of the loaded entity.
	 * @param componentsData A HashMap of loaded component data to be parsed when attaching the component to the new entity.
	 */
	public EntityTemplate(String entityName, Map<EntityLoader.IndividualData, List<EntityLoader.SectionData>> componentsData) {
		this.entityName = entityName;
		this.componentsData = componentsData;
	}

	/**
	 * Creates a new entity from the template.
	 *
	 * @param structure The structure to place the entity into.
	 * @param position Initial world position.
	 * @param rotation Initial rotation.
	 *
	 * @return Returns a new entity created from the template.
	 */
	public Entity createEntity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		Entity instance = new Entity(structure, position, rotation);

		for (EntityLoader.IndividualData k : componentsData.keySet()) {
			try {
				Class componentClass = Class.forName(k.classpath);
				Class[] componentTypes = new Class[]{instance.getClass(), this.getClass()};
				@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
				Object[] componentParameters = new Object[]{instance, this};
				componentConstructor.newInstance(componentParameters);
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
				FlounderEngine.getLogger().error("While loading " + entityName + "'s components " + k.classpath + " constructor could not be found!");
				FlounderEngine.getLogger().exception(e);
			}
		}

		return instance;
	}

	/**
	 * Gets the individual data from the requested variable.
	 *
	 * @param component The component to get data for.
	 * @param key The key to get data, the nave for the individual variable.
	 *
	 * @return Returns string of parsable data.
	 */
	public String getValue(IEntityComponent component, String key) {
		for (EntityLoader.IndividualData data : componentsData.keySet()) {
			if (data.classpath.equals(component.getClass().getName())) {
				for (Pair<String, String> pair : data.individualData) {
					if (pair.getFirst().equals(key)) {
						return pair.getSecond();
					}
				}
			}
		}

		return null;
	}

	/**
	 * Gets data from a entity component section.
	 *
	 * @param component The component to get data for.
	 * @param sectionName The sections name.
	 *
	 * @return The sections data.
	 */
	public String getSectionData(IEntityComponent component, String sectionName) {
		for (EntityLoader.IndividualData data : componentsData.keySet()) {
			if (data.classpath.equals(component.getClass().getName())) {
				for (EntityLoader.SectionData section : componentsData.get(data)) {
					if (section.name.equals(sectionName)) {
						return section.line.trim();
					}
				}
			}
		}

		return null;
	}

	/**
	 * Gets the entity templates name.
	 *
	 * @return The entity templates name.
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Turns a segmented string into a string array.
	 *
	 * @param data The segmented data to parse.
	 *
	 * @return The resulting string array.
	 */
	public static String[] toStringArray(String data) {
		return data.split(",");
	}

	/**
	 * Turns a segmented string into a float array.
	 *
	 * @param data The segmented data to parse.
	 *
	 * @return The resulting float array.
	 */
	public static float[] toFloatArray(String data) {
		String[] values = data.split(",");
		float[] result = new float[values.length];

		for (int i = 0; i < values.length; i++) {
			result[i] = Float.parseFloat(values[i]);
		}

		return result;
	}

	/**
	 * Turns a segmented string into a integer array.
	 *
	 * @param data The segmented data to parse.
	 *
	 * @return The resulting integer array.
	 */
	public static int[] toIntArray(String data) {
		String[] values = data.split(",");
		int[] result = new int[values.length];

		for (int i = 0; i < values.length; i++) {
			result[i] = Integer.parseInt(values[i]);
		}

		return result;
	}
}
