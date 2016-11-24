package tester;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.parsing.*;
import flounder.resources.*;
import flounder.sounds.*;

public class FlounderTester extends FlounderFramework {
	public static void main(String[] args) {
		FlounderTester founderTester = new FlounderTester();
		founderTester.run();
		founderTester.closeConfigs();
		System.exit(0);
	}

	public static Config configMain;
	public static Config configPost;
	public static Config configControls;

	public FlounderTester() {
		super("FlounderTester", -1, new ExtensionRenderer(), new ExtensionGuis(), new ExtensionCamera());
		FlounderDisplay.setup(1080, 720, "Hello World", new MyFile[0], false, true, 4, false);
		TextBuilder.DEFAULT_TYPE = FlounderFonts.FFF_FORWARD;
		configMain = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "settings.conf"));
		configPost = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "post.conf"));
		configControls = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "controls_joystick.conf"));
		MusicPlayer.SOUND_VOLUME = (float) configMain.getDoubleWithDefault("sound_volume", 0.75f, () -> MusicPlayer.SOUND_VOLUME);
	}

	public void closeConfigs() {
		configMain.dispose();
		configPost.dispose();
		configControls.dispose();
	}
}
