package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.parsing.*;
import flounder.resources.*;
import flounder.sounds.*;
import game.options.*;

public class Main {
	public static final Config CONFIG = new Config(new MyFile("configs", "settings.conf"));
	public static final Config POST_CONFIG = new Config(new MyFile("configs", "post.conf"));

	public static void main(String[] args) {
		MusicPlayer.SOUND_VOLUME = (float) CONFIG.getDoubleWithDefault("sound_volume", 0.75f);

		Implementation implementation = new Implementation(new MainGame(), new MainCamera(), new MainRenderer(), CONFIG.getIntWithDefault("fps_target", -1));
		FlounderEngine engine = new FlounderEngine(implementation,
				CONFIG.getIntWithDefault("width", 1080), CONFIG.getIntWithDefault("height", 720), "4Space Game",
				CONFIG.getBooleanWithDefault("vsync", true),
				CONFIG.getBooleanWithDefault("antialias", true), 0, CONFIG.getBooleanWithDefault("fullscreen", false)
		);
		engine.startEngine(FlounderEngine.getFonts().fffForward);

		CONFIG.setValue("sound_volume", MusicPlayer.SOUND_VOLUME);
		if (!FlounderEngine.getDevices().getDisplay().isFullscreen()) {
			CONFIG.setValue("width", FlounderEngine.getDevices().getDisplay().getWidth());
			CONFIG.setValue("height", FlounderEngine.getDevices().getDisplay().getHeight());
		}
		CONFIG.setValue("vsync", FlounderEngine.getDevices().getDisplay().isVSync());
		CONFIG.setValue("antialias", FlounderEngine.getDevices().getDisplay().isAntialiasing());
		CONFIG.setValue("fullscreen", FlounderEngine.getDevices().getDisplay().isFullscreen());
		CONFIG.dispose();

		POST_CONFIG.setValue("post_enabled", OptionsPost.POST_ENABLED);
		POST_CONFIG.setValue("post_selected", OptionsPost.POST_EFFECT);
		POST_CONFIG.setValue("filter_fxaa_enabled", OptionsPost.FILTER_FXAA);
		POST_CONFIG.dispose();
	}
}
