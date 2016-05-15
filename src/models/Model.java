package models;

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

	public int[] getIndices() {
		return indices;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextures() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}
}
