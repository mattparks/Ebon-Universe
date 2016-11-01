package ebon.skybox;

import ebon.*;
import ebon.cameras.*;
import flounder.devices.*;
import flounder.textures.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * A object that holds a skybox FBO.
 */
public class SkyboxFBO {
	private static int cubemapSize;
	private Texture cubemapTexture;
	private int fbo;
	private int depthBuffer;

	private boolean textureLoaded;

	/**
	 * Creates a new skybox fbo.
	 */
	protected SkyboxFBO() {
		if (Ebon.configMain != null) {
			cubemapSize = Ebon.configMain.getIntWithDefault("skybox_res", 2048, () -> SkyboxFBO.cubemapSize);
		} else {
			cubemapSize = 2048;
		}

		cubemapTexture = Texture.newEmptyCubeMap(cubemapSize);

		//create fbo
		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);

		//attach depth buffer
		depthBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, cubemapSize, cubemapSize);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);

		// Sets the image to not be loaded.
		textureLoaded = false;
	}

	public int getDepthBuffer() {
		return depthBuffer;
	}

	public Texture getTexture() {
		return cubemapTexture;
	}

	public boolean isLoaded() {
		return textureLoaded;
	}

	public void setLoaded(boolean fboLoaded) {
		this.textureLoaded = fboLoaded;
	}

	/**
	 * Binds the cubemap FBO.
	 */
	public void bindFBO() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glViewport(0, 0, cubemapSize, cubemapSize);
	}

	/**
	 * Binds the i'th face of the cube texture as target to colour attachment 0.
	 *
	 * @param face The face to bind to.
	 */
	public void bindFace(int face, CameraCubeMap camera) {
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, cubemapTexture.getTextureID(), 0);
		camera.switchToFace(face);
	}

	/**
	 * Unbinds the cubemap FBO.
	 */
	public void unbindFBO() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, FlounderDisplay.getWidth(), FlounderDisplay.getHeight());
	}

	/**
	 * Deletes the FBO from memory.
	 */
	public void dispose() {
		glDeleteRenderbuffers(depthBuffer);
		glDeleteFramebuffers(fbo);
		cubemapTexture.delete();
	}
}
