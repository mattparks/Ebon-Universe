package blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.loaders.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;

import static org.lwjgl.opengl.GL11.*;

public class BlockRenderer extends IRenderer {
	private final int[] INDICES = {1, 2, 3, 7, 6, 5, 4, 8, 9, 10, 11, 12, 13, 14, 15, 0, 16, 17, 18, 1, 3, 19, 7, 5, 20, 4, 9, 21, 10, 12, 22, 13, 15, 23, 0, 17};
	private final float[] VERTICES = {1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -0.999999f, 0.999999f, 1.0f, 1.000001f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 0.999999f, 1.0f, 1.000001f, 1.0f, -1.0f, 1.0f, 0.999999f, 1.0f, 1.000001f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -0.999999f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -0.999999f};
	private final float[] TEXTURE_COORDS = {0.666467f, 0.666866f, 0.333134f, 1.9997358E-4f, 0.333134f, 0.33313298f, 2.0E-4f, 0.33313298f, 2.0E-4f, 0.666467f, 0.333134f, 0.666866f, 0.333134f, 0.9998f, 2.0E-4f, 0.9998f, 0.333134f, 0.666467f, 0.333134f, 0.333533f, 0.333533f, 0.666467f, 0.666467f, 0.666467f, 0.666467f, 0.333533f, 0.9998f, 0.9998f, 0.666866f, 0.9998f, 0.666866f, 0.666867f, 0.666467f, 0.9998f, 0.333533f, 0.9998f, 2.0E-4f, 1.9997358E-4f, 2.0E-4f, 0.666866f, 2.0E-4f, 0.333533f, 0.333533f, 0.333533f, 0.9998f, 0.666866f, 0.333533f, 0.666866f};
	private final float[] NORMALS = {0.5773f, -0.5773f, -0.5773f, 0.5773f, -0.5773f, 0.5773f, -0.5773f, -0.5773f, 0.5773f, -0.5773f, -0.5773f, -0.5773f, 0.5773f, 0.5773f, -0.5773f, 0.5773f, 0.5773f, 0.5773f, -0.5773f, 0.5773f, 0.5773f, -0.5773f, 0.5773f, -0.5773f, 0.5773f, 0.5773f, 0.5773f, 0.5773f, -0.5773f, 0.5773f, 0.5773f, 0.5773f, 0.5773f, -0.5773f, 0.5773f, 0.5773f, -0.5773f, -0.5773f, 0.5773f, -0.5773f, -0.5773f, 0.5773f, -0.5773f, 0.5773f, 0.5773f, -0.5773f, 0.5773f, -0.5773f, -0.5773f, -0.5773f, -0.5773f, -0.5773f, 0.5773f, -0.5773f, 0.5773f, -0.5773f, -0.5773f, 0.5773f, 0.5773f, -0.5773f, 0.5773f, -0.5773f, -0.5773f, 0.5773f, -0.5773f, 0.5773f, -0.5773f, -0.5773f, -0.5773f, 0.5773f, 0.5773f, -0.5773f};
	private final int VAO;
	private BlockShader shader;
	private Matrix4f modelMatrix;

	public BlockRenderer() {
		this.shader = new BlockShader();
		this.modelMatrix = new Matrix4f();

		VAO = Loader.createVAO();
		Loader.createIndicesVBO(VAO, INDICES);
		Loader.storeDataInVBO(VAO, VERTICES, 0, 3);
		Loader.storeDataInVBO(VAO, TEXTURE_COORDS, 1, 2);
		Loader.storeDataInVBO(VAO, NORMALS, 2, 3);
		Loader.storeDataInVBO(VAO, NORMALS, 3, 3);
	}

	@Override
	public void renderObjects(final Vector4f clipPlane, final ICamera camera) {
		prepareRendering(clipPlane, camera);

		WorldManager.getChunkList().forEach(chunk -> {
			if (!chunk.isEmpty() && chunk.isVisible()) {
				renderChunk(chunk);
			}
		});

		endRendering();
	}

	private void prepareRendering(final Vector4f clipPlane, final ICamera camera) {
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(camera.getViewMatrix());
		shader.clipPlane.loadVec4(clipPlane);

		OpenglUtils.antialias(ManagerDevices.getDisplay().isAntialiasing());
		OpenglUtils.cullBackFaces(true);
		OpenglUtils.enableDepthTesting();
		OpenglUtils.enableAlphaBlending();
	}

	private void renderChunk(final Chunk chunk) {
		OpenglUtils.bindVAO(VAO, 0, 1, 2, 3); // Chunk.getVAO(chunk)
		shader.modelMatrix.loadMat4(chunk.updateModelMatrix(modelMatrix));
		glDrawElements(GL_TRIANGLES, INDICES.length, GL_UNSIGNED_INT, 0); // Chunk.getVertexCount(chunk)
		OpenglUtils.unbindVAO(0, 1, 2, 3);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
