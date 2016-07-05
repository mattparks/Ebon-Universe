package game.options;

import game.*;
import game.post.*;

/**
 * Holds basic graphical engine settings.
 */
public class OptionsPost {
	public static boolean POST_ENABLED = MainGame.POST_CONFIG.getBooleanWithDefault("post_enabled", true, () -> OptionsPost.POST_ENABLED);
	public static int POST_EFFECT = MainGame.POST_CONFIG.getIntWithDefault("post_selected", 0, () -> OptionsPost.POST_EFFECT);
	public static boolean FILTER_FXAA = MainGame.POST_CONFIG.getBooleanWithDefault("filter_fxaa_enabled", false, () -> OptionsPost.FILTER_FXAA);
	public static int POST_EFFECT_MAX = PipelineDemo.TOTAL_EFFECTS;
}
