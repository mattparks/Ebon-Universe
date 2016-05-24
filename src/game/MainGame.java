package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.inputs.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
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
		FlounderDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 3.2f, 7.2f);

		MainGuis.init();
		Environment.init(new Fog(new Colour(0.15f, 0.16f, 0.18f), 0.01f, 2.0f, 0.0f, 50.0f), new Light(new Colour(0.6f, 0.6f, 0.6f), new Vector3f(0.0f, 2000.0f, 2000.0f), new Attenuation(0.0f, 0.0f, 1.0f)));
	}

	@Override
	public void update() {
		if (screenshot.wasDown()) {
			FlounderDevices.getDisplay().screenshot();
		}

		if (fullscreen.wasDown()) {
			FlounderDevices.getDisplay().setFullscreen(!FlounderDevices.getDisplay().isFullscreen());
		}

		if (polygons.wasDown()) {
			OpenglUtils.goWireframe(!OpenglUtils.isInWireframe());
		}

		if (pauseMusic.wasDown()) {
			FlounderDevices.getSound().getMusicPlayer().pauseTrack();
		}

		if (skipMusic.wasDown()) {
			FlounderDevices.getSound().getMusicPlayer().skipTrack();
		}

		MainGuis.update();
		player.update(MainGuis.isMenuOpen());
		super.updateGame(player.getPosition(), player.getRotation(), MainGuis.isMenuOpen(), MainGuis.getBlurFactor());
		Environment.update();
	}

	@Override
	public void dispose() {
	}
}

