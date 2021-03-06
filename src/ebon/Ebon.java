package ebon;

import ebon.cameras.*;
import ebon.players.*;
import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.parsing.*;
import flounder.resources.*;
import flounder.sounds.*;

public class Ebon extends FlounderFramework {
	public static void main(String[] args) {
		Ebon ebon = new Ebon();
		ebon.run();
		System.exit(0);
	}

	public static Config configMain;
	public static Config configPost;
	public static Config configControls;

	public Ebon() {
		super("Ebon Universe", -1, new EbonRenderer(), new CameraFocus(), new PlayerFocus(), new EbonInterface(), new EbonGuis());

		configMain = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "settings.conf"));
		configPost = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "post.conf"));
		configControls = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "controls_joystick.conf"));

		FlounderDisplay.setup(configMain.getIntWithDefault("width", 1080, FlounderDisplay::getWindowWidth),
				configMain.getIntWithDefault("height", 720, FlounderDisplay::getWindowHeight),
				"Ebon Universe", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "icon.png")},
				configMain.getBooleanWithDefault("vsync", false, FlounderDisplay::isVSync),
				configMain.getBooleanWithDefault("antialias", true, FlounderDisplay::isAntialiasing),
				configMain.getIntWithDefault("sampled", 0, FlounderDisplay::getSamples),
				configMain.getBooleanWithDefault("fullscreen", false, FlounderDisplay::isFullscreen),
				false
		);
		setFpsLimit(configMain.getIntWithDefault("fps_limit", -1, FlounderFramework::getFpsLimit));

		TextBuilder.DEFAULT_TYPE = FlounderFonts.FFF_FORWARD;
		MusicPlayer.SOUND_VOLUME = (float) configMain.getDoubleWithDefault("sound_volume", 0.75f, () -> MusicPlayer.SOUND_VOLUME);
	}

	protected static void closeConfigs() {
		configMain.dispose();
		configPost.dispose();
		configControls.dispose();
	}
}
