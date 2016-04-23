package blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.loaders.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import org.lwjgl.opengl.*;

public class BlockRenderer extends IRenderer {
	private BlockShader shader;
	private Matrix4f modelMatrix;

	private int paneVaoID;
	private int paneVaoLength;

	public BlockRenderer() {
		this.shader = new BlockShader();
		this.modelMatrix = new Matrix4f();

		createPane();
	}

	private void createPane() {
		paneVaoID = Loader.createVAO();
		paneVaoLength = 6;
		Loader.createIndicesVBO(paneVaoID, new int[]{1, 3, 2, 0, 1, 2});
		Loader.storeDataInVBO(paneVaoID, new float[]{-1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f}, 0, 3);
		Loader.storeDataInVBO(paneVaoID, new float[]{0.9999f, 1.00016594E-4f, 1.0E-4f, 1.00016594E-4f, 0.9999f, 0.9999f, 1.0E-4f, 0.9999f}, 1, 2);
		Loader.storeDataInVBO(paneVaoID, new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f}, 2, 3);
		Loader.storeDataInVBO(paneVaoID, new float[]{-1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f}, 3, 3);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void renderObjects(final Vector4f clipPlane, final ICamera camera) {
		prepareRendering(clipPlane, camera);

		for (final Chunk chunk : WorldManager.getChunkList()) {
			if (chunk.renderable()) {
				final Block[][][] blocks = chunk.getBlocks();

				for (int x = 0; x < Chunk.CHUNK_LENGTH; x++) {
					for (int y = 0; y < Chunk.CHUNK_HEIGHT - 1; y++) {
						for (int z = 0; z < Chunk.CHUNK_LENGTH; z++) {
							loadBlockFaces(blocks[x][y][z]);
						}
					}
				}
			}
		}

		endRendering();
	}

	private void prepareRendering(final Vector4f clipPlane, final ICamera camera) {
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(camera.getViewMatrix());
		shader.clipPlane.loadVec4(clipPlane);

		OpenglUtils.antialias(ManagerDevices.getDisplay().isAntialiasing());
		OpenglUtils.cullBackFaces(false);
		OpenglUtils.enableDepthTesting();
		OpenglUtils.enableAlphaBlending();

		OpenglUtils.bindVAO(paneVaoID, 0, 1, 2, 3);
	}

	private void loadBlockFaces(final Block b) {
		if (b == null) {
			return;
		}

		for (int f = 0; f < 6; f++) {
			if (b.getFaces()[f].isVisible()) {
				Block.blockModelMatrix(b, b.getFaces()[f].getFace(), modelMatrix);
				final Colour colour = b.getType().getColour();

				shader.modelMatrix.loadMat4(modelMatrix);
				shader.colour.loadVec3(colour);
				GL11.glDrawElements(GL11.GL_TRIANGLES, paneVaoLength, GL11.GL_UNSIGNED_INT, 0); // Render the entity instance.
			}
		}
	}

	private void endRendering() {
		OpenglUtils.unbindVAO(0, 1, 2, 3);
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
