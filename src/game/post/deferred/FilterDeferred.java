package game.post.deferred;

import flounder.engine.*;
import flounder.lights.*;
import flounder.post.*;
import flounder.resources.*;
import game.*;
import game.shadows.*;

public class FilterDeferred extends PostFilter {
	private static final int NUMBER_LIGHTS = 32;

	public FilterDeferred() {
		super("filterDeferred", new MyFile("game/post/deferred/deferredFragment.glsl"));
	}

	@Override
	public void storeValues() {
		// Loads camera info.
		shader.getUniformVec3("cameraPosition").loadVec3(FlounderEngine.getCamera().getPosition());
		shader.getUniformMat4("viewMatrix").loadMat4(FlounderEngine.getCamera().getViewMatrix());

		shader.getUniformFloat("shadowMapSize").loadFloat(ShadowRenderer.SHADOW_MAP_SIZE);
		shader.getUniformMat4("shadowSpaceMatrix").loadMat4(((MainRenderer) FlounderEngine.getMasterRenderer()).getShadowMapRenderer().getToShadowMapSpaceMatrix());
		shader.getUniformFloat("shadowDistance").loadFloat(((MainRenderer) FlounderEngine.getMasterRenderer()).getShadowMapRenderer().getShadowDistance());

		shader.getUniformVec3("fogColour").loadVec3(Environment.getFog().getFogColour());
		shader.getUniformFloat("fogDensity").loadFloat(Environment.getFog().getFogDensity());
		shader.getUniformFloat("fogGradient").loadFloat(Environment.getFog().getFogGradient());

		// Clears lights.
		for (int i = 0; i < NUMBER_LIGHTS; i++) {
			shader.getUniformVec3("lightsColour[" + i + "]").loadVec3(0.0f, 0.0f, 0.0f);
			shader.getUniformVec3("lightsPosition[" + i + "]").loadVec3(0.0f, 0.0f, 0.0f);
			shader.getUniformVec3("lightsAttenuation[" + i + "]").loadVec3(1.0f, 0.0f, 0.0f);
		}

		// Puts in new lights.
		int l = 0;

		for (Light light : Environment.getLights()) {
			if (light != null) {
				shader.getUniformVec3("lightsColour[" + l + "]").loadVec3(light.getColour());
				shader.getUniformVec3("lightsPosition[" + l + "]").loadVec3(light.getPosition());
				shader.getUniformVec3("lightsAttenuation[" + l + "]").loadVec3(light.getAttenuation());
				l++;

				if (l >= NUMBER_LIGHTS) {
					FlounderEngine.getLogger().error("Light overflow!");
					break;
				}
			}
		}
	}
}
