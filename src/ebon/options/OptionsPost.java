package ebon.options;

import ebon.*;
import ebon.post.*;

/**
 * Holds basic graphical engine settings.
 */
public class OptionsPost {
	public static boolean POST_ENABLED = Ebon.configPost.getBooleanWithDefault("post_enabled", true, () -> OptionsPost.POST_ENABLED);
	public static int POST_EFFECT = Ebon.configPost.getIntWithDefault("post_selected", 0, () -> OptionsPost.POST_EFFECT);
	public static boolean FILTER_FXAA = Ebon.configPost.getBooleanWithDefault("filter_fxaa_enabled", false, () -> OptionsPost.FILTER_FXAA);
	public static int POST_EFFECT_MAX = PipelineDemo.TOTAL_EFFECTS;
}
