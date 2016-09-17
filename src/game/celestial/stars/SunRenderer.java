package game.celestial.stars;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.shaders.*;
import game.*;
import game.celestial.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class SunRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "sun", "sunVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "sun", "sunFragment.glsl");

	private static Vector3f ROTATION_REUSABLE = new Vector3f();
	private static Matrix4f MODEL_MATRIX_REUSABLE = new Matrix4f();

	private Model sunModel;
	private Shader shader;

	public SunRenderer() {
		sunModel = Model.newModel(new MyFile(MyFile.RES_FOLDER, "models", "sphere.obj")).create();

		shader = Shader.newShader("sun").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).createInSecondThread();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shader.isLoaded() || Environment.IN_SYSTEM_STAR == null) {
			return;
		}

		prepareRendering(clipPlane, camera);
		renderStar(Environment.IN_SYSTEM_STAR);
		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
		OpenGlUtils.cullBackFaces(true);
		OpenGlUtils.enableDepthTesting();

		OpenGlUtils.bindVAO(sunModel.getVaoID(), 0, 1, 2, 3);
	}

	private void renderStar(Star star) {
		Matrix4f.transformationMatrix(star.getPosition(), ROTATION_REUSABLE, (float) star.getSolarRadius() * 0.05f, MODEL_MATRIX_REUSABLE); // (float) star.getSolarRadius()

		shader.getUniformMat4("modelMatrix").loadMat4(MODEL_MATRIX_REUSABLE);
		shader.getUniformVec3("colour").loadVec3(star.getSurfaceColour());

		glDrawElements(GL_TRIANGLES, sunModel.getVaoLength(), GL_UNSIGNED_INT, 0);
	}

	private void endRendering() {
		OpenGlUtils.unbindVAO(0, 1, 2, 3);
		shader.stop();
	}

	@Override
	public void profile() {
		FlounderEngine.getProfiler().add("Sun", "Render Time", super.getRenderTimeMs());
	}

	@Override
	public void dispose() {
	}
}
