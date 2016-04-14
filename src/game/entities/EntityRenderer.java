package game.entities;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.vectors.*;
import game.entities.components.*;
import game.lights.*;
import game.models.*;
import game.world.*;
import org.lwjgl.opengl.*;

import java.util.*;

public class EntityRenderer extends IRenderer {
	public final EntityShader shader;

	public EntityRenderer() {
		shader = new EntityShader();
	}

	@Override
	public void renderObjects(final Vector4f clipPlane, final ICamera camera) {
		prepareRendering(clipPlane, camera);

		for (Entity entity : Environment.getEntityObjects()) {
			final ModelComponent modelTextured = (ModelComponent) entity.getComponent(ModelComponent.ID);
			prepareTexturedModel(modelTextured.getModel());
			prepareInstance(entity);
			GL11.glDrawElements(GL11.GL_TRIANGLES, modelTextured.getModel().getModel().getVAOLength(), GL11.GL_UNSIGNED_INT, 0); // Render the entity instance.
			unbindTexturedModel();
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

		final List<Light> lights = Environment.getLightObjects(camera);

		for (int i = 0; i < EntityShader.MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				shader.lightPosition[i].loadVec3(lights.get(i).getPosition());
				shader.lightColour[i].loadVec3(lights.get(i).getColour());
				shader.attenuation[i].loadVec3(lights.get(i).getAttenuation().toVector());
			} else {
				shader.lightPosition[i].loadVec3(0, 0, 0);
				shader.lightColour[i].loadVec3(0, 0, 0);
				shader.attenuation[i].loadVec3(1, 0, 0);
			}
		}
	}

	private void prepareTexturedModel(ModelTextured texturedModel) {
		shader.useFakeLighting.loadBoolean(texturedModel.isUseFakeLighting());
		shader.atlasRows.loadFloat(texturedModel.getModelTexture().getNumberOfRows());

		shader.shineDamper.loadFloat(texturedModel.getShineDamper());
		shader.reflectivity.loadFloat(texturedModel.getReflectivity());
		shader.useNormalMap.loadBoolean(texturedModel.getNormalTexture() != null);

		OpenglUtils.bindVAO(texturedModel.getModel().getVaoID(), 0, 1, 2, 3, 4);
		OpenglUtils.antialias(ManagerDevices.getDisplay().isAntialiasing());
		OpenglUtils.enableDepthTesting();
		OpenglUtils.enableAlphaBlending();

		OpenglUtils.bindTextureToBank(texturedModel.getModelTexture().getTextureID(), 0);
		OpenglUtils.bindTextureToBank(texturedModel.getNormalTexture() == null ? 0 : texturedModel.getNormalTexture().getTextureID(), 1);
	}

	private void prepareInstance(final Entity entity) {
		final ModelComponent modelComponent = (ModelComponent) entity.getComponent(ModelComponent.ID);
		shader.modelMatrix.loadMat4(entity.getModelMatrix());
		shader.colourMod.loadVec3(modelComponent.getModel().getColourMod());
		shader.transparency.loadFloat(modelComponent.getTransparency());
		shader.atlasOffset.loadVec2(modelComponent.getTextureOffset());

		if (modelComponent.getTransparency() != 1.0 || modelComponent.getModel().getModelTexture().hasTransparency()) {
			OpenglUtils.cullBackFaces(false); // Disable face culling if the object has transparency.
		} else {
			OpenglUtils.cullBackFaces(true); // Enable face culling if the object does not have transparency.
		}
	}

	private void unbindTexturedModel() {
		OpenglUtils.unbindVAO(0, 1, 2, 3, 4);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
