package ebon.universe.orbits;

import flounder.devices.*;
import flounder.framework.entrance.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.shaders.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class OrbitsRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "orbits", "orbitsVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "orbits", "orbitsFragment.glsl");

	private Shader shader;

	private Orbit testing;

	public OrbitsRenderer() {
		shader = Shader.newShader("orbits").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).createInSecondThread();

		testing = new Orbit(0.0, 1.0, 1.0, 0.0, 0.0, 0.0);
		FlounderLogger.log("Testing Orbit: " + testing.toString());
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shader.isLoaded()) {
			return;
		}

		prepareRendering(clipPlane, camera);
		prepareInstance(testing, camera); // TODO: Expand to iterate though a orbit list.
		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		OpenGlUtils.antialias(FlounderDisplay.isAntialiasing());
		OpenGlUtils.cullBackFaces(false);
		OpenGlUtils.disableDepthTesting();
		glDepthMask(false); // Stops orbits from being rendered to the depth buffer.
	}

	private void prepareInstance(Orbit orbit, ICamera camera) {
		Matrix4f modelMatrix = Matrix4f.transformationMatrix(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f((float) orbit.getPitch(), (float) orbit.getYaw(), (float) orbit.getRoll()), new Vector3f(1.0f, 1.0f, 1.0f), null);
		Vector3f colour = new Vector3f(1, 0, 0);

		shader.getUniformMat4("modelMatrix").loadMat4(modelMatrix);
		shader.getUniformVec3("colour").loadVec3(colour);
	}

	private void endRendering() {
		glDepthMask(true);
		shader.stop();
	}

	@Override
	public void profile() {

	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
