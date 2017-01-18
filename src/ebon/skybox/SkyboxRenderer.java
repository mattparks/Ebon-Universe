package ebon.skybox;

import flounder.camera.*;
import flounder.devices.*;
import flounder.helpers.*;
import flounder.loaders.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import flounder.renderer.*;
import flounder.resources.*;
import flounder.shaders.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class SkyboxRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "skybox", "skyboxVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "skybox", "skyboxFragment.glsl");

	private static final float SIZE = 300f;
	private static final float[] VERTICES = {-SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE};

	private Shader shader;
	private int vao;

	public SkyboxRenderer() {
		shader = Shader.newShader("skybox").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).create();

		vao = FlounderLoader.createVAO();
		FlounderLoader.storeDataInVBO(vao, VERTICES, 0, 3);
		OpenGlUtils.unbindVAO();

	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shader.isLoaded()) {
			return;
		}

		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(camera.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());

		OpenGlUtils.bindVAO(vao, 0);

		OpenGlUtils.disableBlending();
		OpenGlUtils.enableDepthTesting();
		OpenGlUtils.cullBackFaces(true);
		OpenGlUtils.antialias(FlounderDisplay.isAntialiasing());

		glDrawArrays(GL_TRIANGLES, 0, VERTICES.length); // Renders the skybox.

		OpenGlUtils.unbindVAO(0);
	}

	@Override
	public void profile() {
		FlounderProfiler.add("Skybox", "Render Time", super.getRenderTime());
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
