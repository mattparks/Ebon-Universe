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

	private boolean fboLoaded;

	public SkyboxFBO() {
		fboFront = createFBO();
		fboBack = createFBO();

		fboUp = createFBO();
		fboDown = createFBO();

		fboLeft = createFBO();
		fboRight = createFBO();

		fboLoaded = false;
	}

	private FBO createFBO() {
		return FBO.newFBO(RESOLUTION, RESOLUTION).create();
	}

	public FBO getFBO(int fboSide) {
		switch (fboSide) {
			case 0:
				return fboFront;
			case 1:
				return fboBack;
			case 2:
				return fboUp;
			case 3:
				return fboDown;
			case 4:
				return fboLeft;
			case 5:
				return fboRight;
		}

		return fboFront;
	}

	public boolean isFboLoaded() {
		return fboLoaded;
	}

	public void setFboLoaded(boolean fboLoaded) {
		this.fboLoaded = fboLoaded;
	}

	public void dispose() {
		fboFront.delete();
		fboBack.delete();

		fboUp.delete();
		fboDown.delete();

		fboLeft.delete();
		fboRight.delete();
	}
}
