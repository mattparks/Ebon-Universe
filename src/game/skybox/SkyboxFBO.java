package game.skybox;

import flounder.engine.*;

import java.nio.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

public class SkyboxFBO {
	private int skyboxWidth;
	private int skyboxHeight;

	private int cubemapFBO;
	private int cubemapColour;
	private int cubemapDepth;

	private boolean fboRendered;

	public SkyboxFBO(int width, int height) {
		this.skyboxWidth = width;
		this.skyboxHeight = height;

		// Create the cubemap FBO.
		cubemapFBO = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, cubemapFBO);

		// Create the colour cubemap.
		cubemapColour = glGenTextures();
		glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapColour);
		for (int face = 0; face < 6; face++) {
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, 0, GL_RGBA, skyboxWidth, skyboxHeight, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		}
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_CUBE_MAP_POSITIVE_X, cubemapColour, 0);

		// Create the uniform depth buffer.
		cubemapDepth = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, cubemapDepth);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, skyboxWidth, skyboxHeight);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, cubemapFBO);

		// Disable the newly create FBO.
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

		// Sets the image to not be loaded.
		fboRendered = false;
	}

	public int getDepthAttachment() {
		return cubemapDepth;
	}

	public int getColourAttachment() {
		return cubemapColour;
	}

	public boolean isLoaded() {
		return fboRendered;
	}

	public void setLoaded(boolean fboLoaded) {
		this.fboRendered = fboLoaded;
	}

	public float getAspectRatio() {
		return skyboxWidth / skyboxHeight;
	}

	public void bindFBO() {
		// Bind the FBO
		glBindFramebuffer(GL_FRAMEBUFFER, cubemapFBO);
		glViewport(0, 0, skyboxWidth, skyboxHeight);
	}

	/**
	 * Binds the i'th face of the cube texture as target to colour attachment 0.
	 *
	 * @param face The face to bind to.
	 */
	public void bindFace(int face) {
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, cubemapColour, 0);
	}

	public void unbindFBO() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, FlounderEngine.getDevices().getDisplay().getWidth(), FlounderEngine.getDevices().getDisplay().getHeight());
	}

	public void dispose() {
		glDeleteFramebuffers(cubemapFBO);
		glDeleteTextures(cubemapColour);
		glDeleteRenderbuffers(cubemapDepth);
		cubemapFBO = 0;
	}
}
