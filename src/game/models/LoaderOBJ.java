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
	private static Map<String, SoftReference<Model>> loaded = new HashMap<>();

	/**
	 * Loads a OBJ file into a ModelRaw object.
	 *
	 * @param file The file to be loaded.
	 *
	 * @return Returns a loaded ModelRaw.
	 */
	public static Model loadOBJ(MyFile file) {
		SoftReference<Model> ref = loaded.get(file.getPath());
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

			ModelData model = new ModelData(file);
//			Material currentMaterial = new Material();
			String line;

			try {
				while ((line = reader.readLine()) != null) {
					String prefix = line.split(" ")[0];

					if (prefix.equals("#")) {
						continue;
					}

					switch (prefix) {
				/*		case "mtllib":
							model.materials.addAll(LoaderMTL.loadMTL(new MyFile(MyFile.RES_FOLDER, line.split(" ")[1])));
							break;
						case "usemtl":
							for (Material m : model.materials) {
								if (m.name.equals(line.split(" ")[1])) {
									currentMaterial = m;
								}
							}
							break;*/
						case "v":
							String[] currentLineV = line.split(" ");
							Vector3f vertex = new Vector3f(Float.valueOf(currentLineV[1]), Float.valueOf(currentLineV[2]), Float.valueOf(currentLineV[3]));
							DataVertex newDataVertex = new DataVertex(model.vertices.size(), vertex);
							model.vertices.add(newDataVertex);
							break;
						case "vt":
							String[] currentLineVT = line.split(" ");
							Vector2f texture = new Vector2f(Float.valueOf(currentLineVT[1]), Float.valueOf(currentLineVT[2]));
							model.textures.add(texture);
							break;
						case "vn":
							String[] currentLineVN = line.split(" ");
							Vector3f normal = new Vector3f(Float.valueOf(currentLineVN[1]), Float.valueOf(currentLineVN[2]), Float.valueOf(currentLineVN[3]));
							model.normals.add(normal);
							break;
						case "s":
							model.enableSmoothShading = !line.contains("off");
							break;
						case "f":
							String[] currentLineF = line.split(" ");
							String[] vertex1 = currentLineF[1].split("/");
							String[] vertex2 = currentLineF[2].split("/");
							String[] vertex3 = currentLineF[3].split("/");
							DataVertex v0 = processDataVertex(vertex1, model.vertices, model.indices); // , currentMaterial
							DataVertex v1 = processDataVertex(vertex2, model.vertices, model.indices); // , currentMaterial
							DataVertex v2 = processDataVertex(vertex3, model.vertices, model.indices); // , currentMaterial
							calculateTangents(v0, v1, v2, model.textures);
							// model.aabbs.add(new AABB(Maths.min(v0.getPosition(), Maths.min(v1.getPosition(), v2.getPosition())), Maths.max(v0.getPosition(), Maths.max(v1.getPosition(), v2.getPosition()))));
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

	private static DataVertex processDataVertex(String[] vertex, List<DataVertex> vertices, List<Integer> indices) { // , Material currentMaterial
		int index = Integer.parseInt(vertex[0]) - 1;
		DataVertex currentDataVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;

		if (!currentDataVertex.isSet()) {
			currentDataVertex.setTextureIndex(textureIndex);
			currentDataVertex.setNormalIndex(normalIndex);
			//		currentDataVertex.setMaterial(currentMaterial);
			indices.add(index);
			return currentDataVertex;
		} else {
			return dealWithAlreadyProcessedDataVertex(currentDataVertex, textureIndex, normalIndex, indices, vertices); //, currentMaterial
		}
	}

	private static DataVertex dealWithAlreadyProcessedDataVertex(DataVertex previousDataVertex, int newTextureIndex, int newNormalIndex, List<Integer> indices, List<DataVertex> vertices) { // , Material currentMaterial
		if (previousDataVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousDataVertex.getIndex());
			return previousDataVertex;
		} else {
			DataVertex anotherDataVertex = previousDataVertex.getDuplicateVertex();

			if (anotherDataVertex != null) {
				return dealWithAlreadyProcessedDataVertex(anotherDataVertex, newTextureIndex, newNormalIndex, indices, vertices); // , currentMaterial
			} else {
				DataVertex duplicateDataVertex = new DataVertex(vertices.size(), previousDataVertex.getPosition());
				duplicateDataVertex.setTextureIndex(newTextureIndex);
				duplicateDataVertex.setNormalIndex(newNormalIndex);
				//	duplicateDataVertex.setMaterial(currentMaterial);
				previousDataVertex.setDuplicateVertex(duplicateDataVertex);
				vertices.add(duplicateDataVertex);
				indices.add(duplicateDataVertex.getIndex());
				return duplicateDataVertex;
			}
		}
	}

	private static void calculateTangents(DataVertex v0, DataVertex v1, DataVertex v2, List<Vector2f> textures) {
		Vector3f deltaPos1 = Vector3f.subtract(v1.getPosition(), v0.getPosition(), null);
		Vector3f deltaPos2 = Vector3f.subtract(v2.getPosition(), v0.getPosition(), null);
		Vector2f uv0 = textures.get(v0.getTextureIndex());
		Vector2f uv1 = textures.get(v1.getTextureIndex());
		Vector2f uv2 = textures.get(v2.getTextureIndex());
		Vector2f deltaUv1 = Vector2f.subtract(uv1, uv0, null);
		Vector2f deltaUv2 = Vector2f.subtract(uv2, uv0, null);

		float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
		deltaPos1.scale(deltaUv2.y);
		deltaPos2.scale(deltaUv1.y);
		Vector3f tangent = Vector3f.subtract(deltaPos1, deltaPos2, null);
		tangent.scale(r);
		v0.addTangent(tangent);
		v1.addTangent(tangent);
		v2.addTangent(tangent);
	}

	public static class ModelData {
		public MyFile file;
		public List<DataVertex> vertices = new ArrayList<>();
		public List<Vector2f> textures = new ArrayList<>();
		public List<Vector3f> normals = new ArrayList<>();
		public List<Integer> indices = new ArrayList<>();
		//	public List<Material> materials = new ArrayList<>();
		// public List<AABB> aabbs = new ArrayList<>();
		public boolean enableSmoothShading = true;

		public ModelData(MyFile file) {
			this.file = file;
		}

		public Model createRaw() {
			for (DataVertex vertex : vertices) {
				vertex.averageTangents();

				if (!vertex.isSet()) {
					vertex.setTextureIndex(0);
					vertex.setNormalIndex(0);
				}
			}

			float[] verticesArray = new float[vertices.size() * 3];
			float[] texturesArray = new float[vertices.size() * 2];
			float[] normalsArray = new float[vertices.size() * 3];
			float[] tangentsArray = new float[vertices.size() * 3];
			// float[] mtlArray = new float[vertices.size() * 10];

			// List<String> verticeMaterials = new ArrayList<>();

			for (int i = 0; i < vertices.size(); i++) {
				DataVertex currentDataVertex = vertices.get(i);
				Vector3f position = currentDataVertex.getPosition();
				Vector2f textureCoord = textures.get(currentDataVertex.getTextureIndex());
				Vector3f normalVector = normals.get(currentDataVertex.getNormalIndex());
				Vector3f tangent = currentDataVertex.getAverageTangent();

				verticesArray[i * 3] = position.x;
				verticesArray[i * 3 + 1] = position.y;
				verticesArray[i * 3 + 2] = position.z;

				texturesArray[i * 2] = textureCoord.x;
				texturesArray[i * 2 + 1] = 1 - textureCoord.y;

				normalsArray[i * 3] = normalVector.x;
				normalsArray[i * 3 + 1] = normalVector.y;
				normalsArray[i * 3 + 2] = normalVector.z;

				tangentsArray[i * 3] = tangent.x;
				tangentsArray[i * 3 + 1] = tangent.y;
				tangentsArray[i * 3 + 2] = tangent.z;

				// verticeMaterials.add(currentDataVertex.getMaterial().name);

				// mtlArray[i * 10] = currentDataVertex.getMaterial().specularCoefficient;
				// mtlArray[i * 10 + 1] = currentDataVertex.getMaterial().ambientColour.getR();
				// mtlArray[i * 10 + 2] = currentDataVertex.getMaterial().ambientColour.getG();
				// mtlArray[i * 10 + 3] = currentDataVertex.getMaterial().ambientColour.getB();
				// mtlArray[i * 10 + 4] = currentDataVertex.getMaterial().diffuseColour.getR();
				// mtlArray[i * 10 + 5] = currentDataVertex.getMaterial().diffuseColour.getG();
				// mtlArray[i * 10 + 6] = currentDataVertex.getMaterial().diffuseColour.getB();
				// mtlArray[i * 10 + 7] = currentDataVertex.getMaterial().specularColour.getR();
				// mtlArray[i * 10 + 8] = currentDataVertex.getMaterial().specularColour.getG();
				// mtlArray[i * 10 + 9] = currentDataVertex.getMaterial().specularColour.getB();
			}

			int[] indicesArray = new int[indices.size()];

			for (int i = 0; i < indicesArray.length; i++) {
				indicesArray[i] = indices.get(i);
			}

			// AABB[] mesh = new AABB[aabbs.size()];

			// for (int i = 0; i < mesh.length; i++) {
			// 	mesh[i] = aabbs.get(i);
			// }

			System.out.println(file + " is being loaded into model memory!");

			Model data = new Model(verticesArray, texturesArray, normalsArray, tangentsArray, null, indicesArray, 3);
			// data.setAABBMesh(new AABBMesh(mesh));
			return data;
		}

		public void destroy() {
			vertices = null;
			textures = null;
			normals = null;
			indices = null;
			//	materials = null;
		}
	}

	public static class DataVertex {
		public static final int NO_INDEX = -1;

		private Vector3f position;
		//		private Material material;
		private int textureIndex;
		private int normalIndex;
		private int index;
		private float length;
		private List<Vector3f> tangents;
		private Vector3f averagedTangent;
		private DataVertex duplicateDataVertex;

		public DataVertex(int index, Vector3f position) {
			this.index = index;
			this.position = position;
			textureIndex = NO_INDEX;
			normalIndex = NO_INDEX;
			length = position.length();
			tangents = new ArrayList<>();
			averagedTangent = new Vector3f(0, 0, 0);
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

		public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
			return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
		}

		public Vector3f getPosition() {
			return position;
		}

		//	public Material getMaterial() {
		//		return material;
		//	}

		//	public void setMaterial(Material material) {
		//		this.material = material;
		//	}

		public int getTextureIndex() {
			return textureIndex;
		}

		public void setTextureIndex(int textureIndex) {
			this.textureIndex = textureIndex;
		}

		public int getNormalIndex() {
			return normalIndex;
		}

		public void setNormalIndex(int normalIndex) {
			this.normalIndex = normalIndex;
		}

		public void addTangent(Vector3f tangent) {
			tangents.add(tangent);
		}

		public void averageTangents() {
			if (tangents.isEmpty()) {
				return;
			}

			for (Vector3f tangent : tangents) {
				Vector3f.add(averagedTangent, tangent, averagedTangent);
			}

			averagedTangent.normalize();
		}

		public Vector3f getAverageTangent() {
			return averagedTangent;
		}

		public DataVertex getDuplicateVertex() {
			return duplicateDataVertex;
		}

		public void setDuplicateVertex(DataVertex duplicateDataVertex) {
			this.duplicateDataVertex = duplicateDataVertex;
		}
	}
}
