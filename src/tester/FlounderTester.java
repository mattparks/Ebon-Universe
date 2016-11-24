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

		configMain = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "settings.conf"));
		configPost = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "post.conf"));
		configControls = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "controls_joystick.conf"));

		FlounderDisplay.setup(configMain.getIntWithDefault("width", 1080, FlounderDisplay::getWindowWidth),
				configMain.getIntWithDefault("height", 720, FlounderDisplay::getWindowHeight),
				"Ebon Universe", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "icon.png")},
				configMain.getBooleanWithDefault("vsync", false, FlounderDisplay::isVSync),
				configMain.getBooleanWithDefault("antialias", true, FlounderDisplay::isAntialiasing),
				configMain.getIntWithDefault("sampled", 0, FlounderDisplay::getSamples),
				configMain.getBooleanWithDefault("fullscreen", false, FlounderDisplay::isFullscreen)
		);
		setFpsLimit(configMain.getIntWithDefault("fps_limit", -1, FlounderFramework::getFpsLimit));

		TextBuilder.DEFAULT_TYPE = FlounderFonts.FFF_FORWARD;
		MusicPlayer.SOUND_VOLUME = (float) configMain.getDoubleWithDefault("sound_volume", 0.75f, () -> MusicPlayer.SOUND_VOLUME);
	}

	public void closeConfigs() {
		configMain.dispose();
		configPost.dispose();
		configControls.dispose();
	}
}
