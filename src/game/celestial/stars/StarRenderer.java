package game.celestial.stars;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.shaders.*;
import flounder.textures.*;
import game.*;
import game.celestial.*;
import org.lwjgl.*;

import java.nio.*;
import java.util.*;

import static org.lwjgl.opengl.ARBDrawInstanced.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class StarRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "stars", "starsVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "stars", "starsFragment.glsl");

	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private static final int MAX_INSTANCES = Environment.GALAXY_STARS;
	private static final int INSTANCE_DATA_LENGTH = 19;
	private static final Vector3f REUSABLE_SCALE = new Vector3f();

	private static final Texture STAR_TEXTURE = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "starTexture.png")).clampEdges().create();
	private static final int VAO = FlounderEngine.getLoader().createInterleavedVAO(VERTICES, 2);
	private static final FloatBuffer BUFFER = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	private static final int VBO = FlounderEngine.getLoader().createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

	private Shader shader;
	private int pointer;
	private int rendered;

	public StarRenderer() {
		shader = Shader.newShader("stars").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).createInSecondThread();

		pointer = 0;
		rendered = 0;

		FlounderEngine.getLoader().addInstancedAttribute(VAO, VBO, 1, 4, INSTANCE_DATA_LENGTH, 0);
		FlounderEngine.getLoader().addInstancedAttribute(VAO, VBO, 2, 4, INSTANCE_DATA_LENGTH, 4);
		FlounderEngine.getLoader().addInstancedAttribute(VAO, VBO, 3, 4, INSTANCE_DATA_LENGTH, 8);
		FlounderEngine.getLoader().addInstancedAttribute(VAO, VBO, 4, 4, INSTANCE_DATA_LENGTH, 12);
		FlounderEngine.getLoader().addInstancedAttribute(VAO, VBO, 5, 3, INSTANCE_DATA_LENGTH, 16);
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shader.isLoaded() || Environment.getStars() == null) {
			return;
		}

		prepareRendering(clipPlane, camera);

		List<Star> stars = Environment.getStars().queryInFrustum(new ArrayList<>(), camera.getViewFrustum());

		// FIXME: To many stars in rendered area slows way down.
		// Added to stars first -> last, so no initial reverse needed.
		// ArraySorting.heapSort(stars); // Sorts the list big to small.
		// stars = ArraySorting.quickSort(stars);
		//Collections.reverse(stars); // Reverse as the sorted list should be close(small) -> far(big).

		// Creates the data to be used when rendering.
		float[] vboData = new float[Math.min(stars.size(), MAX_INSTANCES) * INSTANCE_DATA_LENGTH];
		pointer = 0;

		for (Star star : stars) {
		//	FlounderEngine.getAABBs().addAABBRender(star.getAABB());
			prepareInstance(star, camera, vboData);
		}

		// Renders the stars list.
		FlounderEngine.getLoader().updateVBO(VBO, vboData, BUFFER);
		glDrawArraysInstancedARB(GL_TRIANGLE_STRIP, 0, VERTICES.length, stars.size());

		endRendering();
	}

	@Override
	public void profile() {
		FlounderEngine.getProfiler().add("Stars", "Render Time", super.getRenderTimeMs());
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		shader.getUniformMat4("projectionMatrix").loadMat4(FlounderEngine.getProjectionMatrix());
		shader.getUniformMat4("viewMatrix").loadMat4(camera.getViewMatrix());
		shader.getUniformVec4("clipPlane").loadVec4(clipPlane);

		rendered = 0;

		OpenGlUtils.bindVAO(VAO, 0, 1, 2, 3, 4, 5);
		OpenGlUtils.antialias(FlounderEngine.getDevices().getDisplay().isAntialiasing());
		OpenGlUtils.cullBackFaces(true);
		OpenGlUtils.enableDepthTesting();
		OpenGlUtils.enableAlphaBlending();
		glDepthMask(false); // Stops particles from being rendered to the depth BUFFER.

		OpenGlUtils.bindTextureToBank(STAR_TEXTURE.getTextureID(), 0);
	}

	private void prepareInstance(Star star, ICamera camera, float[] vboData) {
		if (rendered >= MAX_INSTANCES) {
			FlounderEngine.getLogger().error("Stars overflow: " + rendered);
			return;
		}

		Matrix4f viewMatrix = camera.getViewMatrix();
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(modelMatrix, star.getPosition(), modelMatrix);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		Matrix4f.scale(modelMatrix, REUSABLE_SCALE.set((float) star.getSolarRadius(), (float) star.getSolarRadius(), (float) star.getSolarRadius()), modelMatrix);

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
		vboData[pointer++] = star.getSurfaceColour().r;
		vboData[pointer++] = star.getSurfaceColour().g;
		vboData[pointer++] = star.getSurfaceColour().b;

		rendered++;
	}

	private void endRendering() {
		glDepthMask(true);
		OpenGlUtils.disableBlending();
		OpenGlUtils.unbindVAO(0, 1, 2, 3, 4, 5);
		shader.stop();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
