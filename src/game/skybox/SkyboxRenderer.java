package game.skybox;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.shaders.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class SkyboxRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "skybox", "skyboxVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "skybox", "skyboxFragment.glsl");

	private static final Vector3f MODEL_POSITION = new Vector3f(0.0f, 0.0f, 0.0f);
	private static final Vector3f MODEL_ROTATION = new Vector3f(0.0f, 0.0f, 0.0f);
	private static final Vector3f MODEL_ROTATION_Y = new Vector3f(0.0f, 1.0f, 0.0f);
	private static final float MODEL_SCALE = 1800.0f;

	private Shader shader;
	private Matrix4f viewMatrix;
	private Matrix4f modelMatrix;
	private SkyboxFBO skyboxFBO;
	private Model skyboxModel;

	public SkyboxRenderer() {
		shader = Shader.newShader("skybox").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).create();
		viewMatrix = new Matrix4f();
		modelMatrix = new Matrix4f();
		skyboxFBO = new SkyboxFBO();
		skyboxModel = Model.newModel(new MyFile(MyFile.RES_FOLDER, "skybox", "skybox.obj")).create();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);
		// glDrawArrays(GL_TRIANGLES, 0, skyboxModel.getVaoLength());
		glDrawElements(GL_TRIANGLES, skyboxModel.getVaoLength(), GL_UNSIGNED_INT, 0);
		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(updateViewMatrix(camera));
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);
		shader.getUniformMat4("modelMatrix").loadMat4(updateModelMatrix(camera));

		OpenGlUtils.bindVAO(skyboxModel.getVaoID(), 0);
		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
		OpenGlUtils.cullBackFaces(false);

		OpenGlUtils.bindCubemapToBank(skyboxFBO.getColourAttachment(), 0);
	}

	private Matrix4f updateViewMatrix(ICamera camera) {
		viewMatrix.set(camera.getViewMatrix());
		viewMatrix.m30 = 0.0f;
		viewMatrix.m31 = 0.0f;
		viewMatrix.m32 = 0.0f;
		float rotation = 0.0f;
		return Matrix4f.rotate(viewMatrix, MODEL_ROTATION_Y, (float) Math.toRadians(rotation), viewMatrix);
	}

	private Matrix4f updateModelMatrix(ICamera camera) {
		modelMatrix.setIdentity();
		return Matrix4f.transformationMatrix(MODEL_POSITION, MODEL_ROTATION, MODEL_SCALE, modelMatrix);
	}

	private void endRendering() {
		OpenGlUtils.unbindVAO(0);
		shader.stop();
	}

	@Override
	public void profile() {
		FlounderEngine.getProfiler().add("Skybox", "Render Time", super.getRenderTimeMs());
	}

	public SkyboxFBO getSkyboxFBO() {
		return skyboxFBO;
	}

	public void rotateCamera(ICamera camera, int fboSide) {
		Vector3f newRotation = new Vector3f();

		switch (fboSide) {
			case 0:
				// POSITIVE_X, Right
				newRotation.x = 0.0f;
				newRotation.y = 90.0f;
				break;
			case 1:
				// NEGATIVE_X, Left
				newRotation.x = 0.0f;
				newRotation.y = 270.0f;
				break;
			case 2:
				// POSITIVE_Y, Top
				newRotation.x = 90.0f;
				newRotation.y = 180.0f;
				break;
			case 3:
				// NEGATIVE_Y, Bottom
				newRotation.x = 270.0f;
				newRotation.y = 180.0f;
				break;
			case 4:
				// POSITIVE_Z, Back
				newRotation.x = 0.0f;
				newRotation.y = 180.0f;
				break;
			case 5:
				// NEGATIVE_Z, Front
				newRotation.x = 0.0f;
				newRotation.y = 0.0f;
				break;
		}

		camera.setRotation(newRotation);
	}

	@Override
	public void dispose() {
		shader.dispose();
		skyboxFBO.dispose();
	}
}
