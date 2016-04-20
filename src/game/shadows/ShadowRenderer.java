package game.shadows;

import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.textures.fbos.*;
import game.blocks.*;
import game.models.*;
import game.world.*;

/**
 * Deals with the shadow renderObjects pass.
 */
public class ShadowRenderer extends IRenderer {
	public static final int SHADOW_MAP_SIZE = 4092;

	private Matrix4f projectionMatrix;
	private Matrix4f lightViewMatrix;
	private Matrix4f projectionViewMatrix;
	private Matrix4f offset;

	private FBO shadowFbo;
	private ShadowShader shader;
	private ShadowBox shadowBox;

	public ShadowRenderer() {
		projectionMatrix = new Matrix4f();
		lightViewMatrix = new Matrix4f();
		projectionViewMatrix = new Matrix4f();
		offset = createOffset();

		shadowFbo = FBO.newFBO(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE).noColourBuffer().depthBuffer(FBOBuilder.DepthBufferType.TEXTURE).create();
		shader = new ShadowShader();
		shadowBox = new ShadowBox(lightViewMatrix);
	}

	/**
	 * Create the offset for part of the conversion to shadow map space.
	 *
	 * @return The offset as a matrix.
	 */
	private static Matrix4f createOffset() {
		Matrix4f offset = new Matrix4f();
		Matrix4f.translate(offset, new Vector3f(0.5f, 0.5f, 0.5f), offset);
		Matrix4f.scale(offset, new Vector3f(0.5f, 0.5f, 0.5f), offset);
		return offset;
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		prepareRendering(clipPlane, camera);

		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shadowBox.update(camera);
		Vector3f lightDirection = Environment.getSun().getPosition();

		updateOrthographicProjectionMatrix(shadowBox.getWidth(), shadowBox.getHeight(), shadowBox.getLength());
		updateLightViewMatrix(lightDirection, shadowBox.getCenter());
		Matrix4f.multiply(projectionMatrix, lightViewMatrix, projectionViewMatrix);

		shadowFbo.bindFrameBuffer();
		OpenglUtils.prepareNewRenderParse(new Colour(1, 1, 1));
		shader.start();
	}

	/**
	 * Creates the orthographic projection matrix.
	 *
	 * @param width Shadow box width.
	 * @param height Shadow box height.
	 * @param length Shadow box length.
	 */
	private void updateOrthographicProjectionMatrix(float width, float height, float length) {
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = 2f / width;
		projectionMatrix.m11 = 2f / height;
		projectionMatrix.m22 = -2f / length;
		projectionMatrix.m33 = 1;
	}

	/**
	 * Updates the "view" matrix of the light. The light itself has no position, so the "view" matrix is centered at the center of the shadow box.
	 *
	 * @param direction The light direct.
	 * @param position The center of the shadow box.
	 */
	private void updateLightViewMatrix(Vector3f direction, Vector3f position) {
		direction.normalize();
		position.negate();
		lightViewMatrix.setIdentity();
		float h = new Vector2f(direction.x, direction.z).length();
		float pitch = (float) Math.acos(h);
		Matrix4f.rotate(lightViewMatrix, new Vector3f(1, 0, 0), pitch, lightViewMatrix);
		float yaw = (float) Math.toDegrees((float) Math.atan(direction.x / direction.z));

		if (direction.z > 0) {
			yaw -= 180;
		}

		Matrix4f.rotate(lightViewMatrix, new Vector3f(0, 1, 0), (float) -Math.toRadians(yaw), lightViewMatrix);
		Matrix4f.translate(lightViewMatrix, position, lightViewMatrix);
	}

	private void endRendering() {
		shader.stop();
		shadowFbo.unbindFrameBuffer();
	}

	/**
	 * @return The ID of the shadow map texture.
	 */
	public int getShadowMap() {
		return shadowFbo.getDepthTexture();
	}

	public float getShadowDistance() {
		return shadowBox.getShadowDistance();
	}

	/**
	 * @return The shadow box, so that it can be used by other class to test if engine.entities are inside the box.
	 */
	public ShadowBox getShadowBox() {
		return shadowBox;
	}

	/**
	 * This biased projection-view matrix is used to convert fragments into "shadow map space" when rendering the main renderObjects pass.
	 *
	 * @return The to-shadow-map-space matrix.
	 */
	public Matrix4f getToShadowMapSpaceMatrix() {
		return Matrix4f.multiply(offset, projectionViewMatrix, null);
	}

	/**
	 * @return The light's "view" matrix.
	 */
	protected Matrix4f getLightSpaceTransform() {
		return lightViewMatrix;
	}

	@Override
	public void dispose() {
		shader.dispose();
		shadowFbo.delete();
	}
}
