import blocks.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.inputs.*;
import options.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainGame extends IGame {
	private KeyButton screenshot;
	private KeyButton fullscreen;
	private KeyButton polygons;
	private CompoundButton pauseMusic;
	private CompoundButton skipMusic;

	private MainPlayer player;

	@Override
	public void init() {
		this.screenshot = new KeyButton(GLFW_KEY_F2);
		this.fullscreen = new KeyButton(GLFW_KEY_F11);
		this.polygons = new KeyButton(GLFW_KEY_P);
		this.pauseMusic = new CompoundButton(new KeyButton(GLFW_KEY_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_PAUSE));
		this.skipMusic = new CompoundButton(new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_SKIP));

		this.player = new MainPlayer();
		WorldManager.init();
	}

	@Override
	public void update() {
		if (fullscreen.wasDown()) {
			ManagerDevices.getDisplay().setFullscreen(!ManagerDevices.getDisplay().isFullscreen());
		}

		if (polygons.wasDown()) {
			OpenglUtils.goWireframe(!OpenglUtils.isInWireframe());
		}

		if (pauseMusic.wasDown()) {
			ManagerDevices.getSound().getMusicPlayer().pauseTrack();
		}

		if (skipMusic.wasDown()) {
			ManagerDevices.getSound().getMusicPlayer().skipTrack();
		}

		final boolean paused = false;
		final float blurFactor = 0.0f;

		player.update(paused);
		super.updateGame(player.getPosition(), player.getRotation(), paused, blurFactor);

		WorldManager.update();
	}

	@Override
	public void dispose() {
	}
}
