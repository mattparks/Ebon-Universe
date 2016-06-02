package game.options;

import flounder.parsing.*;
import flounder.resources.*;

public class OptionsControls {
	private static final Config CONTROLS_CONFIG = new Config(new MyFile("configs", "controls_joystick.conf"));

	private static Joysticks JOYSTICK_TYPE = Joysticks.XBOX360;
	public static int JOYSTICK_PORT = CONTROLS_CONFIG.getIntWithDefault("joystick_port", 0);

	public static int JOYSTICK_GUI_TOGGLE =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? CONTROLS_CONFIG.getIntWithDefault("xbox_guiToggle", 7) :
					JOYSTICK_TYPE == Joysticks.F310 ? CONTROLS_CONFIG.getIntWithDefault("f310_guiToggle", 9) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? CONTROLS_CONFIG.getIntWithDefault("logitech3d_guiToggle", 7) : 0;
	public static int JOYSTICK_GUI_LEFT =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? CONTROLS_CONFIG.getIntWithDefault("xbox_guiLeft", 2) :
					JOYSTICK_TYPE == Joysticks.F310 ? CONTROLS_CONFIG.getIntWithDefault("f310_guiLeft", 0) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? CONTROLS_CONFIG.getIntWithDefault("logitech3d_guiLeft", 2) : 0;
	public static int JOYSTICK_GUI_RIGHT =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? CONTROLS_CONFIG.getIntWithDefault("xbox_guiRight", 3) :
					JOYSTICK_TYPE == Joysticks.F310 ? CONTROLS_CONFIG.getIntWithDefault("f310_guiRight", 3) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? CONTROLS_CONFIG.getIntWithDefault("logitech3d_guiRight", 3) : 0;
	public static int JOYSTICK_AXIS_X =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? CONTROLS_CONFIG.getIntWithDefault("xbox_axisX", 0) :
					JOYSTICK_TYPE == Joysticks.F310 ? CONTROLS_CONFIG.getIntWithDefault("f310_axisX", 0) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? CONTROLS_CONFIG.getIntWithDefault("logitech3d_axisX", 0) : 0;
	public static int JOYSTICK_AXIS_Y =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? CONTROLS_CONFIG.getIntWithDefault("xbox_axisY", 1) :
					JOYSTICK_TYPE == Joysticks.F310 ? CONTROLS_CONFIG.getIntWithDefault("f310_axisY", 1) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? CONTROLS_CONFIG.getIntWithDefault("logitech3d_axisY", 1) : 0;
	public static int JOYSTICK_MUSIC_PAUSE =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? CONTROLS_CONFIG.getIntWithDefault("xbox_musicPause", 1) :
					JOYSTICK_TYPE == Joysticks.F310 ? CONTROLS_CONFIG.getIntWithDefault("f310_musicPause", 2) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? CONTROLS_CONFIG.getIntWithDefault("logitech3d_musicPause", 1) : 0;
	public static int JOYSTICK_MUSIC_SKIP =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? CONTROLS_CONFIG.getIntWithDefault("xbox_musicSkip", 0) :
					JOYSTICK_TYPE == Joysticks.F310 ? CONTROLS_CONFIG.getIntWithDefault("f310_musicSkip", 1) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? CONTROLS_CONFIG.getIntWithDefault("logitech3d_musicSkip", 0) : 0;

	public enum Joysticks {
		F310, XBOX360, LOGITECH3D
	}
}
