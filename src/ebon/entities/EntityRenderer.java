package ebon.entities;

import ebon.entities.components.*;
import ebon.world.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.entrance.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import flounder.resources.*;
import flounder.shaders.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * A renderer that is used to render entity's.
 */
public class EntityRenderer extends IRenderer {
	private static final int NUMBER_LIGHTS = 4;

	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "entityVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "entityFragment.glsl");

	private Shader shader;

	/**
	 * Creates a new entity renderer.
	 */
	public EntityRenderer() {
		shader = Shader.newShader("entities").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).createInSecondThread();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shader.isLoaded() || EbonEntities.getEntities() == null) {
			return;
		}

		prepareRendering(clipPlane, camera);

		for (Entity entity : EbonEntities.getEntities().queryInFrustum(new ArrayList<>(), FlounderEngine.getCamera().getViewFrustum())) {
			renderEntity(entity);
		}

		endRendering();
	}

	@Override
	public void profile() {
		FlounderProfiler.add("Entities", "Render Time", super.getRenderTimeMs());
		//	FlounderProfiler.add("Entities", "Objects", Environment.getEntities().size());
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		shader.getUniformVec3("fogColour").loadVec3(EbonWorld.getFog().getFogColour());
		shader.getUniformFloat("fogDensity").loadFloat(EbonWorld.getFog().getFogDensity());
		shader.getUniformFloat("fogGradient").loadFloat(EbonWorld.getFog().getFogGradient());

		for (int i = 0; i < NUMBER_LIGHTS; i++) {
			if (i < EbonWorld.getLights().getSize()) {
				shader.getUniformVec3("lightPosition[" + i + "]").loadVec3(EbonWorld.getLights().get(i).getPosition());
				shader.getUniformVec3("lightColour[" + i + "]").loadVec3(EbonWorld.getLights().get(i).getColour());
				shader.getUniformVec3("lightAttenuation[" + i + "]").loadVec3(EbonWorld.getLights().get(i).getAttenuation());
			} else {
				shader.getUniformVec3("lightPosition[" + i + "]").loadVec3(0.0f, 0.0f, 0.0f);
				shader.getUniformVec3("lightColour[" + i + "]").loadVec3(0.0f, 0.0f, 0.0f);
				shader.getUniformVec3("lightAttenuation[" + i + "]").loadVec3(1.0f, 0.0f, 0.0f);
			}
		}

		OpenGlUtils.antialias(FlounderDisplay.isAntialiasing());
		OpenGlUtils.enableDepthTesting();
		OpenGlUtils.enableAlphaBlending();
	}

	private void renderEntity(Entity entity) {
		ComponentModel modelComponent = (ComponentModel) entity.getComponent(ComponentModel.ID);

		if (modelComponent == null || modelComponent.getModel() == null) {
			return;
		}

		OpenGlUtils.bindVAO(modelComponent.getModel().getVaoID(), 0, 1, 2, 3);
		OpenGlUtils.cullBackFaces(true); // Enable face culling if the object does not have transparency.

		if (modelComponent.getTexture() != null) {
			OpenGlUtils.bindTextureToBank(modelComponent.getTexture().getTextureID(), 0);
			shader.getUniformFloat("atlasRows").loadFloat(modelComponent.getTexture().getNumberOfRows());
			shader.getUniformVec2("atlasOffset").loadVec2(modelComponent.getTextureOffset());

			if (modelComponent.getTransparency() != 1.0 || modelComponent.getTexture().hasTransparency()) {
				OpenGlUtils.cullBackFaces(false); // Disable face culling if the object has transparency.
			}
		}

		if (modelComponent.getNormalMap() != null) {
			OpenGlUtils.bindTextureToBank(modelComponent.getNormalMap().getTextureID(), 1);
			shader.getUniformBool("useNormalMap").loadBoolean(true);
		} else {
			shader.getUniformBool("useNormalMap").loadBoolean(false);
		}

		shader.getUniformMat4("modelMatrix").loadMat4(entity.getModelMatrix());
		shader.getUniformFloat("transparency").loadFloat(modelComponent.getTransparency());

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