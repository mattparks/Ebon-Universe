package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.inputs.*;
import flounder.resources.*;
import flounder.sounds.*;
import game.options.*;

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

		final Playlist playlist = new Playlist();
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "era-of-space.wav"), 0.5f));
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "spacey-ambient.wav"), 0.5f));
		ManagerDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 3.2f, 7.2f);

		MainGuis.init();
	}

	@Override
	public void update() {
		if (screenshot.wasDown()) {
			ManagerDevices.getDisplay().screenshot();
		}

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

		MainGuis.update();
		player.update(MainGuis.isMenuOpen());
		super.updateGame(player.getPosition(), player.getRotation(), MainGuis.isMenuOpen(), MainGuis.getBlurFactor());
	}

	@Override
	public void dispose() {
	}
}

