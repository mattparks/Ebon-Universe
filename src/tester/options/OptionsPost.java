package tester.options;

import tester.*;
import tester.post.*;

/**
 * Holds basic graphical engine settings.
 */
public class OptionsPost {
	public static boolean POST_ENABLED = FlounderTester.configPost.getBooleanWithDefault("post_enabled", true, () -> OptionsPost.POST_ENABLED);
	public static int POST_EFFECT = FlounderTester.configPost.getIntWithDefault("post_selected", 0, () -> OptionsPost.POST_EFFECT);
	public static boolean FILTER_FXAA = FlounderTester.configPost.getBooleanWithDefault("filter_fxaa_enabled", false, () -> OptionsPost.FILTER_FXAA);
	public static int POST_EFFECT_MAX = PipelineDemo.TOTAL_EFFECTS;
}
