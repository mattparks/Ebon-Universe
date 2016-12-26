package ebon.entities;

import ebon.entities.components.*;
import ebon.world.*;
import flounder.animation.*;
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
public class AnimatedRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "animatedVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "animatedFragment.glsl");

	private Shader shader;

	/**
	 * Creates a new entity renderer.
	 */
	public AnimatedRenderer() {
		shader = Shader.newShader("animated").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).create();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shader.isLoaded() || EbonEntities.getEntities() == null) {
			return;
		}

		prepareRendering(clipPlane, camera);

		for (Entity entity : EbonEntities.getEntities().getAll(new ArrayList<>())) { // .queryInFrustum(new ArrayList<>(), FlounderCamera.getCamera().getViewFrustum())
			renderEntity(entity);
		}

		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(camera.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		shader.getUniformVec3("lightDirection").loadVec3(AnimationSettings.LIGHT_DIR);

		if (EbonWorld.getFog() != null) {
			shader.getUniformVec3("fogColour").loadVec3(EbonWorld.getFog().getFogColour());
			shader.getUniformFloat("fogDensity").loadFloat(EbonWorld.getFog().getFogDensity());
			shader.getUniformFloat("fogGradient").loadFloat(EbonWorld.getFog().getFogGradient());
		} else {
			shader.getUniformVec3("fogColour").loadVec3(1.0f, 1.0f, 1.0f);
			shader.getUniformFloat("fogDensity").loadFloat(0.003f);
			shader.getUniformFloat("fogGradient").loadFloat(2.0f);
		}

		OpenGlUtils.antialias(FlounderDisplay.isAntialiasing());
		OpenGlUtils.enableDepthTesting();
		OpenGlUtils.disableBlending();
	}

	private void renderEntity(Entity entity) {
		ComponentAnimation componentAnimation = (ComponentAnimation) entity.getComponent(ComponentAnimation.ID);

		if (componentAnimation == null) {
			return;
		}

		OpenGlUtils.bindVAO(componentAnimation.getModelAnimated().getVaoID(), 0, 1, 2, 3, 4, 5);

		if (componentAnimation.getTexture() != null) {
			OpenGlUtils.bindTextureToBank(componentAnimation.getTexture().getTextureID(), 0);
			shader.getUniformFloat("atlasRows").loadFloat(componentAnimation.getTexture().getNumberOfRows());
			shader.getUniformVec2("atlasOffset").loadVec2(componentAnimation.getTextureOffset());
		}

		for (int i = 0; i < componentAnimation.getJointTransforms().length; i++) {
			shader.getUniformMat4("jointTransforms[" + i + "]").loadMat4(componentAnimation.getJointTransforms()[i]);
		}

		shader.getUniformMat4("modelMatrix").loadMat4(entity.getModelMatrix());

		glDrawElements(GL_TRIANGLES, componentAnimation.getModelAnimated().getVaoLength(), GL_UNSIGNED_INT, 0);
		OpenGlUtils.unbindVAO(0, 1, 2, 3, 4, 5);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void profile() {
		FlounderProfiler.add("Animated Entities", "Render Time", super.getRenderTimeMs());
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}