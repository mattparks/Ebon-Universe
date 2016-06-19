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

		Implementation implementation = new Implementation(new MainGame(), new MainCamera(), new MainRenderer(), config.getIntWithDefault("fps_target", -1));
		FlounderEngine engine = new FlounderEngine(implementation,
				config.getIntWithDefault("width", 1080), config.getIntWithDefault("height", 720), "4Space Game",
				config.getBooleanWithDefault("vsync", true),
				config.getBooleanWithDefault("antialias", true), 0, config.getBooleanWithDefault("fullscreen", false)
		);
		engine.startEngine(FlounderEngine.getFonts().FFF_FORWARD); // FlounderFonts.FFF_FORWARD

		// TODO: Write out variables.
	}
}
