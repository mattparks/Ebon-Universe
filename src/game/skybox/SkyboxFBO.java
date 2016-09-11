package game.skybox;

import flounder.fbos.*;

public class SkyboxFBO {
	private static final int RESOLUTION = 512;

	private FBO fboFront;
	private FBO fboBack;

	private FBO fboUp;
	private FBO fboDown;

	private FBO fboLeft;
	private FBO fboRight;

	public SkyboxFBO() {
		fboFront = createFBO();
		fboBack = createFBO();

		fboUp = createFBO();
		fboDown = createFBO();

		fboLeft = createFBO();
		fboRight = createFBO();
	}

	private FBO createFBO() {
		return FBO.newFBO(RESOLUTION, RESOLUTION).create();
	}
}
