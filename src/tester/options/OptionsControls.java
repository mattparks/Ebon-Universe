package tester.options;

import tester.*;

public class OptionsControls {
	private static Joysticks JOYSTICK_TYPE = Joysticks.XBOX360;
	public static int JOYSTICK_PORT = FlounderTester.configControls.getIntWithDefault("joystick_port", 0, () -> OptionsControls.JOYSTICK_PORT);

	public static int JOYSTICK_GUI_TOGGLE =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_guiToggle", 7, () -> OptionsControls.JOYSTICK_GUI_TOGGLE) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_guiToggle", 9, () -> OptionsControls.JOYSTICK_GUI_TOGGLE) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_guiToggle", 7, () -> OptionsControls.JOYSTICK_GUI_TOGGLE) : 0;
	public static int JOYSTICK_GUI_LEFT =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_guiLeft", 0, () -> OptionsControls.JOYSTICK_GUI_LEFT) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_guiLeft", 0, () -> OptionsControls.JOYSTICK_GUI_LEFT) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_guiLeft", 2, () -> OptionsControls.JOYSTICK_GUI_LEFT) : 0;
	public static int JOYSTICK_GUI_RIGHT =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_guiRight", 1, () -> OptionsControls.JOYSTICK_GUI_RIGHT) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_guiRight", 3, () -> OptionsControls.JOYSTICK_GUI_RIGHT) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_guiRight", 3, () -> OptionsControls.JOYSTICK_GUI_RIGHT) : 0;
	public static int JOYSTICK_AXIS_X =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_axisX", 0, () -> OptionsControls.JOYSTICK_AXIS_X) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_axisX", 0, () -> OptionsControls.JOYSTICK_AXIS_X) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_axisX", 0, () -> OptionsControls.JOYSTICK_AXIS_X) : 0;
	public static int JOYSTICK_AXIS_Y =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_axisY", 1, () -> OptionsControls.JOYSTICK_AXIS_Y) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_axisY", 1, () -> OptionsControls.JOYSTICK_AXIS_Y) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_axisY", 1, () -> OptionsControls.JOYSTICK_AXIS_Y) : 0;
	public static int JOYSTICK_ROTATE_X =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_rotateX", 2, () -> OptionsControls.JOYSTICK_ROTATE_X) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_axisX", 0, () -> OptionsControls.JOYSTICK_ROTATE_X) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_axisX", 0, () -> OptionsControls.JOYSTICK_ROTATE_X) : 0;
	public static int JOYSTICK_ROTATE_Y =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_rotateY", 3, () -> OptionsControls.JOYSTICK_ROTATE_Y) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_axisY", 1, () -> OptionsControls.JOYSTICK_ROTATE_Y) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_axisY", 1, () -> OptionsControls.JOYSTICK_ROTATE_Y) : 0;
	public static int JOYSTICK_CAMERA_UP =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_camera_up", 3, () -> OptionsControls.JOYSTICK_CAMERA_UP) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_camera_up", 0, () -> OptionsControls.JOYSTICK_CAMERA_UP) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_camera_up", 0, () -> OptionsControls.JOYSTICK_CAMERA_UP) : 0;
	public static int JOYSTICK_CAMERA_DOWN =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_camera_down", 2, () -> OptionsControls.JOYSTICK_CAMERA_DOWN) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_camera_down", 0, () -> OptionsControls.JOYSTICK_CAMERA_DOWN) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_camera_down", 0, () -> OptionsControls.JOYSTICK_CAMERA_DOWN) : 0;
	public static int JOYSTICK_CAMERA_SWITCH =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_axisY_zoom_in", 6, () -> OptionsControls.JOYSTICK_CAMERA_SWITCH) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_zoom_in", 0, () -> OptionsControls.JOYSTICK_CAMERA_SWITCH) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_zoom_in", 0, () -> OptionsControls.JOYSTICK_CAMERA_SWITCH) : 0;
	public static int JOYSTICK_CAMERA_ZOOM_IN =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_zoom_in", 4, () -> OptionsControls.JOYSTICK_CAMERA_ZOOM_IN) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_zoom_in", 1, () -> OptionsControls.JOYSTICK_CAMERA_ZOOM_IN) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_zoom_in", 5, () -> OptionsControls.JOYSTICK_CAMERA_ZOOM_IN) : 0;
	public static int JOYSTICK_CAMERA_ZOOM_OUT =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_zoom_out", 5, () -> OptionsControls.JOYSTICK_CAMERA_ZOOM_OUT) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_zoom_out", 1, () -> OptionsControls.JOYSTICK_CAMERA_ZOOM_OUT) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_zoom_out", 4, () -> OptionsControls.JOYSTICK_CAMERA_ZOOM_OUT) : 0;
	public static int JOYSTICK_MUSIC_PAUSE =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_musicPause", 8, () -> OptionsControls.JOYSTICK_MUSIC_PAUSE) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_musicPause", 2, () -> OptionsControls.JOYSTICK_MUSIC_PAUSE) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_musicPause", 3, () -> OptionsControls.JOYSTICK_MUSIC_PAUSE) : 0;
	public static int JOYSTICK_MUSIC_SKIP =
			JOYSTICK_TYPE == Joysticks.XBOX360 ? FlounderTester.configControls.getIntWithDefault("xbox_musicSkip", 9, () -> OptionsControls.JOYSTICK_MUSIC_SKIP) :
					JOYSTICK_TYPE == Joysticks.F310 ? FlounderTester.configControls.getIntWithDefault("f310_musicSkip", 1, () -> OptionsControls.JOYSTICK_MUSIC_SKIP) :
							JOYSTICK_TYPE == Joysticks.LOGITECH3D ? FlounderTester.configControls.getIntWithDefault("logitech3d_musicSkip", 0, () -> OptionsControls.JOYSTICK_MUSIC_SKIP) : 0;

	public enum Joysticks {
		F310, XBOX360, LOGITECH3D
	}
}
