package game.terrains;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import game.*;
import game.models.*;

import static org.lwjgl.opengl.GL11.*;

public class TerrainRenderer extends IRenderer {
	private TerrainShader shader;
	private Matrix4f transformationMatrix;

	public TerrainRenderer() {
		shader = new TerrainShader();
		transformationMatrix = new Matrix4f();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);

		for (Terrain t : Environment.getTerrainObjects(camera)) {
			prepareTerrain(t);
			glDrawElements(GL_TRIANGLES, t.getModel().getVaoLength(), GL_UNSIGNED_INT, 0); // Render the terrain instance.
			unbindTerrain();
		}

		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(camera.getViewMatrix());
		shader.clipPlane.loadVec4(clipPlane);

		shader.fogColour.loadVec3(Environment.getFog().getFogColour());
		shader.fogDensity.loadFloat(Environment.getFog().getFogDensity());
		shader.fogGradient.loadFloat(Environment.getFog().getFogGradient());

		//List<Light> lights = Environment.getLightObjects(camera);
		shader.lightPosition[0].loadVec3(Environment.getSun().getPosition());
		shader.lightColour[0].loadVec3(Environment.getSun().getColour());
		shader.attenuation[0].loadVec3(Environment.getSun().getAttenuation());

		for (int i = 1; i < TerrainShader.MAX_LIGHTS; i++) {
			//if (i < lights.size()) {
			//	shader.lightPosition[i].loadVec3(lights.get(i).getPosition());
			//	shader.lightColour[i].loadVec3(lights.get(i).getColour().toVector());
			//	shader.attenuation[i].loadVec3(lights.get(i).getAttenuation().toVector());
			//} else {
				shader.lightPosition[i].loadVec3(new Vector3f(0, 0, 0));
				shader.lightColour[i].loadVec3(new Vector3f(0, 0, 0));
				shader.attenuation[i].loadVec3(new Vector3f(1, 0, 0));
			//}
		}
	}

	private void prepareTerrain(Terrain terrain) {
		Model model = terrain.getModel();
		TerrainTexturePack texturePack = terrain.getTexturePack();
		Matrix4f.transformationMatrix(terrain.getPosition(), terrain.getRotation(), 1.0f, transformationMatrix);

		OpenGlUtils.bindVAO(model.getVaoID(), 0, 1, 2);
		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
		OpenGlUtils.cullBackFaces(true);

		shader.modelMatrix.loadMat4(transformationMatrix);

		shader.shineDamper.loadFloat(1); // TODO: Add shines.
		shader.reflectivity.loadFloat(0);

		OpenGlUtils.bindTextureToBank(texturePack.getBackgroundTexture().getTextureID(), 0);
		OpenGlUtils.bindTextureToBank(texturePack.getRTexture().getTextureID(), 1);
		OpenGlUtils.bindTextureToBank(texturePack.getGTexture().getTextureID(), 2);
		OpenGlUtils.bindTextureToBank(texturePack.getBTexture().getTextureID(), 3);
	}

	private void unbindTerrain() {
		OpenGlUtils.unbindVAO(0, 1, 2);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void profile() {
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
