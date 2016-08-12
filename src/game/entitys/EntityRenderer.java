package game.entitys;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.resources.*;
import flounder.shaders.*;
import game.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * A renderer that is used to render entity's.
 */
public class EntityRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile("game/entitys", "entityVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/entitys", "entityFragment.glsl");

	private ShaderProgram shader;

	/**
	 * Creates a new entity renderer.
	 */
	public EntityRenderer() {
		shader = new ShaderProgram("entity", VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);

		for (Entity entity : Environment.getEntitys()) {
			renderEntity(entity);
		}

		endRendering();
	}

	@Override
	public void profile() {
		if (FlounderEngine.getProfiler().isOpen()) {
			FlounderEngine.getProfiler().add("Entity", "Render Time", super.getRenderTimeMs());
			FlounderEngine.getProfiler().add("Entity", "Objects", Environment.getEntitys().size());
		}
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
		OpenGlUtils.cullBackFaces(true);
		OpenGlUtils.enableDepthTesting();
	}

	private void renderEntity(Entity entity) {
		OpenGlUtils.bindVAO(entity.getModel().getVaoID(), 0, 1, 2, 3);
		OpenGlUtils.bindTextureToBank(entity.getTexture().getTextureID(), 0);
		OpenGlUtils.bindTextureToBank(entity.getNormalMap().getTextureID(), 1);

		shader.getUniformMat4("modelMatrix").loadMat4(entity.getModelMatrix());

		shader.getUniformFloat("numberOfRows").loadFloat(entity.getTexture().getNumberOfRows());
		shader.getUniformVec2("textureOffset").loadVec2(entity.getTextureOffset());
		shader.getUniformBool("useNormalMap").loadBoolean(entity.getNormalMap() != null);

		glDrawElements(GL_TRIANGLES, entity.getModel().getVaoLength(), GL_UNSIGNED_INT, 0);

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