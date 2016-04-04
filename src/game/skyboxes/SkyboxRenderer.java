package game.skyboxes;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.profiling.*;
import flounder.loaders.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.textures.*;
import game.world.*;
import org.lwjgl.opengl.*;

public class SkyboxRenderer extends IRenderer {
	public static final MyFile SKYBOXES_LOC = new MyFile(MyFile.RES_FOLDER, "skybox");
	private static final int TEXTURE = TextureManager.loadCubeMap(new MyFile(SKYBOXES_LOC, "nightRight.png"), new MyFile(SKYBOXES_LOC, "nightLeft.png"), new MyFile(SKYBOXES_LOC, "nightTop.png"), new MyFile(SKYBOXES_LOC, "nightBottom.png"), new MyFile(SKYBOXES_LOC, "nightBack.png"), new MyFile(SKYBOXES_LOC, "nightFront.png"));

	private static final float SIZE = 300f;
	private static final float[] VERTICES = {-SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE};
	public final int VAO;

	private final SkyboxShader shader;
	private final Matrix4f viewMatrix;
	private final Vector3f viewMatrixRotate;

	public SkyboxRenderer() {
		shader = new SkyboxShader();
		viewMatrix = new Matrix4f().setIdentity();
		viewMatrixRotate = new Vector3f(0.0f, 1.0f, 0.0f);

		VAO = Loader.createVAO();
		Loader.storeDataInVBO(VAO, VERTICES, 0, 3);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void renderObjects(final Vector4f clipPlane, final ICamera camera) {
		prepareRendering(clipPlane, camera);

		OpenglUtils.bindVAO(VAO, 0);
		OpenglUtils.antialias(ManagerDevices.getDisplay().isAntialiasing());
		OpenglUtils.bindTextureToBank(TEXTURE, 0);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, VERTICES.length); // Renders the skybox.

		OpenglUtils.unbindVAO(0);
		endRendering();
		FlounderProfiler.add("Skybox", "Render Time", super.getRenderTimeMs());
	}

	private void prepareRendering(final Vector4f clipPlane, final ICamera camera) {
		shader.start();

		viewMatrix.set(camera.getViewMatrix());

		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(updateViewMatrix());
		shader.lowerFogLimit.loadFloat(Environment.getFog().getSkyLowerLimit());
		shader.upperFogLimit.loadFloat(Environment.getFog().getSkyUpperLimit());
		shader.fogColour.loadVec3(Environment.getFog().getFogColour());

		// Logger.error(Matrix4f.transform(Matrix4f.multiply(FlounderEngine.getProjectionMatrix(), updateViewMatrix(), null), new Vector4f(12, 0, -5, 1), null));
	}

	private Matrix4f updateViewMatrix() {
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		float rotation = 0;
		return Matrix4f.rotate(viewMatrix, viewMatrixRotate, (float) Math.toRadians(rotation), viewMatrix);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
