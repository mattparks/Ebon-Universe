package game.waters;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.fbos.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.textures.*;
import game.*;

import static org.lwjgl.opengl.GL11.*;

public class WaterRenderer extends IRenderer {
	public static final MyFile WATERS_LOC = new MyFile(MyFile.RES_FOLDER, "water");
	public static final Texture dudvTexture = Texture.newTexture(new MyFile(WATERS_LOC, "dudvMap.png")).create();
	public static final Texture normalTexture = Texture.newTexture(new MyFile(WATERS_LOC, "normalMap.png")).create();

	private static final float WAVE_SPEED = 0.0125f;

	private WaterShader shader;
	private Matrix4f transformationMatrix;
	private float moveFactor;

	public FBO refractionFBO;
	public FBO reflectionFBO;

	public WaterRenderer() {
		shader = new WaterShader();
		transformationMatrix = new Matrix4f();
		moveFactor = 0.0f;

		refractionFBO = FBO.newFBO(0.6f).depthBuffer(DepthBufferType.TEXTURE).create();
		reflectionFBO = FBO.newFBO(0.6f).depthBuffer(DepthBufferType.RENDER_BUFFER).create();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);

		moveFactor += WAVE_SPEED * FlounderEngine.getDelta();
		moveFactor %= 1;

		for (Water w : Environment.getWaterObjects(camera)) {
			prepareWater(w);
			glDrawArrays(GL_TRIANGLES, 0, Water.VERTICES.length); // Render the water instance.
			unbindWater();
		}

		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(camera.getViewMatrix());
		shader.nearPlane.loadFloat(camera.getNearPlane());
		shader.farPlane.loadFloat(camera.getFarPlane());

		shader.fogColour.loadVec3(Environment.getFog().getFogColour());
		shader.fogDensity.loadFloat(Environment.getFog().getFogDensity());
		shader.fogGradient.loadFloat(Environment.getFog().getFogGradient());

		shader.lightPosition.loadVec3(Environment.getSun().getPosition());
		shader.lightColour.loadVec3(Environment.getSun().getColour());
	}

	private void prepareWater(Water tile) {
		Matrix4f.transformationMatrix(tile.getPosition(), tile.getRotation(), Environment.WATER_SIZE, transformationMatrix.setIdentity());
		shader.modelMatrix.loadMat4(transformationMatrix);

		shader.moveFactor.loadFloat(moveFactor);
		shader.colourAdditive.loadVec3(tile.colourAdditive);
		shader.colourMix.loadFloat(tile.colourMix);
		shader.textureTiling.loadFloat(tile.textureTiling);
		shader.waveStrength.loadFloat(tile.waveStrength);
		shader.normalDampener.loadFloat(tile.normalDampener);
		shader.dropOffDepth.loadFloat(tile.dropOffDepth);
		shader.reflectivity.loadFloat(tile.reflectivity);
		shader.shineDamper.loadFloat(tile.shineDamper);

		OpenGlUtils.bindVAO(Water.VAO, 0);
		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
		OpenGlUtils.cullBackFaces(false);
		OpenGlUtils.enableDepthTesting();
		OpenGlUtils.disableBlending();

		OpenGlUtils.bindTextureToBank(reflectionFBO.getColourTexture(), 0);
		OpenGlUtils.bindTextureToBank(refractionFBO.getColourTexture(), 1);
		OpenGlUtils.bindTextureToBank(dudvTexture.getTextureID(), 2);
		OpenGlUtils.bindTextureToBank(normalTexture.getTextureID(), 3);
		OpenGlUtils.bindTextureToBank(refractionFBO.getDepthTexture(), 4);
	}

	private void unbindWater() {
		OpenGlUtils.unbindVAO(0);
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
		refractionFBO.delete();
		reflectionFBO.delete();
	}
}
