package game.planets;

import flounder.engine.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import org.lwjgl.opengl.*;

public class PlanetRenderer extends IRenderer {
	private final Matrix4f transformation;
	private PlanetShader shader;
	private Planet planet;

	public PlanetRenderer() {
		shader = new PlanetShader();
		planet = new Planet();
		transformation = new Matrix4f();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		planet.update(camera);

		if (planet.getChunks().size() < 1) {
			return;
		}

		prepareRendering(clipPlane, camera);

		for (PlanetChunk c : planet.getChunks()) {
			Vector3f position = Vector3f.add(planet.getPosition(), c.getPosition(), null);
			shader.modelMatrix.loadMat4(Matrix4f.transformationMatrix(position, c.getRotation(), 1, transformation));
			shader.colour.loadVec3(c.getColour());
			OpenglUtils.bindVAO(c.getModel().getVaoID(), 0);
			OpenglUtils.antialias(true);
			OpenglUtils.cullBackFaces(false);
			GL11.glDrawElements(GL11.GL_TRIANGLES, c.getModel().getVAOLength(), GL11.GL_UNSIGNED_INT, 0); // Render the planet chunk instance.
			OpenglUtils.unbindVAO(0);
		}

		endRendering();
	}

	public void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(camera.getViewMatrix());
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
