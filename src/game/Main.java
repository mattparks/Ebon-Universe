package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.sounds.*;
import game.cameras.*;

public class Main {
	public static void main(String[] args) {
		MusicPlayer.SOUND_VOLUME = (float) MainGame.CONFIG.getDoubleWithDefault("sound_volume", 0.75f, () -> MusicPlayer.SOUND_VOLUME);

		Implementation implementation = new Implementation(
				new MainGame(),
				new CameraFocus(),
				new MainRenderer(),
				new MainGuis(),
				MainGame.CONFIG.getIntWithDefault("fps_target", -1, FlounderEngine::getTargetFPS)
		);
		FlounderEngine engine = new FlounderEngine(implementation,
				MainGame.CONFIG.getIntWithDefault("width", 1080, () -> FlounderEngine.getDevices().getDisplay().getWidth()),
				MainGame.CONFIG.getIntWithDefault("height", 720, () -> FlounderEngine.getDevices().getDisplay().getHeight()),
				"4Space Game",
				MainGame.CONFIG.getBooleanWithDefault("vsync", true, () -> FlounderEngine.getDevices().getDisplay().isVSync()),
				MainGame.CONFIG.getBooleanWithDefault("antialias", true, () -> FlounderEngine.getDevices().getDisplay().isAntialiasing()),
				4,
				MainGame.CONFIG.getBooleanWithDefault("fullscreen", false, () -> FlounderEngine.getDevices().getDisplay().isFullscreen())
		);
		engine.startEngine(FlounderEngine.getFonts().fffForward);
		System.exit(1);
	}
}
