package game.entities.loading;

import flounder.engine.*;
import flounder.maths.vectors.*;

import java.io.*;
import java.util.*;

/**
 * Class capable of loading engine.entities from a .entity file.
 */
public class EntityLoader {
	public static EntityTemplate load(String name) {
		// Creates the file reader.
		File saveFile = new File("entities/" + name + ".entity");

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(saveFile));

			if (fileReader == null) {
				FlounderEngine.getLogger().error("Error creating reader the entity file: " + saveFile);
				return null;
			}

			// Loaded data.
			String entityName = "unnamed";
			Map<ComponentData, List<SectionData>> componentsData = new HashMap<>();

			// Current line.
			String line;

			// Each line read loop.
			while ((line = fileReader.readLine()) != null) {
				// Entity General Data.
				if (line.contains("EntityData")) {
					while (!(line = fileReader.readLine()).contains("};")) {
						if (line.contains("Name")) {
							entityName = line.replaceAll("\\s+", "").replaceAll(";", "").substring("Name:".length());
						}
					}
				}

				// Components.
				if (line.contains("Components")) {
					int fileNestation = 1;

					String componentClasspaths = null;
					String componentSubsection = null;

					List<String> individualData = new ArrayList<>();
					List<String> sectionLines = new ArrayList<>();
					List<SectionData> sections = new ArrayList<>();

					while (fileNestation > 0) {
						line = fileReader.readLine();

						if (line == null) {
							break;
						}

						if (line.contains("{")) {
							if (componentClasspaths == null) {
								componentClasspaths = line.replaceAll("\\s+", "");
								componentClasspaths = componentClasspaths.substring(0, componentClasspaths.length() - 1);
							} else {
								componentSubsection = line.replaceAll("\\s+", "");
								componentSubsection = componentSubsection.substring(0, componentSubsection.length() - 1);
							}

							fileNestation++;
						} else if (line.contains("};")) {
							fileNestation--;

							if (fileNestation == 1) {
								componentsData.put(new ComponentData(componentClasspaths, new ArrayList<>(individualData)), sections);
								individualData.clear();
								componentClasspaths = null;
							} else if (componentSubsection != null) {
								sections.add(new SectionData(componentSubsection, new ArrayList<>(sectionLines)));
								sectionLines.clear();
								componentSubsection = null;
							}
						} else if (!line.isEmpty()) {
							if (componentClasspaths != null && componentSubsection == null) {
								individualData.add(line.replaceAll("\\s+", "").trim());
							} else if (componentSubsection != null) {
								sectionLines.add(line.replaceAll("\\s+", "").trim());
							}
						} else {
							// Empty line!
						}
					}
				}
			}

			return new EntityTemplate(entityName, componentsData);
		} catch (IOException e) {
			FlounderEngine.getLogger().error("File reader for entity " + saveFile.getPath() + " did not execute successfully!");
			FlounderEngine.getLogger().exception(e);
		}

		return null;
	}

	/**
	 * Converts an list of Vector 4's into a array of floats.
	 *
	 * @param input The vectors to be stored.
	 *
	 * @return The new array of floats.
	 */
	public static float[] toFloatArrayV4(List<Vector4f> input) {
		float[] list = new float[input.size() * 4];
		int index = 0;

		for (Vector4f v : input) {
			list[index] = v.getX();
			list[index + 1] = v.getY();
			list[index + 2] = v.getZ();
			list[index + 3] = v.getW();
		}

		return list;
	}

	/**
	 * Converts an list of Vector 3's into a array of floats.
	 *
	 * @param input The vectors to be stored.
	 *
	 * @return The new array of floats.
	 */
	public static float[] toFloatArrayV3(List<Vector3f> input) {
		float[] list = new float[input.size() * 3];
		int index = 0;

		for (Vector3f v : input) {
			list[index] = v.getX();
			list[index + 1] = v.getY();
			list[index + 2] = v.getZ();
		}

		return list;
	}

	/**
	 * Converts an list of Vector 2's into a array of floats.
	 *
	 * @param input The vectors to be stored.
	 *
	 * @return The new array of floats.
	 */
	public static float[] toFloatArrayV2(List<Vector2f> input) {
		float[] list = new float[input.size() * 2];
		int index = 0;

		for (Vector2f v : input) {
			list[index] = v.getX();
			list[index + 1] = v.getY();
		}

		return list;
	}

	/**
	 * Converts an list of Integers into a array of ints.
	 *
	 * @param input The Integers to be stored.
	 *
	 * @return The new array of ints.
	 */
	public static int[] toIntArrayV1(List<Integer> input) {
		int[] list = new int[input.size()];
		int index = 0;

		for (int i : input) {
			list[index] = i;
		}

		return list;
	}

	public static class ComponentData {
		public String classpath;
		public List<String> individualData;

		public ComponentData(String name, List<String> individualData) {
			this.classpath = name;
			this.individualData = individualData;
		}
	}

	public static class SectionData {
		public String name;
		public List<String> sectionLines;

		public SectionData(String name, List<String> sectionLines) {
			this.name = name;
			this.sectionLines = sectionLines;
		}
	}
}
