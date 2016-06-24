package game.models;

import flounder.engine.*;

import static org.lwjgl.opengl.GL30.*;

public class Model {
	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;

	private int vaoID;
	private int vaoLength;

	public Model(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;

		// Loads into the VAO.
		this.vaoID = FlounderEngine.getLoader().createVAO();
		FlounderEngine.getLoader().createIndicesVBO(vaoID, indices);
		FlounderEngine.getLoader().storeDataInVBO(vaoID, vertices, 0, 3);
		FlounderEngine.getLoader().storeDataInVBO(vaoID, textureCoords, 1, 2);
		FlounderEngine.getLoader().storeDataInVBO(vaoID, normals, 2, 3);
		glBindVertexArray(0);
		this.vaoLength = indices.length;
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

	public int[] getIndices() {
		return indices;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVaoLength() {
		return vaoLength;
	}
}
