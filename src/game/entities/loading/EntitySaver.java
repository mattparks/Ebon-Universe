package game.entities.loading;

import flounder.engine.*;
import flounder.helpers.*;
import game.entities.*;

import java.io.*;
import java.util.*;

/**
 * Class capable of saving engine.entities to a .entity file.
 */
public class EntitySaver {
	/**
	 * Saves the entity components to a .entity file.
	 *
	 * @param entity The entity to save.
	 * @param name The nave for the entity and file.
	 */
	public static void save(Entity entity, String name) {
		try {
			// Creates the save folder
			File saveFolder = new File("entities");

			if (!saveFolder.exists()) {
				saveFolder.mkdir();
			}

			// The save file and the writers.
			File saveFile = new File(saveFolder.getPath() + "/" + name + ".entity");
			saveFile.createNewFile();
			FileWriter fileWriter = new FileWriter(saveFile);
			EntityFileWriter entityFileWriter = new EntityFileWriter(fileWriter);

			// Date and save info.
			String savedDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "." + Calendar.getInstance().get(Calendar.YEAR) + " - " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE);
			FlounderEngine.getLogger().log("Entity " + name + " is being saved at: " + savedDate);
			entityFileWriter.addComment("Date Generated: " + savedDate, "Created By: " + System.getProperty("user.classpath"));

			// Entity General Data.
			entityFileWriter.beginNewSegment("EntityData");
			{
				entityFileWriter.writeSegmentData("Name: " + name + ";", true);
			}
			entityFileWriter.endSegment(false);

			// Components.
			entityFileWriter.beginNewSegment("Components");
			{
				for (int i = 0; i < entity.getComponents().size(); i++) {
					entityFileWriter.beginNewSegment(entity.getComponents().get(i).getClass().getName());

					Pair<String[], EntitySaverFunction[]> saveableValues = entity.getComponents().get(i).getSavableValues();

					// Individual data components.
					for (String s : saveableValues.getFirst()) {
						entityFileWriter.writeSegmentData(s + ";", true);
					}

					// Blank area between both sections.
					if (saveableValues.getSecond().length > 0) {
						entityFileWriter.enterBlankLine();
						entityFileWriter.enterBlankLine();
					}

					// Segmented data components.
					int fi = 0;

					for (EntitySaverFunction f : saveableValues.getSecond()) {
						entityFileWriter.beginNewSegment(f.getSectionName());
						{
							f.writeIntoSection(entityFileWriter);
						}
						entityFileWriter.endSegment(fi++ == saveableValues.getSecond().length - 1);
					}

					entityFileWriter.endSegment(i == entity.getComponents().size() - 1);
				}
			}
			entityFileWriter.endSegment(false);

			// Closes the file for writing.
			fileWriter.close();
		} catch (IOException e) {
			FlounderEngine.getLogger().error("File saver for entity " + name + " did not execute successfully!");
			FlounderEngine.getLogger().exception(e);
		}
	}
}
