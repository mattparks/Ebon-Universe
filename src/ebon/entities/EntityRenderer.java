package ebon.entities;

import ebon.entities.components.*;
import ebon.world.*;
import flounder.camera.*;
import flounder.devices.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import flounder.renderer.*;
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

	private static final MyFile ANIMATED_VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "animatedVertex.glsl");
	private static final MyFile NORMAL_VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "entityVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "entityFragment.glsl");

	private Shader shaderAnimated;
	private Shader shaderNormal;

	private int renderedAnimated;
	private int renderedNormal;

	/**
	 * Creates a new entity renderer.
	 */
	public EntityRenderer() {
		shaderAnimated = Shader.newShader("entities").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, ANIMATED_VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).create();
		shaderNormal = Shader.newShader("entities").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, NORMAL_VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).create();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shaderAnimated.isLoaded() || !shaderNormal.isLoaded() || EbonEntities.getEntities() == null) {
			return;
		}

		// Prepares both shaders.
		prepareRendering(clipPlane, camera, shaderAnimated);
		prepareRendering(clipPlane, camera, shaderNormal);

		// Attempts to render each entity.
		for (Entity entity : EbonEntities.getEntities().queryInFrustum(new ArrayList<>(), FlounderCamera.getCamera().getViewFrustum())) {
			renderEntity(entity);
		}
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera, Shader shader) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(camera.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		if (EbonWorld.getFog() != null) {
			shader.getUniformVec3("fogColour").loadVec3(EbonWorld.getFog().getFogColour());
			shader.getUniformFloat("fogDensity").loadFloat(EbonWorld.getFog().getFogDensity());
			shader.getUniformFloat("fogGradient").loadFloat(EbonWorld.getFog().getFogGradient());
		} else {
			shader.getUniformVec3("fogColour").loadVec3(1.0f, 1.0f, 1.0f);
			shader.getUniformFloat("fogDensity").loadFloat(0.003f);
			shader.getUniformFloat("fogGradient").loadFloat(2.0f);
		}

		for (int i = 0; i < NUMBER_LIGHTS; i++) {
			if (EbonWorld.getLights() != null && i < EbonWorld.getLights().getSize()) {
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
		shader.stop();

		renderedAnimated = 0;
		renderedNormal = 0;
	}

	private void renderEntity(Entity entity) {
		ComponentModel componentModel = (ComponentModel) entity.getComponent(ComponentModel.ID);
		ComponentAnimation componentAnimation = (ComponentAnimation) entity.getComponent(ComponentAnimation.ID);
		boolean animatedModel = componentAnimation != null && componentAnimation.getAnimation() != null;
		Shader shader = animatedModel ? shaderAnimated : shaderNormal;

		if (!animatedModel && (componentModel == null || componentModel.getModel() == null)) {
			return;
		}

		shader.start();

		if (animatedModel) {
			// TODO: Bind animated VAO.
		} else {
			OpenGlUtils.bindVAO(componentModel.getModel().getVaoID(), 0, 1, 2, 3);
		}

		if (componentModel.getTexture() != null) {
			OpenGlUtils.bindTextureToBank(componentModel.getTexture().getTextureID(), 0);
			shader.getUniformFloat("atlasRows").loadFloat(componentModel.getTexture().getNumberOfRows());
			shader.getUniformVec2("atlasOffset").loadVec2(componentModel.getTextureOffset());

			// Face culling if the object has transparency.
			OpenGlUtils.cullBackFaces(componentModel.getTransparency() == 1.0 || !componentModel.getTexture().hasTransparency());
		}

		if (componentModel.getNormalMap() != null) {
			OpenGlUtils.bindTextureToBank(componentModel.getNormalMap().getTextureID(), 1);
			shader.getUniformBool("useNormalMap").loadBoolean(true);
		} else {
			shaderNormal.getUniformBool("useNormalMap").loadBoolean(false);
		}

		shader.getUniformMat4("modelMatrix").loadMat4(entity.getModelMatrix());
		shader.getUniformFloat("transparency").loadFloat(componentModel.getTransparency());

		if (animatedModel) {
			// TODO: Pass animated uniforms.
		}

		glDrawElements(GL_TRIANGLES, componentModel.getModel().getVaoLength(), GL_UNSIGNED_INT, 0);

		if (animatedModel) {
			// TODO: Unbind animated VAO.
			renderedAnimated++;
		} else {
			OpenGlUtils.unbindVAO(0, 1, 2, 3);
			renderedNormal++;
		}

		shader.stop();
	}

	@Override
	public void profile() {
		FlounderProfiler.add("Entities", "Render Time", super.getRenderTimeMs());
		FlounderProfiler.add("Entities", "Animated", renderedAnimated);
		FlounderProfiler.add("Entities", "Normal", renderedNormal);
	}

	@Override
	public void dispose() {
		shaderAnimated.dispose();
		shaderNormal.dispose();
	}
}