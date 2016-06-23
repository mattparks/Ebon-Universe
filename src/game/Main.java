package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.parsing.*;
import flounder.resources.*;
import flounder.sounds.*;

public class Main {
	public static final Config CONFIG = new Config(new MyFile("configs", "settings.conf"));
	public static final Config POST_CONFIG = new Config(new MyFile("configs", "post.conf"));

	public static void main(String[] args) {
		MusicPlayer.SOUND_VOLUME = (float) CONFIG.getDoubleWithDefault("sound_volume", 0.75f, () -> ("" + MusicPlayer.SOUND_VOLUME));

		Implementation implementation = new Implementation(
				new MainGame(),
				new MainCamera(),
				new MainRenderer(),
				CONFIG.getIntWithDefault("fps_target", -1, () -> ("" + FlounderEngine.getTargetFPS()))
		);
		FlounderEngine engine = new FlounderEngine(implementation,
				CONFIG.getIntWithDefault("width", 1080, () -> ("" + FlounderEngine.getDevices().getDisplay().getWidth())),
				CONFIG.getIntWithDefault("height", 720, () -> ("" + FlounderEngine.getDevices().getDisplay().getHeight())),
				"4Space Game",
				CONFIG.getBooleanWithDefault("vsync", true, () -> ("" + FlounderEngine.getDevices().getDisplay().isVSync())),
				CONFIG.getBooleanWithDefault("antialias", true, () -> ("" + FlounderEngine.getDevices().getDisplay().isAntialiasing())),
				0,
				CONFIG.getBooleanWithDefault("fullscreen", false, () -> ("" + FlounderEngine.getDevices().getDisplay().isFullscreen()))
		);
		engine.startEngine(FlounderEngine.getFonts().fffForward);

		CONFIG.dispose();
		POST_CONFIG.dispose();
	}
}
