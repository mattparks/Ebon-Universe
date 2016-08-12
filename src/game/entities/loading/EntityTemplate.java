package game.entities.loading;

import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.space.*;
import game.entities.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Class that represents loaded .entity data.
 */
public class EntityTemplate {
	public static final MyFile ENTITIES_FOLDER = new MyFile(MyFile.RES_FOLDER, "entities");

	private String entityName;
	private Map<EntityLoader.ComponentData, List<EntityLoader.SectionData>> componentsData;

	/**
	 * Creates a new template from loaded data.
	 *
	 * @param entityName The name of the loaded entity.
	 * @param componentsData A HashMap of loaded component data to be parsed when attaching the component to the new entity.
	 */
	public EntityTemplate(String entityName, Map<EntityLoader.ComponentData, List<EntityLoader.SectionData>> componentsData) {
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

		for (EntityLoader.ComponentData k : componentsData.keySet()) {
			if (k != null) {
				try {
					Class componentClass = Class.forName(k.classpath);
					Class[] componentTypes = new Class[]{instance.getClass(), this.getClass()};
					@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
					Object[] componentParameters = new Object[]{instance, this};
					componentConstructor.newInstance(componentParameters);
				} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
					System.out.println("While loading " + entityName + "'s components " + k.classpath + " constructor could not be found!");
					e.printStackTrace();
				}
			}
		}

		return instance;
	}

	/**
	 * Gets the parsable individual data from the requested key.
	 *
	 * @param key The key to get data, the nave for the individual variable.
	 *
	 * @return Returns string of parsable data.
	 */
	public String getValue(String classpath, String key) {
		for (EntityLoader.ComponentData data : componentsData.keySet()) {
			if (data.classpath.equals(classpath)) {
				for (String line : data.individualData) {
					String[] lineKeys = line.split(":");

					if (lineKeys[0].trim().equals(key)) {
						String lineValue = lineKeys[1].replace(";", "").trim();

						if (lineValue.equals("null")) {
							return null;
						} else {
							return lineValue;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Gets data from a entity component section.
	 *
	 * @param classpath The components class path.
	 * @param sectionName The sections name.
	 *
	 * @return The sections data.
	 */
	public List<String> getSectionData(String classpath, String sectionName) {
		for (EntityLoader.ComponentData data : componentsData.keySet()) {
			if (data.classpath.equals(classpath)) {
				for (EntityLoader.SectionData section : componentsData.get(data)) {
					if (section.name.equals(sectionName)) {
						return section.sectionLines;
					}
				}
			}
		}

		return null;
	}

	public String toOneLine(List<String> data) {
		String result = "";

		for (String s : data) {
			result += s.replaceAll("[\\t\\n\\r]", " ").trim();
		}

		return result;
	}

	public float[] toFloatArray(String data) {
		String[] values = data.replace("[", "").replace("]", "").replace("/", ",").split(",");
		float[] result = new float[values.length];

		for (int i = 0; i < values.length; i++) {
			result[i] = Float.parseFloat(values[i]);
		}

		return result;
	}

	public int[] toIntArray(String data) {
		String[] values = data.replace("[", "").replace("]", "").replace("/", ",").split(",");
		int[] result = new int[values.length];

		for (int i = 0; i < values.length; i++) {
			result[i] = Integer.parseInt(values[i]);
		}

		return result;
	}
}
