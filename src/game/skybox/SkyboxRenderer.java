package game.skybox;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.profiling.*;
import flounder.loaders.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import game.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class SkyboxRenderer extends IRenderer {
	private static final float SIZE = 300f;
	private static final float[] VERTICES = {-SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE};

	private final SkyboxShader shader;
	private final Matrix4f viewMatrix;
	private final Vector3f viewMatrixRotate;

	private final int vao;

	public SkyboxRenderer() {
		shader = new SkyboxShader();
		viewMatrix = new Matrix4f().setIdentity();
		viewMatrixRotate = new Vector3f(0.0f, 1.0f, 0.0f);

		vao = Loader.createVAO();
		Loader.storeDataInVBO(vao, VERTICES, 0, 3);
		glBindVertexArray(0);
	}

	@Override
	public void renderObjects(final Vector4f clipPlane, final ICamera camera) {
		prepareRendering(clipPlane, camera);
		glDrawArrays(GL_TRIANGLES, 0, VERTICES.length); // Renders the skybox.
		endRendering();

		FlounderProfiler.add("Skybox", "Render Time", super.getRenderTimeMs());
	}

	private void prepareRendering(final Vector4f clipPlane, final ICamera camera) {
		viewMatrix.set(camera.getViewMatrix());

		// Starts the shader and loads values. TODO: Clip plane.
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(updateViewMatrix());
		shader.lowerFogLimit.loadFloat(Environment.getFog().getSkyLowerLimit());
		shader.upperFogLimit.loadFloat(Environment.getFog().getSkyUpperLimit());
		shader.fogColour.loadVec3(Environment.getFog().getFogColour());

		// Binds the VAO, sets antialiasing and binds the cube map.
		OpenglUtils.bindVAO(vao, 0);
		OpenglUtils.antialias(FlounderDevices.getDisplay().isAntialiasing());
	}

	private Matrix4f updateViewMatrix() {
		// Gets rid of some view matrix components to keep the skybox at a distance from the camera.
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		float rotation = 0;
		return Matrix4f.rotate(viewMatrix, viewMatrixRotate, (float) Math.toRadians(rotation), viewMatrix);
	}

	private void endRendering() {
		// Unbinds the VAO and stops the shader.
		OpenglUtils.unbindVAO(0);
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
