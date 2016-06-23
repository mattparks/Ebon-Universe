package game.options;

import flounder.parsing.*;
import flounder.resources.*;
import game.*;
import game.post.*;

/**
 * Holds basic graphical engine settings.
 */
public class OptionsPost {
	public static boolean POST_ENABLED = Main.POST_CONFIG.getBooleanWithDefault("post_enabled", true);
	public static int POST_EFFECT = Main.POST_CONFIG.getIntWithDefault("post_selected", 0);
	public static boolean FILTER_FXAA = Main.POST_CONFIG.getBooleanWithDefault("filter_fxaa_enabled", false);
	public static int POST_EFFECT_MAX = PipelineDemo.TOTAL_EFFECTS;
}
