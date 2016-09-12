package game.skybox;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.shaders.*;
import game.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class SkyboxRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "skybox", "skyboxVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "skybox", "skyboxFragment.glsl");

	private static final float SIZE = 1800f;
	private static final float[] VERTICES = {-SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE};
	private static final int VAO = FlounderEngine.getLoader().createInterleavedVAO(VERTICES, 3);
	private static final Vector3f ROTATE_Y = new Vector3f(0.0f, 1.0f, 0.0f);

	private Shader shader;
	private Matrix4f viewMatrix;
	private SkyboxFBO skyboxFBO;

	public SkyboxRenderer() {
		shader = Shader.newShader("skybox").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).create();
		viewMatrix = new Matrix4f();
		skyboxFBO = new SkyboxFBO();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);

		{
			OpenGlUtils.bindVAO(VAO, 0);
			OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());

			for (int s = 0; s < 6; s++) {
				OpenGlUtils.bindTextureToBank(skyboxFBO.getFBO(s).getColourTexture(0), s);
			}

			glDrawArrays(GL_TRIANGLES, 0, VERTICES.length);

			OpenGlUtils.unbindVAO(0);
		}

		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();

		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(updateViewMatrix(camera.getViewMatrix()));

		shader.getUniformFloat("lowerFogLimit").loadFloat(Environment.getFog().getSkyLowerLimit());
		shader.getUniformFloat("upperFogLimit").loadFloat(Environment.getFog().getSkyUpperLimit());
		shader.getUniformVec3("fogColour").loadVec3(Environment.getFog().getFogColour());
	}

	private Matrix4f updateViewMatrix(Matrix4f input) {
		viewMatrix.set(input);
		viewMatrix.m30 = 0.0f;
		viewMatrix.m31 = 0.0f;
		viewMatrix.m32 = 0.0f;
		float rotation = 0.0f;
		return Matrix4f.rotate(viewMatrix, ROTATE_Y, (float) Math.toRadians(rotation), viewMatrix);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void profile() {
		FlounderEngine.getProfiler().add("Skybox", "Render Time", super.getRenderTimeMs());
	}

	public boolean isSkyboxNull() {
		return skyboxFBO.isFboLoaded();
	}

	public void setSkyboxNull(boolean isNull) {
		skyboxFBO.setFboLoaded(isNull);
	}

	public void bindSkyboxFBO(int fboSide) {
		OpenGlUtils.bindTextureToBank(skyboxFBO.getFBO(fboSide).getColourTexture(0), fboSide);
	}

	public void unbindSkyboxFBO(int fboSide) {
		skyboxFBO.getFBO(fboSide).unbindFrameBuffer();
	}

	public void rotateCamera(ICamera camera, int fboSide) {
		float pitch = 0.0f;
		float yaw = 0.0f;
		float roll = 0.0f;

		switch (fboSide) {
			case 0:
				// fboFront
				pitch = 0.0f;
				yaw = 0.0f;
				roll = 0.0f;
				break;
			case 1:
				// fboBack
				pitch = 0.0f;
				yaw = 180.0f;
				roll = 0.0f;
				break;
			case 2:
				// fboUp
				pitch = 90.0f;
				yaw = 0.0f;
				roll = 0.0f;
				break;
			case 3:
				// fboDown
				pitch = 180.0f;
				yaw = 0.0f;
				roll = 0.0f;
				break;
			case 4:
				// fboLeft
				pitch = 0.0f;
				yaw = 90.0f;
				roll = 0.0f;
				break;
			case 5:
				// fboRight
				pitch = 0.0f;
				yaw = 180.0f;
				roll = 0.0f;
				break;
		}

		camera.getPosition().set(pitch, yaw, roll);
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
