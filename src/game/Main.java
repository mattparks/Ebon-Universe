package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.parsing.*;
import flounder.resources.*;
import flounder.sounds.*;

public class Main {
	public static final Config CONFIG = new Config(new MyFile("configs", "settings.conf"));

	public static void main(String[] args) {
		MusicPlayer.SOUND_VOLUME = (float) CONFIG.getDoubleWithDefault("sound_volume", 0.75f);

		Implementation implementation = new Implementation(new MainGame(), new MainCamera(), new MainRenderer(), CONFIG.getIntWithDefault("fps_target", -1));
		FlounderEngine engine = new FlounderEngine(implementation,
				CONFIG.getIntWithDefault("width", 1080), CONFIG.getIntWithDefault("height", 720), "4Space Game",
				CONFIG.getBooleanWithDefault("vsync", true),
				CONFIG.getBooleanWithDefault("antialias", true), 0, CONFIG.getBooleanWithDefault("fullscreen", false)
		);
		engine.startEngine(FlounderEngine.getFonts().fffForward);

		// TODO: Write out variables.
	}
}
