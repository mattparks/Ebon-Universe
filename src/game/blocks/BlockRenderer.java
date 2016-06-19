package game.blocks;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import game.*;

import static org.lwjgl.opengl.GL11.*;

public class BlockRenderer extends IRenderer {
	private BlockShader shader;
	private Matrix4f modelMatrix;

	public BlockRenderer() {
		this.shader = new BlockShader();
		this.modelMatrix = new Matrix4f();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);

		Environment.getBlocksManager().getChunkList().forEach(chunk -> {
			if (!chunk.isEmpty() && chunk.isVisible()) {
				renderChunk(chunk);
			}
		});

		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(camera.getViewMatrix());
		shader.clipPlane.loadVec4(clipPlane);
		shader.fogColour.loadVec3(Environment.getFog().getFogColour());
		shader.fogDensity.loadFloat(Environment.getFog().getFogDensity());
		shader.fogGradient.loadFloat(Environment.getFog().getFogGradient());

		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
		OpenGlUtils.cullBackFaces(true);
		OpenGlUtils.enableDepthTesting();
		OpenGlUtils.enableAlphaBlending();
	}

	private void renderChunk(Chunk chunk) {
		OpenGlUtils.bindVAO(chunk.getVAO(), 0, 1, 2, 3);
		shader.modelMatrix.loadMat4(chunk.updateModelMatrix(modelMatrix));
		//	glDrawElements(GL_TRIANGLES, chunk.getVAOLength(), GL_UNSIGNED_INT, 0);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, chunk.getVAOLength());
		OpenGlUtils.unbindVAO(0, 1, 2, 3);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
