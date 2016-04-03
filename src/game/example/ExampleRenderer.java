package game.example;

import flounder.engine.*;
import flounder.loaders.*;
import flounder.maths.vectors.*;
import org.lwjgl.opengl.*;

public class ExampleRenderer extends IRenderer {
	private final ExampleShader shader;
	private final int vao;
	private final int vertexCount;

	public ExampleRenderer() {
		// OpenGL expects vertices to be defined counter clockwise by default
		float[] vertices = {
				-0.5f, 0.5f, 0f, // V0
				-0.5f, -0.5f, 0f, // V1
				0.5f, -0.5f, 0f, // V2
				0.5f, 0.5f, 0f, // V3
		};

		int[] indices = {
				0, 1, 3, // Top left triangle (V0, V1, V3)
				3, 1, 2 // Bottom right triangle (V3, V1, V1)
		};

		vao = Loader.createVAO();
		Loader.createIndicesVBO(vao, indices);
		Loader.storeDataInVBO(vao, vertices, 0, 3);
		GL30.glBindVertexArray(0);
		vertexCount = vertices.length;

		shader = new ExampleShader();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		shader.start();
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
