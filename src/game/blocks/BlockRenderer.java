package game.blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import game.*;
import game.models.*;
import game.shadows.*;
import game.world.*;
import org.lwjgl.opengl.*;

public class BlockRenderer extends IRenderer {
	private final BlockShader shader;
	private final Model boxModel;
	private final Matrix4f modelMatrix;

	public BlockRenderer() {
		this.shader = new BlockShader();
		this.boxModel = LoaderOBJ.loadOBJ(new MyFile(MyFile.RES_FOLDER, "entities", "triangle.obj"));
		this.modelMatrix = new Matrix4f();
	}

	@Override
	public void renderObjects(final Vector4f clipPlane, final ICamera camera) {
		prepareRendering(clipPlane, camera);

		for (final Chunk chunk : ChunkManager.getChunkList()) {
		//	if (chunk.renderable()) {
				final Block[][][] blocks = chunk.getBlocks();

				for (int x = 0; x < Chunk.CHUNK_LENGTH - 1; x++) {
					for (int y = 0; y < Chunk.CHUNK_HEIGHT - 1; y++) {
						for (int z = 0; z < Chunk.CHUNK_LENGTH - 1; z++) {
							if (blocks[x][y][z] != null && blocks[x][y][z].renderable()) {
    							renderBlock(blocks[x][y][z]);
							}
						}
					}
				}
		//	}
		}

		endRendering();
	}

	private void prepareRendering(final Vector4f clipPlane, final ICamera camera) {
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(camera.getViewMatrix());
		shader.clipPlane.loadVec4(clipPlane);

		shader.fogColour.loadVec3(Environment.getFog().getFogColour());
		shader.fogDensity.loadFloat(Environment.getFog().getFogDensity());
		shader.fogGradient.loadFloat(Environment.getFog().getFogGradient());

		final ShadowRenderer shadowMapMaster = ((MainMasterRenderer) FlounderEngine.getMasterRenderer()).getShadowRenderer();

		shader.shadowSpaceMatrix.loadMat4(shadowMapMaster.getToShadowMapSpaceMatrix());
		shader.shadowDistance.loadFloat(shadowMapMaster.getShadowDistance());
		shader.shadowMapSize.loadFloat(ShadowRenderer.SHADOW_MAP_SIZE);

		OpenglUtils.bindVAO(boxModel.getVaoID(), 0, 1, 2, 3);
		OpenglUtils.antialias(ManagerDevices.getDisplay().isAntialiasing());
		OpenglUtils.enableDepthTesting();
		OpenglUtils.enableAlphaBlending();

		OpenglUtils.bindTextureToBank(shadowMapMaster.getShadowMap(), 1);
	}

	private void renderBlock(final Block block) {
		// TODO: Bind texture.

		shader.modelMatrix.loadMat4(Block.updateModelMatrix(block, modelMatrix));
		shader.colour.loadVec3(block.getType().getColour());
		GL11.glDrawElements(GL11.GL_TRIANGLES, boxModel.getVAOLength(), GL11.GL_UNSIGNED_INT, 0); // Render the entity instance.

		// TODO: Unbind texture.
	}

	private void unbindTexturedModel() {
		OpenglUtils.unbindVAO(0, 1, 2, 3);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
//		boxModel.delete();
	}
}
