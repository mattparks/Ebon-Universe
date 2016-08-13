package game.entities.loading;

import flounder.engine.*;
import flounder.helpers.*;

import java.io.*;
import java.lang.ref.*;
import java.util.*;

/**
 * Class capable of loading entities from a .entity file.
 */
public class EntityLoader {
	private static Map<String, SoftReference<EntityTemplate>> loadedTemplates = new HashMap<>();

	public static EntityTemplate load(String name) {
		SoftReference<EntityTemplate> ref = loadedTemplates.get(name);
		EntityTemplate data = ref == null ? null : ref.get();

		if (data == null) {
			FlounderEngine.getLogger().log(name + " is being loaded into a entity template right now!");
			loadedTemplates.remove(name);

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
				Map<IndividualData, List<SectionData>> componentsData = new HashMap<>();

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

						List<Pair<String, String>> individualData = new ArrayList<>();
						List<String> sectionLines = new ArrayList<>();
						List<SectionData> sections = new ArrayList<>();

						while (fileNestation > 0) {
							line = fileReader.readLine();

							if (line == null) {
								individualData.clear();
								sectionLines.clear();
								sections.clear();
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
									componentsData.put(new IndividualData(componentClasspaths, new ArrayList<>(individualData)), sections);
									individualData.clear();
									componentClasspaths = null;
								} else if (componentSubsection != null) {
									sections.add(new SectionData(componentSubsection, new ArrayList<>(sectionLines)));
									sectionLines.clear();
									componentSubsection = null;
								}
							} else if (!line.isEmpty()) {
								if (componentClasspaths != null && componentSubsection == null) {
									String[] lineKeys = line.replaceAll("\\s+", "").replace(";", "").trim().split(":");
									individualData.add(new Pair<>(lineKeys[0].trim(), lineKeys[1].trim()));
								} else if (componentSubsection != null) {
									sectionLines.add(line.replaceAll("\\s+", "").trim());
								}
							}
						}
					}
				}

				data = new EntityTemplate(entityName, componentsData);
			} catch (IOException e) {
				FlounderEngine.getLogger().error("File reader for entity " + saveFile.getPath() + " did not execute successfully!");
				FlounderEngine.getLogger().exception(e);
				return null;
			}

			loadedTemplates.put(name, new SoftReference<>(data));
		}

		return data;
	}

	/**
	 * A class that contains individual from a component.
	 */
	protected static class IndividualData {
		protected String classpath;
		protected List<Pair<String, String>> individualData;

		protected IndividualData(String name, List<Pair<String, String>> individualData) {
			this.classpath = name;
			this.individualData = individualData;
		}
	}

	/**
	 * A class that contains data from a section from a component.
	 */
	protected static class SectionData {
		protected String name;
		protected String line;

		protected SectionData(String name, List<String> sectionLines) {
			this.name = name;
			this.line = "";

			for (String s : sectionLines) {
				line += s.replaceAll("[\\t\\n\\r]", " ").trim();
			}
		}
	}
}
