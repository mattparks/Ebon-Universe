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

	private MainGuis guis;
	private MainPlayer player;

	@Override
	public void init() {
		this.screenshot = new KeyButton(GLFW_KEY_F2);
		this.fullscreen = new KeyButton(GLFW_KEY_F11);
		this.polygons = new KeyButton(GLFW_KEY_P);
		this.pauseMusic = new CompoundButton(new KeyButton(GLFW_KEY_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_PAUSE));
		this.skipMusic = new CompoundButton(new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_SKIP));

		this.guis = new MainGuis();
		this.player = new MainPlayer();

		Playlist playlist = new Playlist();
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(MyFile.RES_FOLDER, "music", "era-of-space.wav"), 1.0f));
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(MyFile.RES_FOLDER, "music", "spacey-ambient.wav"), 1.0f));
		FlounderDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 4.0f, 10.0f);

		Environment.init(new Fog(new Colour(135.0f, 206.0f, 235.0f, true), 0.01f, 2.0f, 0.0f, 50.0f), new Light(new Colour(0.6f, 0.6f, 0.6f), new Vector3f(0.0f, 2000.0f, 2000.0f), new Attenuation(0.0f, 0.0f, 1.0f)));
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
			MainSeed.randomize();
			FlounderDevices.getSound().getMusicPlayer().skipTrack();
		}

		guis.update();
		player.update(guis.isMenuOpen());
		updateGame(player.getPosition(), player.getRotation(), guis.isMenuOpen(), guis.getBlurFactor());
		Environment.update();
	}

	@Override
	public void dispose() {
	}
}

