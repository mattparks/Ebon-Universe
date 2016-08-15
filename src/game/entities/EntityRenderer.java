package game.entities;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.shaders.*;
import game.*;
import game.entities.components.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * A renderer that is used to render entity's.
 */
public class EntityRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile("game/entities", "entityVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/entities", "entityFragment.glsl");

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
		Environment.getEntitys().queryInFrustum(new ArrayList<>(), FlounderEngine.getCamera().getViewFrustum()).forEach(this::renderEntity);
		endRendering();
	}

	@Override
	public void profile() {
		if (FlounderEngine.getProfiler().isOpen()) {
			FlounderEngine.getProfiler().add("Entity", "Render Time", super.getRenderTimeMs());
			//	FlounderEngine.getProfiler().add("Entity", "Objects", Environment.getEntitys().size());
		}
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		shader.getUniformVec3("fogColour").loadVec3(Environment.getFog().getFogColour());
		shader.getUniformFloat("fogDensity").loadFloat(Environment.getFog().getFogDensity());
		shader.getUniformFloat("fogGradient").loadFloat(Environment.getFog().getFogGradient());

		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
		OpenGlUtils.enableDepthTesting();
		OpenGlUtils.enableAlphaBlending();
	}

	private void renderEntity(Entity entity) {
		ModelComponent modelComponent = (ModelComponent) entity.getComponent(ModelComponent.ID);

		if (modelComponent == null || modelComponent.getModel() == null) {
			return;
		}

		OpenGlUtils.bindVAO(modelComponent.getModel().getVaoID(), 0, 1, 2, 3);
		OpenGlUtils.bindTextureToBank(modelComponent.getTexture().getTextureID(), 0);

		if (modelComponent.getNormalMap() != null) {
			OpenGlUtils.bindTextureToBank(modelComponent.getNormalMap().getTextureID(), 1);
		}

		shader.getUniformMat4("modelMatrix").loadMat4(entity.getModelMatrix());

		shader.getUniformFloat("numberOfRows").loadFloat(modelComponent.getTexture().getNumberOfRows());
		shader.getUniformVec2("textureOffset").loadVec2(modelComponent.getTextureOffset());
		shader.getUniformBool("useNormalMap").loadBoolean(modelComponent.getNormalMap() != null);

		shader.getUniformFloat("transparency").loadFloat(modelComponent.getTransparency());

		if (modelComponent.getTransparency() != 1.0 || modelComponent.getTexture().hasTransparency()) {
			OpenGlUtils.cullBackFaces(false); // Disable face culling if the object has transparency.
		} else {
			OpenGlUtils.cullBackFaces(true); // Enable face culling if the object does not have transparency.
		}

		glDrawElements(GL_TRIANGLES, modelComponent.getModel().getVaoLength(), GL_UNSIGNED_INT, 0);

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