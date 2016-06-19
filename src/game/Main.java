package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.parsing.*;
import flounder.resources.*;
import flounder.sounds.*;

public class Main {
	public static void main(String[] args) {
		Config config = new Config(new MyFile("configs", "settings.conf"));
		MusicPlayer.SOUND_VOLUME = (float) config.getDoubleWithDefault("sound_volume", 0.75f);

		Implementation module = new Implementation(new MainGame(), new MainCamera(), new MainRenderer());
		FlounderEngine engine = new FlounderEngine(module,
				config.getIntWithDefault("width", 1080), config.getIntWithDefault("height", 720), "Flounder Demo",
				config.getBooleanWithDefault("vsync", true),
				config.getBooleanWithDefault("antialias", true), 0, config.getBooleanWithDefault("fullscreen", false)
		);
		// config.getIntWithDefault("fps_target", -1),
		engine.startEngine(null); // FontManager.FFF_FORWARD

		// TODO: Write out variables.
	}
}
