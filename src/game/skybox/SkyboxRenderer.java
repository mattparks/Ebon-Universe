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

public class SkyboxRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile("game/skybox", "skyboxVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/skybox", "skyboxFragment.glsl");

	public static final MyFile SKYBOXES_LOC = new MyFile(MyFile.RES_FOLDER, "skybox");
	private static final MyFile[] TEXTURE_FILES = {new MyFile(SKYBOXES_LOC, "nightRight.png"), new MyFile(SKYBOXES_LOC, "nightLeft.png"), new MyFile(SKYBOXES_LOC, "nightTop.png"), new MyFile(SKYBOXES_LOC, "nightBottom.png"), new MyFile(SKYBOXES_LOC, "nightBack.png"), new MyFile(SKYBOXES_LOC, "nightFront.png")};
	private static final int TEXTURE = FlounderEngine.getTextures().loadCubeMap(TEXTURE_FILES);

	private static final float SIZE = 1800f;
	private static final float[] VERTICES = {-SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE};
	public static final int VAO = FlounderEngine.getLoader().createInterleavedVAO(VERTICES, 3);

	private ShaderProgram shader;

	public SkyboxRenderer() {
		shader = new ShaderProgram("skybox", VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);

		{
			OpenGlUtils.bindVAO(VAO, 0);
			OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
			OpenGlUtils.bindTextureToBank(TEXTURE, 0);

			glDrawArrays(GL_TRIANGLES, 0, VERTICES.length);

			OpenGlUtils.unbindVAO(0);
		}

		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();

		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(createViewMatrix(camera.getViewMatrix()));

		shader.getUniformFloat("lowerFogLimit").loadFloat(Environment.getFog().getSkyLowerLimit());
		shader.getUniformFloat("upperFogLimit").loadFloat(Environment.getFog().getSkyUpperLimit());
		shader.getUniformVec3("fogColour").loadVec3(Environment.getFog().getFogColour());
	}

	private Matrix4f createViewMatrix(Matrix4f input) {
		Matrix4f matrix = new Matrix4f(input);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		float rotation = 0;
		return Matrix4f.rotate(matrix, new Vector3f(0, 1, 0), (float) Math.toRadians(rotation), matrix);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void profile() {
		FlounderEngine.getProfiler().add("Skybox", "Render Time", super.getRenderTimeMs());
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
