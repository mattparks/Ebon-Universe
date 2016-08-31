package game.terrains;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.shaders.*;
import game.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * A renderer that is used to render entity's.
 */
public class TerrainRenderer extends IRenderer {
	private static final int NUMBER_LIGHTS = 4;

	private static final MyFile VERTEX_SHADER = new MyFile(ShaderProgram.SHADERS_LOC, "terrains", "terrainVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(ShaderProgram.SHADERS_LOC, "terrains", "terrainFragment.glsl");

	private ShaderProgram shader;

	/**
	 * Creates a new entity renderer.
	 */
	public TerrainRenderer() {
		shader = new ShaderProgram("terrains", VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (Environment.getTerrains() == null) {
			return;
		}

		prepareRendering(clipPlane, camera);

		for (Terrain terrain : Environment.getTerrains().queryInFrustum(new ArrayList<>(), FlounderEngine.getCamera().getViewFrustum())) {
			renderTerrain(terrain);
		}

		endRendering();
	}

	@Override
	public void profile() {
		FlounderEngine.getProfiler().add("Terrains", "Render Time", super.getRenderTimeMs());
		//	FlounderEngine.getProfiler().add("Terrains", "Objects", Environment.getTerrains().size());
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		shader.getUniformVec3("fogColour").loadVec3(Environment.getFog().getFogColour());
		shader.getUniformFloat("fogDensity").loadFloat(Environment.getFog().getFogDensity());
		shader.getUniformFloat("fogGradient").loadFloat(Environment.getFog().getFogGradient());

		for (int i = 0; i < NUMBER_LIGHTS; i++) {
			if (i < Environment.getLights().size()) {
				shader.getUniformVec3("lightPosition[" + i + "]").loadVec3(Environment.getLights().get(i).getPosition());
				shader.getUniformVec3("lightColour[" + i + "]").loadVec3(Environment.getLights().get(i).getColour());
				shader.getUniformVec3("lightAttenuation[" + i + "]").loadVec3(Environment.getLights().get(i).getAttenuation());
			} else {
				shader.getUniformVec3("lightPosition[" + i + "]").loadVec3(0.0f, 0.0f, 0.0f);
				shader.getUniformVec3("lightColour[" + i + "]").loadVec3(0.0f, 0.0f, 0.0f);
				shader.getUniformVec3("lightAttenuation[" + i + "]").loadVec3(1.0f, 0.0f, 0.0f);
			}
		}

		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
	}

	private void renderTerrain(Terrain terrain) {
		Model model = terrain.getModel();
		TerrainTexturePack texturePack = terrain.getTexturePack();

		OpenGlUtils.bindVAO(model.getVaoID(), 0, 1, 2);
		OpenGlUtils.cullBackFaces(true); // Enable face culling if the object does not have transparency.

		shader.getUniformMat4("modelMatrix").loadMat4(terrain.getModelMatrix());

		shader.getUniformFloat("shineDamper").loadFloat(1); // TODO: Add shines.
		shader.getUniformFloat("reflectivity").loadFloat(0);

		OpenGlUtils.bindTextureToBank(texturePack.getBackgroundTexture().getTextureID(), 0);
		OpenGlUtils.bindTextureToBank(texturePack.getRTexture().getTextureID(), 1);
		OpenGlUtils.bindTextureToBank(texturePack.getGTexture().getTextureID(), 2);
		OpenGlUtils.bindTextureToBank(texturePack.getBTexture().getTextureID(), 3);

		glDrawElements(GL_TRIANGLES, model.getVaoLength(), GL_UNSIGNED_INT, 0);
		OpenGlUtils.unbindVAO(0, 1, 2);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}