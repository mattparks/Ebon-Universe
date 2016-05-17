package game.models;

import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.resources.*;

import java.io.*;
import java.lang.ref.*;
import java.util.*;

/**
 * Class capable of loading OBJ files into Models.
 */
public class LoaderOBJ {
	private static final Map<String, SoftReference<Model>> loaded = new HashMap<>();

	/**
	 * Loads a OBJ file into a ModelRaw object.
	 *
	 * @param file The file to be loaded.
	 *
	 * @return Returns a loaded ModelRaw.
	 */
	public static Model loadOBJ(final MyFile file) {
		final SoftReference<Model> ref = loaded.get(file.getPath());
		Model data = ref == null ? null : ref.get();

		if (data == null) {
			loaded.remove(file.getPath());
			BufferedReader reader;

			try {
				reader = file.getReader();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			final ModelData model = new ModelData(file);
			String line;

			try {
				while ((line = reader.readLine()) != null) {
					final String prefix = line.split(" ")[0];

					if (prefix.equals("#")) {
						continue;
					}

					switch (prefix) {
						case "v":
							final String[] currentLineV = line.split(" ");
							final Vector3f vertex = new Vector3f(Float.valueOf(currentLineV[1]), Float.valueOf(currentLineV[2]), Float.valueOf(currentLineV[3]));
							final DataVertex newDataVertex = new DataVertex(model.vertices.size(), vertex);
							model.vertices.add(newDataVertex);
							break;
						case "vt":
							final String[] currentLineVT = line.split(" ");
							final Vector2f texture = new Vector2f(Float.valueOf(currentLineVT[1]), Float.valueOf(currentLineVT[2]));
							model.textures.add(texture);
							break;
						case "vn":
							final String[] currentLineVN = line.split(" ");
							final Vector3f normal = new Vector3f(Float.valueOf(currentLineVN[1]), Float.valueOf(currentLineVN[2]), Float.valueOf(currentLineVN[3]));
							model.normals.add(normal);
							break;
						case "s":
							model.enableSmoothShading = !line.contains("off");
							break;
						case "f":
							final String[] currentLineF = line.split(" ");
							final String[] vertex1 = currentLineF[1].split("/");
							final String[] vertex2 = currentLineF[2].split("/");
							final String[] vertex3 = currentLineF[3].split("/");
							final DataVertex v0 = processDataVertex(vertex1, model.vertices, model.indices);
							final DataVertex v1 = processDataVertex(vertex2, model.vertices, model.indices);
							final DataVertex v2 = processDataVertex(vertex3, model.vertices, model.indices);
							break;
						default:
							System.err.println("[OBJ " + file.getName() + "] Unknown Line: " + line);
							break;
					}
				}

				data = model.createRaw();
				model.destroy();
				reader.close();
			} catch (IOException e) {
				FlounderLogger.error("Error reading the OBJ " + file);
			}

			loaded.put(file.getPath(), new SoftReference<>(data));
		}

		return data;
	}

	private static DataVertex processDataVertex(final String[] vertex, final List<DataVertex> vertices, final List<Integer> indices) {
		final int index = Integer.parseInt(vertex[0]) - 1;
		final DataVertex currentDataVertex = vertices.get(index);
		final int textureIndex = Integer.parseInt(vertex[1]) - 1;
		final int normalIndex = Integer.parseInt(vertex[2]) - 1;

		if (!currentDataVertex.isSet()) {
			currentDataVertex.setTextureIndex(textureIndex);
			currentDataVertex.setNormalIndex(normalIndex);
			indices.add(index);
			return currentDataVertex;
		} else {
			return dealWithAlreadyProcessedDataVertex(currentDataVertex, textureIndex, normalIndex, indices, vertices);
		}
	}

	private static DataVertex dealWithAlreadyProcessedDataVertex(final DataVertex previousDataVertex, final int newTextureIndex, final int newNormalIndex, final List<Integer> indices, final List<DataVertex> vertices) {
		if (previousDataVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousDataVertex.getIndex());
			return previousDataVertex;
		} else {
			final DataVertex anotherDataVertex = previousDataVertex.getDuplicateVertex();

			if (anotherDataVertex != null) {
				return dealWithAlreadyProcessedDataVertex(anotherDataVertex, newTextureIndex, newNormalIndex, indices, vertices);
			} else {
				final DataVertex duplicateDataVertex = new DataVertex(vertices.size(), previousDataVertex.getPosition());
				duplicateDataVertex.setTextureIndex(newTextureIndex);
				duplicateDataVertex.setNormalIndex(newNormalIndex);
				previousDataVertex.setDuplicateVertex(duplicateDataVertex);
				vertices.add(duplicateDataVertex);
				indices.add(duplicateDataVertex.getIndex());
				return duplicateDataVertex;
			}
		}
	}

