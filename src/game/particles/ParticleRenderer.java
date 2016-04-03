package game.particles;

import flounder.devices.*;
import flounder.engine.*;
import flounder.loaders.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import java.nio.*;
import java.util.*;

public class ParticleRenderer extends IRenderer {
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGTH = 22;

	private static final int VAO = Loader.createInterleavedVAO(VERTICES, 2);
	private static final FloatBuffer BUFFER = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	private int VBO = Loader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

	private ParticleShader shader;
	private int pointer;
	private List<List<Particle>> viewableParticles;

	public ParticleRenderer() {
		shader = new ParticleShader();
		viewableParticles = new ArrayList<>();

		Loader.addInstancedAttribute(VAO, VBO, 1, 4, INSTANCE_DATA_LENGTH, 0);
		Loader.addInstancedAttribute(VAO, VBO, 2, 4, INSTANCE_DATA_LENGTH, 4);
		Loader.addInstancedAttribute(VAO, VBO, 3, 4, INSTANCE_DATA_LENGTH, 8);
		Loader.addInstancedAttribute(VAO, VBO, 4, 4, INSTANCE_DATA_LENGTH, 12);
		Loader.addInstancedAttribute(VAO, VBO, 5, 4, INSTANCE_DATA_LENGTH, 16);
		Loader.addInstancedAttribute(VAO, VBO, 6, 1, INSTANCE_DATA_LENGTH, 20);
		Loader.addInstancedAttribute(VAO, VBO, 7, 1, INSTANCE_DATA_LENGTH, 21);
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);
		ParticleManager.getParticles(viewableParticles, camera);

		for (List<Particle> list : viewableParticles) {
			pointer = 0;
			float[] vboData = new float[list.size() * INSTANCE_DATA_LENGTH];

			for (Particle p : list) {
				prepareTexturedModel(p.getParticleType());
				prepareInstance(p, camera, vboData);
			}

			Loader.updateVBO(VBO, vboData, BUFFER);
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, VERTICES.length, list.size());
			unbindTexturedModel();
		}

		shader.stop();
	}

	public void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.projectionMatrix.loadMat4(FlounderEngine.getProjectionMatrix());
		shader.viewMatrix.loadMat4(camera.getViewMatrix());
		shader.clipPlane.loadVec4(clipPlane);
	}

	private void prepareTexturedModel(final ParticleType particleType) {
		unbindTexturedModel();

		OpenglUtils.bindVAO(VAO, 0, 1, 2, 3, 4, 5, 6, 7);
		OpenglUtils.antialias(ManagerDevices.getDisplay().isAntialiasing());
		OpenglUtils.cullBackFaces(true);
		OpenglUtils.enableDepthTesting();
		OpenglUtils.enableAlphaBlending();
		GL11.glDepthMask(false); // Stops engine.particles from being rendered to the depth BUFFER.

		shader.numberOfRows.loadFloat(particleType.getTexture().getNumberOfRows());
		OpenglUtils.bindTextureToBank(particleType.getTexture().getTextureID(), 0);
	}

	private void unbindTexturedModel() {
		GL11.glDepthMask(true);
		OpenglUtils.disableBlending();
		OpenglUtils.unbindVAO(0, 1, 2, 3, 4, 5, 6, 7);
	}

	private void prepareInstance(Particle particle, ICamera camera, float[] vboData) {
		ParticleType particleType = particle.getParticleType();
		Matrix4f viewMatrix = camera.getViewMatrix();
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f modelViewMatrix = new Matrix4f();
		Matrix4f.translate(modelMatrix, particle.getPosition(), modelMatrix);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		Matrix4f.rotate(modelMatrix, new Vector3f(0, 0, 1), (float) Math.toRadians(particleType.getRotation()), modelMatrix);
		Matrix4f.scale(modelMatrix, new Vector3f(particleType.getScale(), particleType.getScale(), particleType.getScale()), modelMatrix);
		//shader.modelMatrix.loadMat4(modelMatrix);
		//shader.transparency.loadFloat(particle.getTransparency());
		//shader.textureOffset1.loadVec2(particle.getTextureOffset1());
		//shader.textureOffset2.loadVec2(particle.getTextureOffset2());
		//shader.textureCoordInfo.loadVec2(particle.getParticleType().getTexture().getNumberOfRows(), particle.getTextureBlendFactor());

		vboData[pointer++] = modelMatrix.m00;
		vboData[pointer++] = modelMatrix.m01;
		vboData[pointer++] = modelMatrix.m02;
		vboData[pointer++] = modelMatrix.m03;
		vboData[pointer++] = modelMatrix.m10;
		vboData[pointer++] = modelMatrix.m11;
		vboData[pointer++] = modelMatrix.m12;
		vboData[pointer++] = modelMatrix.m13;
		vboData[pointer++] = modelMatrix.m20;
		vboData[pointer++] = modelMatrix.m21;
		vboData[pointer++] = modelMatrix.m22;
		vboData[pointer++] = modelMatrix.m23;
		vboData[pointer++] = modelMatrix.m30;
		vboData[pointer++] = modelMatrix.m31;
		vboData[pointer++] = modelMatrix.m32;
		vboData[pointer++] = modelMatrix.m33;
		vboData[pointer++] = particle.getTextureOffset1().x;
		vboData[pointer++] = particle.getTextureOffset1().y;
		vboData[pointer++] = particle.getTextureOffset2().x;
		vboData[pointer++] = particle.getTextureOffset2().y;
		vboData[pointer++] = particle.getTextureBlendFactor();
		vboData[pointer++] = particle.getTransparency();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
