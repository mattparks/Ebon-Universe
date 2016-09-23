package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.resources.*;
import flounder.sounds.*;
import game.cameras.*;

public class Main {
	public static void main(String[] args) {
		FlounderEngine.loadEngineStatics("Ebon Universe");
		MyFile[] displayIcons = new MyFile[]{new MyFile(MyFile.RES_FOLDER, "icon.png")};
		MusicPlayer.SOUND_VOLUME = (float) MainGame.CONFIG.getDoubleWithDefault("sound_volume", 0.75f, () -> MusicPlayer.SOUND_VOLUME);

		Implementation implementation = new Implementation(
				new MainGame(),
				new CameraFPS(),
				new MainRenderer(),
				new MainGuis(),
				MainGame.CONFIG.getIntWithDefault("fps_target", 60, FlounderEngine::getTargetFPS)
		);
		FlounderEngine engine = new FlounderEngine(implementation,
				MainGame.CONFIG.getIntWithDefault("width", 1080, () -> FlounderEngine.getDevices().getDisplay().getWindowWidth()),
				MainGame.CONFIG.getIntWithDefault("height", 720, () -> FlounderEngine.getDevices().getDisplay().getWindowHeight()),
				"Ebon Universe", displayIcons,
				MainGame.CONFIG.getBooleanWithDefault("vsync", true, () -> FlounderEngine.getDevices().getDisplay().isVSync()),
				MainGame.CONFIG.getBooleanWithDefault("antialias", true, () -> FlounderEngine.getDevices().getDisplay().isAntialiasing()),
				MainGame.CONFIG.getIntWithDefault("msaa_samples", 4, () -> FlounderEngine.getDevices().getDisplay().getSamples()),
				MainGame.CONFIG.getBooleanWithDefault("fullscreen", false, () -> FlounderEngine.getDevices().getDisplay().isFullscreen())
		);
		engine.startEngine(FlounderEngine.getFonts().fffForward);
		System.exit(1);
	}
}
