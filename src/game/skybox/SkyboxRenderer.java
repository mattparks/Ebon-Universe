package game.skybox;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import game.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class SkyboxRenderer extends IRenderer {
	public static final MyFile SKYBOXES_LOC = new MyFile(MyFile.RES_FOLDER, "skybox");

	private static final MyFile[] TEXTURE_FILES = {new MyFile(SKYBOXES_LOC, "nightRight.png"), new MyFile(SKYBOXES_LOC, "nightLeft.png"), new MyFile(SKYBOXES_LOC, "nightTop.png"), new MyFile(SKYBOXES_LOC, "nightBottom.png"), new MyFile(SKYBOXES_LOC, "nightBack.png"), new MyFile(SKYBOXES_LOC, "nightFront.png")};
	private static final int TEXTURE = FlounderEngine.getTextures().loadCubeMap(TEXTURE_FILES);

	private static final float SIZE = (FlounderEngine.getCamera().getFarPlane() - 25.0f) / (float) Math.sqrt(2.0);
	private static final float[] VERTICES = {-SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE};

	private SkyboxShader shader;
	private Matrix4f viewMatrix;
	private Vector3f viewMatrixRotate;

	private int vao;

	public SkyboxRenderer() {
		shader = new SkyboxShader();
		viewMatrix = new Matrix4f().setIdentity();
		viewMatrixRotate = new Vector3f(0.0f, 1.0f, 0.0f);

		vao = FlounderEngine.getLoader().createVAO();
		FlounderEngine.getLoader().storeDataInVBO(vao, VERTICES, 0, 3);
		glBindVertexArray(0);
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);
		glDrawArrays(GL_TRIANGLES, 0, VERTICES.length); // Renders the skybox.
		endRendering();
	}

	@Override
	public void profile() {
		if (FlounderEngine.getProfiler().isOpen()) {
			FlounderEngine.getProfiler().add("Skybox", "Render Time", super.getRenderTimeMs());
		}
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		viewMatrix.set(camera.getViewMatrix());

		// Starts the shader and loads values. TODO: Clip plane.
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(updateViewMatrix());
		shader.lowerFogLimit.loadFloat(Environment.getFog().getSkyLowerLimit());
		shader.upperFogLimit.loadFloat(Environment.getFog().getSkyUpperLimit());
		shader.fogColour.loadVec3(Environment.getFog().getFogColour());

		// Binds the VAO, sets antialiasing and binds the cube map.
		OpenGlUtils.bindVAO(vao, 0);
		OpenGlUtils.bindTextureToBank(TEXTURE, 0);
		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
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
		OpenGlUtils.unbindVAO(0);
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
