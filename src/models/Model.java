package models;

import flounder.maths.vectors.*;

import java.util.*;

public class Model {
	private final float[] vertices;
	private final float[] textureCoords;
	private final float[] normals;
	private final int[] indices;

	public Model(final float[] vertices, final float[] textureCoords, final float[] normals, final int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
	}

	public List<Vector3f> getVec3List(final float[] input) {
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

	public List<Vector2f> getVec2List(final float[] input) {
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

	public int[] getIndices() {
		return indices;
	}

	public float[] getVertices() {
		return vertices;
	}

	public List<Vector3f> getVerticesList() {
		return getVec3List(vertices);
	}

	public float[] getTextures() {
		return textureCoords;
	}

	public List<Vector2f> getTexturesList() {
		return getVec2List(textureCoords);
	}

	public float[] getNormals() {
		return normals;
	}

	public List<Vector3f> getNormalsList() {
		return getVec3List(normals);
	}
}
