package game.options;

import flounder.parsing.*;
import flounder.resources.*;
import game.post.*;

/**
 * Holds basic graphical engine settings.
 */
public class OptionsPost {
	private static final Config POST_CONFIG = new Config(new MyFile("configs", "post.conf"));

	public static boolean POST_ENABLED = POST_CONFIG.getBooleanWithDefault("post_enabled", true);
	public static int POST_EFFECT = POST_CONFIG.getIntWithDefault("post_selected", 0);
	public static int POST_EFFECT_MAX = PipelineDemo.TOTAL_EFFECTS;
}
