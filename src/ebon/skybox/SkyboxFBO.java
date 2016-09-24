package ebon.skybox;

import ebon.*;
import flounder.engine.*;

import java.nio.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * A object that holds a skybox FBO.
 */
public class SkyboxFBO {
	public static final int FBO_SIZE = Ebon.configMain.getIntWithDefault("skybox_res", 2048, () -> SkyboxFBO.FBO_SIZE);
	public static final float CAMERA_FOV = 90.0f;
	public static final float CAMERA_NEAR = 0.1f;
	public static final float CAMERA_FAR = (float) Environment.getGalaxyManager().GALAXY_RADIUS * 100.0f;

	private int cubemapFBO;
	private int cubemapColour;
	private int cubemapDepth;

	private boolean textureLoaded;

	/**
	 * Creates a new skybox fbo.
	 */
	protected SkyboxFBO() {
		// Create the cubemap FBO.
		cubemapFBO = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, cubemapFBO);

		// Create the colour cubemap.
		cubemapColour = glGenTextures();
		glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapColour);
		for (int face = 0; face < 6; face++) {
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, 0, GL_RGBA, FBO_SIZE, FBO_SIZE, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		}
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_CUBE_MAP_POSITIVE_X, cubemapColour, 0);

		// Create the uniform depth buffer.
		cubemapDepth = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, cubemapDepth);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, FBO_SIZE, FBO_SIZE);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, cubemapFBO);

		// Disable the newly create FBO.
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

		// Sets the image to not be loaded.
		textureLoaded = false;
	}

	public int getDepthAttachment() {
		return cubemapDepth;
	}

	public int getColourAttachment() {
		return cubemapColour;
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
		glBindFramebuffer(GL_FRAMEBUFFER, cubemapFBO);
		glViewport(0, 0, FBO_SIZE, FBO_SIZE);
	}

	/**
	 * Binds the i'th face of the cube texture as target to colour attachment 0.
	 *
	 * @param face The face to bind to.
	 */
	public void bindFace(int face) {
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, cubemapColour, 0);
	}

	/**
	 * Unbinds the cubemap FBO.
	 */
	public void unbindFBO() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, FlounderEngine.getDevices().getDisplay().getWidth(), FlounderEngine.getDevices().getDisplay().getHeight());
	}

	/**
	 * Deletes the FBO from memory.
	 */
	public void dispose() {
		glDeleteFramebuffers(cubemapFBO);
		glDeleteTextures(cubemapColour);
		glDeleteRenderbuffers(cubemapDepth);
	}
}