	public static class ModelData {
		public MyFile file;
		public List<DataVertex> vertices = new ArrayList<>();
		public List<Vector2f> textures = new ArrayList<>();
		public List<Vector3f> normals = new ArrayList<>();
		public List<Integer> indices = new ArrayList<>();
		public boolean enableSmoothShading = true;

		public ModelData(MyFile file) {
			this.file = file;
		}

		public Model createRaw() {
			for (final DataVertex vertex : vertices) {
				if (!vertex.isSet()) {
					vertex.setTextureIndex(0);
					vertex.setNormalIndex(0);
				}
			}

			final float[] verticesArray = new float[vertices.size() * 3];
			final float[] texturesArray = new float[vertices.size() * 2];
			final float[] normalsArray = new float[vertices.size() * 3];

			for (int i = 0; i < vertices.size(); i++) {
				DataVertex currentDataVertex = vertices.get(i);
				Vector3f position = currentDataVertex.getPosition();
				Vector2f textureCoord = textures.get(currentDataVertex.getTextureIndex());
				Vector3f normalVector = normals.get(currentDataVertex.getNormalIndex());

				verticesArray[i * 3] = position.x;
				verticesArray[i * 3 + 1] = position.y;
				verticesArray[i * 3 + 2] = position.z;

				texturesArray[i * 2] = textureCoord.x;
				texturesArray[i * 2 + 1] = 1 - textureCoord.y;

				normalsArray[i * 3] = normalVector.x;
				normalsArray[i * 3 + 1] = normalVector.y;
				normalsArray[i * 3 + 2] = normalVector.z;
			}

			int[] indicesArray = new int[indices.size()];

			for (int i = 0; i < indicesArray.length; i++) {
				indicesArray[i] = indices.get(i);
			}

			System.out.println(file + " is being loaded into model memory!");
			return new Model(verticesArray, texturesArray, normalsArray, indicesArray);
		}

		public void destroy() {
			vertices = null;
			textures = null;
			normals = null;
			indices = null;
		}
	}

	public static class DataVertex {
		public static final int NO_INDEX = -1;

		private final Vector3f position;
		private int textureIndex;
		private int normalIndex;
		private int index;
		private float length;
		private DataVertex duplicateDataVertex;

		public DataVertex(final int index, final Vector3f position) {
			this.index = index;
			this.position = position;
			textureIndex = NO_INDEX;
			normalIndex = NO_INDEX;
			length = position.length();
			duplicateDataVertex = null;
		}

		public int getIndex() {
			return index;
		}

		public float getLength() {
			return length;
		}

		public boolean isSet() {
			return (textureIndex != NO_INDEX) && (normalIndex != NO_INDEX);
		}

		public boolean hasSameTextureAndNormal(final int textureIndexOther, final int normalIndexOther) {
			return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
		}

		public Vector3f getPosition() {
			return position;
		}

		public int getTextureIndex() {
			return textureIndex;
		}

		public void setTextureIndex(final int textureIndex) {
			this.textureIndex = textureIndex;
		}

		public int getNormalIndex() {
			return normalIndex;
		}

		public void setNormalIndex(final int normalIndex) {
			this.normalIndex = normalIndex;
		}

		public DataVertex getDuplicateVertex() {
			return duplicateDataVertex;
		}

		public void setDuplicateVertex(final DataVertex duplicateDataVertex) {
			this.duplicateDataVertex = duplicateDataVertex;
		}
	}
}
