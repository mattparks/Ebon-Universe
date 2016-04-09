package game.options;

public class OptionsControls {
	public static Joysticks JOYSTICK_TYPE = Joysticks.F310;
	public static int JOYSTICK_PORT = 0;
	public static int JOYSTICK_GUI_TOGGLE = JOYSTICK_TYPE == Joysticks.XBOX360 ? 7 : JOYSTICK_TYPE == Joysticks.F310 ? 9 : 7;
	public static int JOYSTICK_GUI_LEFT = JOYSTICK_TYPE == Joysticks.XBOX360 ? 0 : JOYSTICK_TYPE == Joysticks.F310 ? 0 : 3;
	public static int JOYSTICK_GUI_RIGHT = JOYSTICK_TYPE == Joysticks.XBOX360 ? 1 : JOYSTICK_TYPE == Joysticks.F310 ? 3 : 4;
	public static int JOYSTICK_AXIS_X = JOYSTICK_TYPE == Joysticks.XBOX360 ? 0 : JOYSTICK_TYPE == Joysticks.F310 ? 0 : 0;
	public static int JOYSTICK_AXIS_Y = JOYSTICK_TYPE == Joysticks.XBOX360 ? 1 : JOYSTICK_TYPE == Joysticks.F310 ? 1 : 1;
	public static int JOYSTICK_CAMERA_ZOOM_IN = JOYSTICK_TYPE == Joysticks.XBOX360 ? 5 : JOYSTICK_TYPE == Joysticks.F310 ? 5 : 5;
	public static int JOYSTICK_CAMERA_ZOOM_OUT = JOYSTICK_TYPE == Joysticks.XBOX360 ? 4 : JOYSTICK_TYPE == Joysticks.F310 ? 4 : 4;
	public static int JOYSTICK_MUSIC_PAUSE = JOYSTICK_TYPE == Joysticks.XBOX360 ? 1 : JOYSTICK_TYPE == Joysticks.F310 ? 2 : 1;
	public static int JOYSTICK_MUSIC_SKIP = JOYSTICK_TYPE == Joysticks.XBOX360 ? 0 : JOYSTICK_TYPE == Joysticks.F310 ? 1 : 0;
	public enum Joysticks {
		F310, XBOX360, LOGITECH3D
	}
}
