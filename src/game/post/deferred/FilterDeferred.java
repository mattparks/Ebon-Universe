package game.post.deferred;

import flounder.engine.*;
import flounder.lights.*;
import flounder.post.*;
import flounder.resources.*;
import game.*;

public class FilterDeferred extends PostFilter {
	private static final int NUMBER_LIGHTS = 32;

	public FilterDeferred() {
		super("filterDeferred", new MyFile("game/post/deferred/deferredFragment.glsl"));
	}

	@Override
	public void storeValues() {
		// Loads camera info.
		shader.getUniformVec3("cameraPosition").loadVec3(FlounderEngine.getCamera().getPosition());

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
