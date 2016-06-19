package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.fonts.*;
import flounder.helpers.*;
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
	private CompoundButton toggleMusic;
	private CompoundButton skipMusic;

	private MainGuis guis;
	private MainPlayer player;

	private boolean stillLoading;

	@Override
	public void init() {
		TextBuilder.DEFAULT_TYPE = FontManager.FFF_FORWARD; // TODO: move out

		this.screenshot = new KeyButton(GLFW_KEY_F2);
		this.fullscreen = new KeyButton(GLFW_KEY_F11);
		this.polygons = new KeyButton(GLFW_KEY_P);
		this.toggleMusic = new CompoundButton(new KeyButton(GLFW_KEY_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_PAUSE));
		this.skipMusic = new CompoundButton(new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_SKIP));

		this.guis = new MainGuis();
		this.player = new MainPlayer();

		this.stillLoading = true;

		Playlist playlist = new Playlist();
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(MyFile.RES_FOLDER, "music", "era-of-space.wav"), 1.0f));
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(MyFile.RES_FOLDER, "music", "spacey-ambient.wav"), 1.0f));
		FlounderEngine.getDevices().getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 4.0f, 10.0f);

		Environment.init(new Fog(new Colour(135.0f, 206.0f, 235.0f, true), 0.01f, 2.0f, 0.0f, 50.0f), new Light(new Colour(0.6f, 0.6f, 0.6f), new Vector3f(0.0f, 2000.0f, 2000.0f), new Attenuation(0.0f, 0.0f, 1.0f)));
	}

	@Override
	public void update() {
		if (screenshot.wasDown()) {
			FlounderEngine.getDevices().getDisplay().screenshot();
		}

		if (fullscreen.wasDown()) {
			FlounderEngine.getDevices().getDisplay().setFullscreen(!FlounderEngine.getDevices().getDisplay().isFullscreen());
		}

		if (polygons.wasDown()) {
			OpenGlUtils.goWireframe(!OpenGlUtils.isInWireframe());
		}

		if (toggleMusic.wasDown()) {
			if (FlounderEngine.getDevices().getSound().getMusicPlayer().isPaused()) {
				FlounderEngine.getDevices().getSound().getMusicPlayer().unpauseTrack();
			} else {
				FlounderEngine.getDevices().getSound().getMusicPlayer().pauseTrack();
			}
		}

		if (skipMusic.wasDown()) {
			MainSeed.randomize();
			FlounderEngine.getDevices().getSound().getMusicPlayer().skipTrack();
		}

		if (MainGuis.isStartingGame()) {
			// Pause the music for the start screen.
			FlounderEngine.getDevices().getSound().getMusicPlayer().pauseTrack();
		} else if (!MainGuis.isStartingGame() && stillLoading) {
			// Unpause the music for the main menu.
			stillLoading = false;
			FlounderEngine.getLogger().log("Starting main menu music.");
			FlounderEngine.getDevices().getSound().getMusicPlayer().unpauseTrack();
		}

		guis.update();
		player.update(guis.isMenuOpen());
		Environment.update();
		update(player.getPosition(), player.getRotation(), guis.isMenuOpen(), guis.getBlurFactor());
	}

	@Override
	public void dispose() {
	}
}

