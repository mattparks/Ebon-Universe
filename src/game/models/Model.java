package game.models;

import flounder.loaders.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import org.lwjgl.opengl.*;

import java.util.*;

public class Model {
	private final float[] vertices;
	private final float[] textureCoords;
	private final float[] normals;
	private final float[] tangents;
	private final float[] colours;
	private final int[] indices;

	private final int vaoID;
	private final int vaoLength;
	private final AABB aabb;
	// private AABBMesh aabbMesh;

	public Model(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices, int dimensions) {
		this(vertices, textureCoords, normals, tangents, null, indices, dimensions);
	}

	public Model(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, float[] colours, int[] indices, int dimensions) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.tangents = tangents;
		this.colours = colours;
		this.indices = indices;
		this.vaoID = createVAO();
		this.vaoLength = (indices != null) ? indices.length : vertices.length / dimensions;
		this.aabb = createAABB();
		// this.aabbMesh = null;
	}

	private int createVAO() {
		int vaoID = Loader.createVAO();
		Loader.createIndicesVBO(vaoID, indices);
		Loader.storeDataInVBO(vaoID, vertices, 0, 3);
		Loader.storeDataInVBO(vaoID, textureCoords, 1, 2);
		Loader.storeDataInVBO(vaoID, normals, 2, 3);
		Loader.storeDataInVBO(vaoID, tangents, 3, 3);
		Loader.storeDataInVBO(vaoID, colours, 4, 4);
		GL30.glBindVertexArray(0);
		return vaoID;
	}

	private AABB createAABB() {
		float minX = 0, minY = 0, minZ = 0;
		float maxX = 0, maxY = 0, maxZ = 0;
		int tripleCount = 0;

		for (float position : vertices) {
			if (tripleCount == 0 && position < minX) {
				minX = position;
			} else if (tripleCount == 0 && position > maxX) {
				maxX = position;
			}

			if (tripleCount == 1 && position < minY) {
				minY = position;
			} else if (tripleCount == 1 && position > maxY) {
				maxY = position;
			}

			if (tripleCount == 2 && position < minZ) {
				minZ = position;
			} else if (tripleCount == 2 && position > maxZ) {
				maxZ = position;
			}

			if (tripleCount >= 2) {
				tripleCount = 0;
			} else {
				tripleCount++;
			}
		}

		return new AABB(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ));
	}

	public void delete() {
		Loader.deleteVAOFromCache(vaoID);
	}

	public List<Vector3f> getVerticesList() {
		return getVec3List(vertices);
	}

	public List<Vector3f> getVec3List(float[] input) {
		List<Vector3f> list = new ArrayList<>();

		int tripleCount = 0;
		Vector3f c = new Vector3f();

		for (float vertex : input) {
			if (tripleCount == 0) {
				c.x = vertex;
			} else if (tripleCount == 1) {
				c.y = vertex;
			} else if (tripleCount == 2) {
				c.z = vertex;
			}

			if (tripleCount >= 2) {
				tripleCount = 0;
				list.add(c);
				c = new Vector3f();
			} else {
				tripleCount++;
			}
		}

		return list;
	}

	public List<Vector2f> getTexturesList() {
		return getVec2List(textureCoords);
	}

	public List<Vector2f> getVec2List(float[] input) {
		List<Vector2f> list = new ArrayList<>();

		int doubleCount = 0;
		Vector2f c = new Vector2f();

		for (float vertex : input) {
			if (doubleCount == 0) {
				c.x = vertex;
			} else if (doubleCount == 1) {
				c.y = vertex;
			}

			if (doubleCount >= 1) {
				doubleCount = 0;
				list.add(c);
				c = new Vector2f();
			} else {
				doubleCount++;
			}
		}

		return list;
	}

	public List<Vector3f> getNormalsList() {
		return getVec3List(normals);
	}

	public List<Vector3f> getTangentsList() {
		return getVec3List(tangents);
	}

	public int[] getIndicesList() {
		return indices;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVAOLength() {
		return vaoLength;
	}

	public AABB getAABB() {
		return aabb;
	}

	//public AABBMesh getAABBMesh() {
	//	return aabbMesh;
	//}

	//public void setAABBMesh(AABBMesh aabbMesh) {
	//	this.aabbMesh = aabbMesh;
	//}
}
